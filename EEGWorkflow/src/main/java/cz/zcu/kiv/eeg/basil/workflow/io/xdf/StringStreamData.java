package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class used to store string streams loaded from xdf file
 * <p>
 * Created by Tomas Prokop on 11.02.2019.
 */
public class StringStreamData implements IStreamData<String> {
    private List<String>[] samples;
    private List<Double> timeStamps;
    private List<Double> clockTimes;
    private List<Double> clockValues;

    private int channelCount;

    public StringStreamData(int channels) {
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

    public List<String>[] getSamples() {
        return samples;
    }

    public List<Double> getTimeStamps() {
        return timeStamps;
    }

    public void addSample(String val, int channel) {
        samples[channel].add(val);
    }

    public void addSamples(String[] vals) {
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
