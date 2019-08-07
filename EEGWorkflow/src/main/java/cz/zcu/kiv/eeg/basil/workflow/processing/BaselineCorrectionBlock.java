package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.utils.SignalProcessing;

import java.io.*;
import java.util.ArrayList;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;
import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

/**
 * 
 * Corrects the baseline by subtracting average voltage values in the baseline
 * correction part from the rest of the signal. Especially useful for ERP epochs.
 * 
 * Created by Tomas Prokop on 01.08.2017.
 *  
 */
@BlockType(type="BaselineCorrection", family="Preprocessing", runAsJar = true)
public class BaselineCorrectionBlock implements Serializable {

    @BlockProperty(name="StartTime",type = NUMBER, defaultValue = "")
	private double startTime; /* in milliseconds */

    @BlockProperty(name="EndTime",type = NUMBER, defaultValue = "")
	private double endTime;   /* in milliseconds */

	@BlockInput(name = "EEGData", type = STREAM)
	private PipedInputStream eegPipeIn  = new PipedInputStream();

	@BlockOutput(name = "EEGData", type = STREAM)
	private PipedOutputStream eegPipeOut = new PipedOutputStream();

	private EEGDataPackageList eegDataPackageList=null;

	public BaselineCorrectionBlock(){
		//Required Empty Default Constructor for Workflow Designer
	}

	@BlockExecute
	public void process() throws Exception{

		ObjectInputStream  eegObjectIn  = new ObjectInputStream(eegPipeIn);
		ObjectOutputStream eegObjectOut = new ObjectOutputStream(eegPipeOut);

		EEGDataPackage eegData;
		while ((eegData = (EEGDataPackage) eegObjectIn.readObject())!= null) {
			double[][] data = eegData.getData();

			// for all channels
			for (int i = 0; i < data.length; i++) {
				double sampling = eegData.getConfiguration().getSamplingInterval();
				// calculate the baseline only in the requested interval
				int start = (int) (0.001 * startTime * sampling);
				int end = (int) (0.001 * endTime * sampling);
				double averageBaseline = SignalProcessing.average(data[i], start, end);

				// subtract the baseline from the rest of the signal
				for (int j = 0; j < data[i].length; j++) {
					data[i][j] = data[i][j] - averageBaseline;
				}
			}
			eegObjectOut.writeObject(eegData);
			eegObjectOut.flush();
		}
		eegObjectOut.writeObject(null);
		eegObjectOut.flush();

		eegObjectOut.close();
		eegObjectIn.close();
		eegPipeOut.close();
		eegPipeIn.close();
    }

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

    public EEGDataPackageList getEegDataPackageList() {
        return eegDataPackageList;
    }

    public void setEegDataPackageList(EEGDataPackageList eegDataPackageList) {
        this.eegDataPackageList = eegDataPackageList;
    }
}
