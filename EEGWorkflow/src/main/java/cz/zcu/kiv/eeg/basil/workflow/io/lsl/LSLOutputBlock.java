package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import edu.ucsd.sccn.LSL;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

/**
 * Provides the data as an
 * LabStreamingLayer (LSL) output
 * e.g. for simulated on-line experiments
 * 
 * @author Lukas Vareka
 *
 */
@BlockType(type="LSLOutput", family = "DataProvider", runAsJar = true)
public class LSLOutputBlock {
	/**
	 * Input data to be streamed
	 */

	@BlockInput(name = "EEGData", type = STREAM)
	private PipedInputStream eegPipeIn = new PipedInputStream();

	// @BlockInput(name = "EEGData", type = "EEGDataList")
	private EEGDataPackageList eegDataPackageList;

	@BlockExecute
	public void process() throws IOException, InterruptedException, ClassNotFoundException {

		ObjectInputStream eegObjectIn = new ObjectInputStream(eegPipeIn);
		EEGDataPackage eegDataObj = (EEGDataPackage) eegObjectIn.readObject();
		if(eegDataObj == null) return;

		int nchannels = 0;
		double srate  = 0;
		if(eegDataObj.getConfiguration() != null) {
			nchannels = eegDataObj.getData().length;
			srate     = eegDataObj.getConfiguration().getSamplingInterval();
		}
				
		
		//establish a LSL stream
		LSL.StreamInfo infoMarkers = new LSL.StreamInfo("LSLMarkers", "Markers", 1, LSL.IRREGULAR_RATE, LSL.ChannelFormat.string, "myuid4563");
		LSL.StreamOutlet outletMarkers = new LSL.StreamOutlet(infoMarkers);
		
		LSL.StreamInfo infoEEG = new LSL.StreamInfo("LSLEEG", "EEG", nchannels, srate, LSL.ChannelFormat.float32, "myuid324457");
		LSL.StreamOutlet outletEEG = new LSL.StreamOutlet(infoEEG);
		 
		LSLMarkerOutput markerOutput = new LSLMarkerOutput(outletMarkers);
		LSLEEGOutput eegOutput = new LSLEEGOutput(outletEEG);

		setData(eegDataObj, markerOutput, eegOutput);
		System.out.println("In LSLOutputBlock: PipedIn receives data: " + eegDataObj.toString());

		// set data
		while ((eegDataObj = (EEGDataPackage) eegObjectIn.readObject())!= null) {
			setData(eegDataObj, markerOutput, eegOutput);
		}

		eegObjectIn.close();
		eegPipeIn.close();
	}


	private void setData(EEGDataPackage eegDataPackage, LSLMarkerOutput markerOutput, LSLEEGOutput eegOutput) throws InterruptedException {
		double[][] data 		= eegDataPackage.getData();
		List<EEGMarker> markers = eegDataPackage.getMarkers();
		for(EEGMarker eegMaker: markers){
			System.out.println(eegMaker.toString());
		}

		markerOutput.setData(markers);
		markerOutput.start();

		eegOutput.setData(data);
		eegOutput.start();

		eegOutput.join();
		markerOutput.join();

	}

	public EEGDataPackageList getEegDataPackageList() {
		return eegDataPackageList;
	}

	public void setEegDataPackageList(EEGDataPackageList eegDataPackageList) {
		this.eegDataPackageList = eegDataPackageList;
	}

}
