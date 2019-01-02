package cz.zcu.kiv.eeg.basil.workflow.classification;

import java.util.List;

import cz.zcu.kiv.eeg.basil.data.FeatureVector;

/**
 * Created by Tomas Prokop on 15.08.2017.
 */
public interface ITrainCondition {
    boolean canAddSample(String targetMarker, String marker);
    void addSample(FeatureVector fv, String targetMarker, String marker);
    List<FeatureVector> getFeatureVectors();
    
}
