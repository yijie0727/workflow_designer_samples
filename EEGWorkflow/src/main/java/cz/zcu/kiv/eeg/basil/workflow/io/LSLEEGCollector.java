package cz.zcu.kiv.eeg.basil.workflow.io;


import cz.zcu.kiv.eeg.basil.data.Configuration;
import edu.ucsd.sccn.LSL;
import edu.ucsd.sccn.LSL.XMLElement;

/**
 * 
 * Collects samples using EEG LSL stream
 * @author lvareka
 *
 */
public class LSLEEGCollector extends Thread {
	private LSLDataProvider messageProvider; /* reference to EEG/ERP data provider */
	private volatile boolean          running;
		
	public LSLEEGCollector(LSLDataProvider messageProvider) {
		this.messageProvider = messageProvider;
		this.running = true;
	}

	public void run()  {
		 	LSL.StreamInfo[] eegData = LSL.resolve_stream("type", "EEG");
		 	
		   
		    try {
		    	LSL.StreamInlet eegInlet = new LSL.StreamInlet(eegData[0]);
		    	String streamName = eegInlet.info().name();
		    	double srate = eegInlet.info().nominal_srate();
		    	XMLElement metadata = eegInlet.info().desc();
		    	
		    	Configuration configuration=new Configuration();
	            configuration.setSamplingInterval(srate);
		    	float[] eegSample = new float[eegInlet.info().channel_count()];
		       

		        while (running) { /* collects and sends one sample for multiple EEG channels */
	                eegInlet.pull_sample(eegSample);
		            messageProvider.addEEGSample(eegSample.clone(), configuration);
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	
	public void terminate() {
		this.running = false;
	}
	

}
