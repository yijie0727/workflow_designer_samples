package cz.zcu.kiv.eeg.basil.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Prokop on 01.07.2019.
 */
public class ClassificationResults implements Serializable {
    private List<Double> expectedOutputs;
    private List<Double> realOutputs;
    private List<String> markers;
    private List<Integer> results;

    public ClassificationResults() {
        expectedOutputs = new ArrayList<>();
        realOutputs = new ArrayList<>();
        markers = new ArrayList<>();
        results = new ArrayList<>();
    }

    public void Add(double expected, double real, String marker, int result){
        expectedOutputs.add(expected);
        realOutputs.add(real);
        markers.add(marker);
        results.add(result);
    }

    public List<Double> getExpectedOutputs() {
        return expectedOutputs;
    }

    public List<Double> getRealOutputs() {
        return realOutputs;
    }

    public List<String> getMarkers() {
        return markers;
    }

    public List<Integer> getResults() {
        return results;
    }
}
