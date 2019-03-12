package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import java.util.HashMap;

/**
 * Created by Tomas Prokop on 28.01.2019.
 */
public class XdfFileData {
    private FileHeader header;
    private HashMap<Integer, StreamHeader> streamHeaders;

    /**
     * XML footer for each stream
     */
    private HashMap<Integer, HashMap<String, Object>> streamFooters;

    /**
     * Clock data, time values and time series
     */
    private HashMap<Integer, IStreamData> data;

    public XdfFileData() {
        streamHeaders = new HashMap<>();
        streamFooters = new HashMap<>();
        data = new HashMap<>();
    }

    public FileHeader getHeader() {
        return header;
    }

    public void setHeader(FileHeader header) {
        this.header = header;
    }

    public HashMap<Integer, StreamHeader> getStreamHeaders() {
        return streamHeaders;
    }

    public HashMap<Integer, HashMap<String, Object>> getStreamFooters() {
        return streamFooters;
    }

    public HashMap<Integer, IStreamData> getData() {
        return data;
    }

    public IStreamData getData(int id) {
        return data.get(id);
    }

    public StreamHeader getHeader(int id) {
        return streamHeaders.get(id);
    }

    public HashMap<String, Object> getFooter(int id) {
        return streamFooters.get(id);
    }

    public void addStreamHeader(int id, StreamHeader header) {
        streamHeaders.put(id, header);
    }

    public void addStreamFooter(int id, HashMap<String, Object> footer) {
        streamFooters.put(id, footer);
    }

    public void addStreamData(int id, IStreamData sData) {
        data.put(id, sData);
    }
}
