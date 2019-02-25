package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Map;

/**
 * Created by Tomas Prokop on 21.01.2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamHeader {

    private String name;

    private String type;

    @JacksonXmlProperty(localName = "channel_count")
    private int channelCount;

    @JacksonXmlProperty(localName = "nominal_srate")
    private double sampling;

    @JacksonXmlProperty(localName = "channel_format")
    private ChannelFormat channelFormat;

    @JacksonXmlProperty(localName = "source_id")
    private String sourceId;

    private double version;

    @JacksonXmlProperty(localName = "created_at")
    private double createdAt;

    private String uid;

    @JacksonXmlProperty(localName = "session_id")
    private String sessionId;

    private String hostname;

    private EEGDescription desc;

    private double tDiff;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public double getSampling() {
        return sampling;
    }

    public void setSampling(double sampling) {
        this.sampling = sampling;
        this.tDiff = sampling > 0 ? 1.0 / sampling : 0;
    }

    public ChannelFormat getChannelFormat() {
        return channelFormat;
    }

    public void setChannelFormat(ChannelFormat channelFormat) {
        this.channelFormat = channelFormat;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(double createdAt) {
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public EEGDescription getDesc() {
        return desc;
    }

    public void setDesc(EEGDescription desc) {
        this.desc = desc;
    }

    public double getTimeDiff() {
        return tDiff;
    }
}
