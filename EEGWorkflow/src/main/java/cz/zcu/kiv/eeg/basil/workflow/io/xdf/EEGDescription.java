package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EEGDescription {
    private String manufacturer;
    private CapInfo cap;

    @JacksonXmlProperty(localName = "channels")
    private ChannelsInfo channelsInfo;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public CapInfo getCap() {
        return cap;
    }

    public void setCap(CapInfo cap) {
        this.cap = cap;
    }

    public ChannelsInfo getChannelsInfo() {
        return channelsInfo;
    }

    public void setChannelsInfo(ChannelsInfo channelsInfo) {
        this.channelsInfo = channelsInfo;
    }
}
