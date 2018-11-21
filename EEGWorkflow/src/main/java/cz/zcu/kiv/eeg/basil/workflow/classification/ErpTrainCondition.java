package cz.zcu.kiv.eeg.basil.workflow.classification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.zcu.kiv.eeg.basil.data.FeatureVector;

/**
 * Created by Tomas Prokop on 15.08.2017.
 */
public class ErpTrainCondition implements ITrainCondition, Serializable {

    private int targetCnt = 0;
    private int nontargetCnt = 0;

    private ArrayList<FeatureVector> featureVectors = new ArrayList<>();

    @Override
    public boolean canAddSample(String targetMarker, String marker) {
        boolean isTarget = isTarget(targetMarker, marker);
        return  canAddSample(isTarget);
    }

    public int getTargetCnt() {
        return targetCnt;
    }

    public int getNontargetCnt() {
        return nontargetCnt;
    }

    private boolean canAddSample(boolean isTarget){
        if (isTarget) {
            if (targetCnt <= nontargetCnt)
                return true;
        } else {
            if (nontargetCnt <= targetCnt)
                return true;
        }

        return false;
    }

    @Override
    public void addSample(FeatureVector fv, String targetMarker, String marker) {
        boolean isTarget = isTarget(targetMarker, marker); 
        if (canAddSample(isTarget)) {
            if(isTarget) {
                targetCnt++;
            }
            else {
                nontargetCnt++;
            }

            fv.setExpectedOutput(isTarget ? 1.0 : 0.0);
            featureVectors.add(fv);
        }
    }

    private boolean isTarget(String targetMarker, String marker){
    	if (targetMarker == null) return false;
        return targetMarker.equals(marker);
    }

    @Override
    public List<FeatureVector> getFeatureVectors() {
        return featureVectors;
    }

	

   
}
