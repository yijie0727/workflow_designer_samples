package cz.zcu.kiv.eeg.basil.workflow.io.lsl;


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
	private LSLDataProviderBlock messageProvider; /* reference to EEG/ERP data provider */
	private volatile boolean          running;
	private LSL.StreamInlet eegInlet;
		
	public LSLEEGCollector(LSLDataProviderBlock messageProvider) {
		this.messageProvider = messageProvider;
		this.running = true;
	}

	public void run()  {
		 	LSL.StreamInfo[] eegData = LSL.resolve_stream("type", "EEG");
		 	
		   
		    try {
		    	eegInlet = new LSL.StreamInlet(eegData[0]);
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
		    
		    if (eegInlet != null) {
				eegInlet.close();
			}
	}

	
	public void terminate() {
		this.running = false;
	}
	

}
