package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

/**
 * Created by Tomas Prokop on 14.01.2019.
 */
public class XdfRecord {
    private XdfData data;
    private FileHeader header;

    public XdfRecord() {
    }

    public XdfRecord(XdfData data, FileHeader header) {
        this.data = data;
        this.header = header;
    }

    public XdfData getData() {
        return data;
    }

    public void setData(XdfData data) {
        this.data = data;
    }

    public FileHeader getHeader() {
        return header;
    }

    public void setHeader(FileHeader header) {
        this.header = header;
    }
}
