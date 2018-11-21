package cz.zcu.kiv.eeg.basil.workflow.io;


import edu.ucsd.sccn.LSL;

/**
 * Collects markers from LSL Markers stream
 * @author lvareka
 *
 */
public class LSLMarkerCollector extends Thread {
	private LSLDataProvider messageProvider; /* reference to EEG/ERP data provider */
	private volatile boolean running;
	
	public LSLMarkerCollector(LSLDataProvider messageProvider) {
		this.messageProvider = messageProvider;
		this.running         = true;
	}

	@Override
	public void run() {
		// 
		LSL.StreamInfo[] markers = LSL.resolve_stream("type", "Markers");
		try {
			LSL.StreamInlet markersInlet = new LSL.StreamInlet(markers[0]);
			String[] marker = new String[1];
	        while (running) { /* collects and transfers the current marker */
	           markersInlet.pull_sample(marker);
               messageProvider.addMarker(marker.clone());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void terminate() {
		this.running = false;
	}

}
