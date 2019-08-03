package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;

import java.io.*;

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

    @BlockInput (name = "EEGData", type = "EEGDataPipeStream")
    private PipedInputStream eegPipeIn = new PipedInputStream();

    @BlockOutput(name = "EEGData", type = "EEGDataPipeStream")
    private PipedOutputStream eegPipeOut = new PipedOutputStream();


    public IntervalSelectionBlock(){
        //Required Empty Default Constructor for Workflow Designer
    }

    @BlockExecute
    public void preprocess() throws IOException, ClassNotFoundException {

        ObjectInputStream eegObjectIn  = new ObjectInputStream(eegPipeIn);
        ObjectOutputStream eegObjectOut = new ObjectOutputStream(eegPipeOut);

        EEGDataPackage pcg;
        while ((pcg = (EEGDataPackage) eegObjectIn.readObject())!= null) {
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
