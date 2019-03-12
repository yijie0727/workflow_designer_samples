package cz.zcu.kiv.eeg.basil.workflow.io.xdf;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tomas Prokop on 11.02.2019.
 */
public interface IStreamData<T> {

    int getChannelCount();

    List<Double> getClockTimes();

    List<Double> getClockValues();

    List<T>[] getSamples();

    List<Double> getTimeStamps();

    void addSample(T val, int channel);

    void addSamples(T[] vals);

    void addTimeStamps(Double[] vals);

    void addClockTime(double stamp);

    void addClockValue(double value);
}
