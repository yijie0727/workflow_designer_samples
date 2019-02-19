package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Tomas Prokop on 14.01.2019.
 */
public class XdfReader {
    private XdfFileData xdfData;

    public XdfFileData getXdfData() {
        return xdfData;
    }

    public boolean read(String file) {
        File f = new File(file);
        if (!f.exists())
            return false;

        try {
            xdfData = new XdfFileData();

            byte[] buffer = new byte[4];
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(buffer);
            String s = new String(buffer, StandardCharsets.UTF_8);
            if (!s.equals("XDF:")) {
                return false;
            }

            int chunks = 0;
            boolean readChunk = true;
            while (readChunk) {
                long len = readVarLenInteger(bis);
                if (len == -1) //eof
                    break;

                short tag = readTag(bis);
                System.out.println(tag);
                String xml = null;

                XmlMapper mapper;
                chunks++;

                switch (tag) {
                    case 1: //file header
                        buffer = new byte[(int) len - 2]; //todo what if len > int.MaxValue???
                        bis.read(buffer);
                        xml = new String(buffer, StandardCharsets.UTF_8);
                        mapper = new XmlMapper();
                        FileHeader header = mapper.readValue(xml, FileHeader.class);
                        xdfData.setHeader(header);
                        break;
                    case 2: //stream header
                        int id = readStreamId(bis);
                        buffer = new byte[(int) len - 6]; //todo what if len > int.MaxValue???
                        bis.read(buffer);
                        xml = new String(buffer, StandardCharsets.UTF_8);
                        mapper = new XmlMapper();
                        StreamHeader sHeader = mapper.readValue(xml, StreamHeader.class);
                        xdfData.addStreamHeader(id, sHeader);
                        xdfData.addStreamData(id, createStreamData(sHeader));
                        break;
                    case 3: //data chunk
                        id = readStreamId(bis);
                        sHeader = xdfData.getHeader(id);
                        long varLen = readVarLenInteger(bis);
                        if (sHeader.getChannelFormat() != ChannelFormat.string) {
                            readData(bis, varLen, id);
                        } else {
                            readStringData(bis, varLen, id);
                        }

                        break;
                    case 4: //clock offset chunk
                        id = readStreamId(bis);
                        buffer = new byte[8];
                        bis.read(buffer);
                        ByteBuffer wrapped = ByteBuffer.wrap(buffer);
                        wrapped.order(ByteOrder.LITTLE_ENDIAN);
                        xdfData.getData(id).addClockTime(wrapped.getDouble());
                        bis.read(buffer);
                        wrapped = ByteBuffer.wrap(buffer);
                        xdfData.getData(id).addClockValue(wrapped.getDouble());
                        break;
                    case 6: //footer
                        id = readStreamId(bis);
                        buffer = new byte[(int) len - 6];
                        bis.read(buffer);
                        mapper = new XmlMapper();
                        xml = new String(buffer, StandardCharsets.UTF_8);
                        HashMap<String, Object> footer = mapper.readValue(xml, HashMap.class);
                        xdfData.addStreamFooter(id, footer);
                        break;
                    default: //just read chunk like boundary and don't care about it
                        buffer = new byte[(int) len - 2];
                        bis.read(buffer);
                        break;
                }
            }

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void readStringData(InputStream bis, long len, int id) throws IOException {
        StreamHeader header = xdfData.getHeader(id);
        IStreamData data = xdfData.getData(id);

        byte[] buffer;
        Double[] stamps = new Double[(int) len];
        Arrays.fill(stamps, 0.0);

        double lastStamp = 0;
        int size = data.getTimeStamps().size();
        if (size > 0) {
            lastStamp = (double) data.getTimeStamps().get(size - 1);
        }

        ByteBuffer wrap = ByteBuffer.allocate(8);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        int bytesToRead = -1;

        for (int i = 0; i < len; i++) {
            int b = bis.read();

            if (b > 0) { //timestamp available
                buffer = new byte[8];
                bis.read(buffer);
                wrap.put(buffer);
                wrap.rewind();
                stamps[i] = wrap.getDouble();
                wrap.clear();
            } else { //timestamp needs to be computed
                stamps[i] = lastStamp + header.getTimeDiff();
            }

            for (int k = 0; k < header.getChannelCount(); k++) {
                bytesToRead = (int) readVarLenInteger(bis);
                buffer = new byte[bytesToRead];
                bis.read(buffer);
                String value = new String(buffer);
                data.addSample(value, k);
            }
        }

        data.addTimeStamps(stamps);
    }

    private void readData(InputStream bis, long len, int id) throws IOException {
        StreamHeader header = xdfData.getHeader(id);
        IStreamData data = xdfData.getData(id);

        ByteBuffer wrap = ByteBuffer.allocate(1024);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer;
        Double[] stamps = new Double[(int) len];
        Arrays.fill(stamps, 0.0);
        int bytesToRead = header.getChannelCount() * ChannelFormat.getBytesCount(header.getChannelFormat());

        double lastStamp = 0;
        int size = data.getTimeStamps().size();
        if (size > 0) {
            lastStamp = (double) data.getTimeStamps().get(size - 1);
        }

        for (int i = 0; i < len; i++) {
            int b = bis.read();

            if (b > 0) { //timestamp available
                buffer = new byte[8];
                bis.read(buffer);
                wrap.put(buffer);
                wrap.rewind();
                stamps[i] = wrap.getDouble();
                wrap.clear();
            } else { //timestamp needs to be computed
                stamps[i] = lastStamp + header.getTimeDiff();
            }

            buffer = new byte[bytesToRead];
            bis.read(buffer);
            wrap.put(buffer);
            wrap.rewind();
            for (int k = 0; k < header.getChannelCount(); k++) {
                switch (header.getChannelFormat()) {
                    case int8:
                        data.addSample(buffer[k], k);
                        break;
                    case int16:
                        data.addSample(wrap.getShort(), k);
                        break;
                    case int32:
                        data.addSample(wrap.getInt(), k);
                        break;
                    case int64:
                        data.addSample(wrap.getLong(), k);
                        break;
                    case float32:
                        data.addSample(wrap.getFloat(), k);
                        break;
                    case double64:
                        data.addSample(wrap.getDouble(), k);
                        break;
                    default:
                        break;
                }
            }
            wrap.clear();
        }

        data.addTimeStamps(stamps);
    }

    private IStreamData createStreamData(StreamHeader header) {
        int channels = header.getChannelCount();
        switch (header.getChannelFormat()) {
            case int8:
                return new NumberStreamData<Byte>(channels);
            case int16:
                return new NumberStreamData<Short>(channels);
            case int32:
                return new NumberStreamData<Integer>(channels);
            case int64:
                return new NumberStreamData<Long>(channels);
            case float32:
                return new NumberStreamData<Float>(channels);
            case double64:
                return new NumberStreamData<Double>(channels);
            default:
                return new StringStreamData(channels);
        }
    }

    private int readStreamId(InputStream is) throws IOException {
        byte[] buff = new byte[4];
        is.read(buff);
        ByteBuffer wrapped = ByteBuffer.wrap(buff);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);

        return wrapped.getInt();
    }

    private short readTag(InputStream is) throws IOException {
        byte[] buff = new byte[2];
        is.read(buff);
        ByteBuffer wrapped = ByteBuffer.wrap(buff);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);

        return wrapped.getShort();
    }

    private long readVarLenInteger(InputStream is) throws IOException {
        int n = is.read();
        if (n == -1)
            return n;

        if (n == 1) {
            return is.read();
        }

        if (n != 4 && n != 8)
            throw new IOException();

        byte[] buff = new byte[n];
        is.read(buff);
        ByteBuffer wrapped = ByteBuffer.wrap(buff);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);

        return n == 4 ? wrapped.getInt() : wrapped.getLong();
    }
}
