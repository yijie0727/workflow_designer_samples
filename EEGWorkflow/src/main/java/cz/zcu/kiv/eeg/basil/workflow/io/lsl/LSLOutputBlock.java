package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import java.io.IOException;
import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import edu.ucsd.sccn.LSL;

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
	@BlockProperty(name = "EEGData", type = "EEGDataList")
	private EEGDataPackageList eegDataPackageList;
	
	@BlockExecute
	public void process() throws IOException, InterruptedException {
		if (this.eegDataPackageList == null) {
			return;
		}
		List<EEGDataPackage> eegDataPackages = this.eegDataPackageList.getEegDataPackage();
		int nchannels = 0;
		double srate  = 0;
		if (eegDataPackages.size() > 0 && eegDataPackages.get(0).getConfiguration() != null) {
			nchannels = eegDataPackages.get(0).getData().length;
			srate     = eegDataPackages.get(0).getConfiguration().getSamplingInterval();
		}
				
		
		//establish a LSL stream
		LSL.StreamInfo infoMarkers = new LSL.StreamInfo("LSLMarkers", "Markers", 1, LSL.IRREGULAR_RATE, LSL.ChannelFormat.string, "myuid4563");
		LSL.StreamOutlet outletMarkers = new LSL.StreamOutlet(infoMarkers);
		
		LSL.StreamInfo infoEEG = new LSL.StreamInfo("LSLEEG", "EEG", nchannels, srate, LSL.ChannelFormat.float32, "myuid324457");
		LSL.StreamOutlet outletEEG = new LSL.StreamOutlet(infoEEG);
		 
		LSLMarkerOutput markerOutput = new LSLMarkerOutput(outletMarkers);
		LSLEEGOutput eegOutput = new LSLEEGOutput(outletEEG);
				
		// send data
		for (EEGDataPackage eegDataPackage: eegDataPackages) {
			double[][] data = eegDataPackage.getData();
			List<EEGMarker> markers = eegDataPackage.getMarkers();
						
			markerOutput.setData(markers);
			markerOutput.start();
			
			eegOutput.setData(data);
			eegOutput.start();
						
			markerOutput.join();
			eegOutput.join();
		}
	}

}
