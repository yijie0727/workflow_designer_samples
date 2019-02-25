package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by Tomas Prokop on 25.02.2019.
 */
public class ChannelLocation {
    @JacksonXmlProperty(localName = "X")
    private double x;

    @JacksonXmlProperty(localName = "Y")
    private double y;

    @JacksonXmlProperty(localName = "Z")
    private double z;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
