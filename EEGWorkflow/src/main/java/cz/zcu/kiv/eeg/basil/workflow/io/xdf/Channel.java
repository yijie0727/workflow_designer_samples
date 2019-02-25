package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
public class Channel {
    private String label;
    private ChannelLocation location;

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
