package cz.zcu.kiv.eeg.basil.data;

import java.io.Serializable;

public class Configuration implements Serializable {
    private double samplingInterval;
    private int preStimulus;
    private int postStimulus;

    public int getPreStimulus() {
        return preStimulus;
    }

    public void setPreStimulus(int preStimulus) {
        this.preStimulus = preStimulus;
    }

    public int getPostStimulus() {
        return postStimulus;
    }

    public void setPostStimulus(int postStimulus) {
        this.postStimulus = postStimulus;
    }

    public double getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(double samplingInterval) {
        this.samplingInterval = samplingInterval;
    }
}
