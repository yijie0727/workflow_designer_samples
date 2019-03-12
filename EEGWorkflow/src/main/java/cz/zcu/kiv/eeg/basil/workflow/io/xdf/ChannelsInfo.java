package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelsInfo {
    private String type;

    @JacksonXmlProperty(localName = "channel")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Channel> channels;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
