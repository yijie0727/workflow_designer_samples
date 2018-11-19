package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.math.IirBandpassFilter;

import java.io.Serializable;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

/**
 * Bandpass filter block
 * @author Prokop
 */
@BlockType(type="FilterBlock",family = "Preprocessing", runAsJar = true)
public class BandpassFilterBlock implements Serializable {

    @BlockProperty(name="Lower cutoff frequency",type=NUMBER, defaultValue = "1")
    private double lowFreq;

    @BlockProperty(name="High cutoff frequency",type=NUMBER, defaultValue = "30")
    private double highFreq;

    private IirBandpassFilter filter;

    @BlockOutput(name = "EEGData", type = "EEGDataList")
    @BlockInput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList eegData;

    public BandpassFilterBlock(){
        //Required Empty Default Constructor for Workflow Designer
    }

    @BlockExecute
    public void preprocess() {
        for(EEGDataPackage pcg : eegData.getEegDataPackage()) {
            if (filter == null)
                this.filter = new IirBandpassFilter(lowFreq, highFreq, (int) pcg.getConfiguration().getSamplingInterval());

            double[][] data = pcg.getData();

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = filter.getOutputSample(data[i][j]);
                }
                // reset the memory of the filter
                filter.reset();
            }
        }
    }
}
