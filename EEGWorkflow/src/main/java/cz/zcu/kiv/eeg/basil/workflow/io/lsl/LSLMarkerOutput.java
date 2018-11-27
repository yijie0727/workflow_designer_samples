package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import java.util.List;

import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import edu.ucsd.sccn.LSL.StreamOutlet;

public class LSLMarkerOutput extends Thread {
	private StreamOutlet outletMarkers;
	private List<EEGMarker> markers;
	
	public LSLMarkerOutput(StreamOutlet outletMarkers) {
		this.outletMarkers = outletMarkers;
	}
	
	public void setData(List<EEGMarker> markers) {
		this.markers = markers;
		
	}

	@Override
	public void run() {
		int timer = 0;
		for (EEGMarker marker: markers) {
			int delay = marker.getOffset() - timer;
			outletMarkers.push_sample(new String[] {marker.getName()});
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timer = marker.getOffset();
		}
		
	}

	

}
