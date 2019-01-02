package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

@BlockType(type="EpochExtraction",family = "Preprocessing", runAsJar = true)
public class EpochExtractionBlock implements Serializable {

    @BlockProperty(name="PreStimulus onset",type=NUMBER, defaultValue = "0")
    private int preStimulus;  /* time before the stimulus onset in ms */

    @BlockProperty(name="PostStimulus onset",type=NUMBER, defaultValue = "0")
    private int postStimulus; /* time after the stimulus onset in ms */

    @BlockInput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList eegDataPackageList;

    @BlockOutput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList epochs;


    @BlockExecute
    public void process(){

        ArrayList<EEGDataPackage> epochsList = new ArrayList<>();
        for(EEGDataPackage eegData:eegDataPackageList.getEegDataPackage()){

            List<EEGMarker> markers = eegData.getMarkers();
            double[][]        data  = eegData.getData();
            double sampling = eegData.getConfiguration().getSamplingInterval();

            for (EEGMarker currentMarker: markers) {
                int startSample =  - (int)((0.001 * this.preStimulus) /* time in s */ * sampling);
                int endSample = (int) ((0.001 * this.postStimulus) /* time in s */ * sampling);
                int offset = currentMarker.getOffset();
                double[][] epochData = new double[data.length][endSample - startSample];

                if (offset + startSample < 0 || offset + endSample >= data[0].length) {
                    System.err.println("Epoch outside of the expected range. Data length: " + data.length + ", startSample: " + startSample + ", endSample: " + endSample);
                    continue; /* epoch prestimulus offset outside of the range */
                }
                for (int i = 0; i < data.length; i++) {
                    System.arraycopy(data[i], offset + startSample , epochData[i], 0, endSample - startSample);
                }

                epochsList.add(new EEGDataPackage(epochData, Arrays.asList(currentMarker), eegData.getChannelNames(),eegData.getConfiguration()));
            }
        }
        epochs=new EEGDataPackageList(epochsList);
    }

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

    public EEGDataPackageList getEegDataPackageList() {
        return eegDataPackageList;
    }

    public void setEegDataPackageList(EEGDataPackageList eegDataPackageList) {
        this.eegDataPackageList = eegDataPackageList;
    }

    public EEGDataPackageList getEpochs() {
        return epochs;
    }

    public void setEpochs(EEGDataPackageList epochs) {
        this.epochs = epochs;
    }
}
