package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;

import java.io.Serializable;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

/**
 * Selects specified data interval from given set of EEG data
 * Created by Tomas Prokop on 17.09.2018.
 */
@BlockType(type="IntervalSelectionBlock",family = "Preprocessing", runAsJar = true)
public class IntervalSelectionBlock implements Serializable {

    @BlockProperty(name="Start index",type=NUMBER, defaultValue = "200")
    private int startIndex;

    @BlockProperty(name="Samples",type=NUMBER, defaultValue = "512")
    private int samples;

    @BlockOutput(name = "EEGData", type = "EEGDataList")
    @BlockInput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList eegData;

    public IntervalSelectionBlock(){
        //Required Empty Default Constructor for Workflow Designer
    }

    @BlockExecute
    public void preprocess() {
        for(EEGDataPackage pcg : eegData.getEegDataPackage()) {
            int start = startIndex, samp = samples;

            double[][] originalEegData = pcg.getData();
            int dataLen = originalEegData[0].length;
            if(startIndex < 0 || startIndex >= dataLen)
                start = 0;

            if(samples < 0 || samples >= dataLen - start + 1)
                samp = dataLen - start + 1;

            double[][] reducedData = new double[originalEegData.length][samp];

            for (int i = 0; i < originalEegData.length; i++) {
                System.arraycopy(originalEegData[i], start, reducedData[i], 0, samp);
            }

            pcg.setData(reducedData);
        }
    }
}
