package cz.zcu.kiv.eeg.basil.data;

import java.io.Serializable;

public class Configuration implements Serializable {
    private double samplingInterval;

    public double getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(double samplingInterval) {
        this.samplingInterval = samplingInterval;
    }
}
