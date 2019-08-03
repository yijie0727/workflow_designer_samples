package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.math.IirBandpassFilter;

import java.io.*;

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

    @BlockInput (name = "EEGData", type = "EEGDataPipeStream")
    private PipedInputStream  eegPipeIn = new PipedInputStream();

    @BlockOutput(name = "EEGData", type = "EEGDataPipeStream")
    private PipedOutputStream eegPipeOut = new PipedOutputStream();


    public BandpassFilterBlock(){
        //Required Empty Default Constructor for Workflow Designer
    }

    @BlockExecute
    public void preprocess() throws IOException, ClassNotFoundException {

        ObjectInputStream  eegObjectIn  = new ObjectInputStream(eegPipeIn);
        ObjectOutputStream eegObjectOut = new ObjectOutputStream(eegPipeOut);

        EEGDataPackage pcg;
        while ((pcg = (EEGDataPackage) eegObjectIn.readObject())!= null) {
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
            eegObjectOut.writeObject(pcg);
            eegObjectOut.flush();
        }

        eegObjectOut.writeObject(null);
        eegObjectOut.flush();

        eegObjectIn.close();
        eegObjectOut.close();
        eegPipeIn.close();
        eegPipeOut.close();
    }
}
