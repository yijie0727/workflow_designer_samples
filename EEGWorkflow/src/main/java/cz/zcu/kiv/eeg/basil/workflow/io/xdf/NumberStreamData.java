package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class used to store number streams (e.g. double, int,...) loaded from xdf file
 * Created by Tomas Prokop on 28.01.2019.
 */
public class NumberStreamData<T extends Number> implements IStreamData<T> {
    private List<T>[] samples;
    private List<Double> timeStamps;
    private List<Double> clockTimes;
    private List<Double> clockValues;

    private int channelCount;

    public NumberStreamData(int channels) {
        channelCount = channels;
        samples = new List[channels];
        for (int i = 0; i < channels; i++) {

            samples[i] = new ArrayList<>();
        }
        clockTimes = new ArrayList<>();
        clockValues = new ArrayList<>();
        timeStamps = new ArrayList<>();
    }

    @Override
    public int getChannelCount() {
        return channelCount;
    }

    public List<Double> getClockTimes() {
        return clockTimes;
    }

    public List<Double> getClockValues() {
        return clockValues;
    }

    public List<T>[] getSamples() {
        return samples;
    }

    public List<Double> getTimeStamps() {
        return timeStamps;
    }

    public void addSample(T val, int channel) {
        samples[channel].add(val);
    }

    public void addSamples(T[] vals) {
        if (vals.length < samples.length)
            throw new ArrayIndexOutOfBoundsException("vals lenght is not equal to channel count");

        for (int i = 0; i < vals.length; i++) {
            samples[i].add(vals[i]);
        }
    }

    public void addTimeStamps(Double[] vals) {
        Collections.addAll(timeStamps, vals);
    }

    public void addClockTime(double stamp) {
        clockTimes.add(stamp);
    }

    public void addClockValue(double value) {
        clockValues.add(value);
    }
}
