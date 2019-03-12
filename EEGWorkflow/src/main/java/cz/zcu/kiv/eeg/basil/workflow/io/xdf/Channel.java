package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {
    private String label;
    private String type;
    private String unit;
    private ChannelLocation location;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ChannelLocation getLocation() {
        return location;
    }

    public void setLocation(ChannelLocation location) {
        this.location = location;
    }
}
