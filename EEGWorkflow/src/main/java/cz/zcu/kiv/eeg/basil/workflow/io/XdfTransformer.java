package cz.zcu.kiv.eeg.basil.workflow.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.IStreamData;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.StreamHeader;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfFileData;

public class XdfTransformer {

	
	// input data
	private XdfFileData xdfData;
	private String eegStreamName;
	private String markerStreamName; 
	
	// output data
	private EEGDataPackage eegData;
	
	// supporting data
	private int eegKey;
	private int markerKey;
	
	
	public XdfTransformer(XdfFileData data, String eegStreamName, String markerStreamName) {
		this.xdfData = data;
		this.eegData = new EEGDataPackage();
		this.eegStreamName = eegStreamName;
		this.markerStreamName = markerStreamName;
	}

	public boolean convertData() {
		if (this.xdfData == null) {
			return false;
		}
			
		// find EEG stream
		// find marker stream
		setEEGMarkerStreams(this.xdfData.getStreamHeaders());
		
		// set sampling rate		
		Configuration configuration = new Configuration();
        configuration.setSamplingInterval(this.xdfData.getStreamHeaders().get(eegKey).getSampling());
        eegData.setConfiguration(configuration);
        
        // set EEG data
        IStreamData stream 		  = this.xdfData.getData(eegKey);
        List<Float>[] streamData = stream.getSamples();
        double[][] allData		  = new double[streamData.length][streamData[0].size()];
        for (int i = 0; i < streamData.length; i++) {
        	double[] channelData = new double[streamData[i].size()];
        	for (int j = 0; j < streamData[i].size(); j++) {
        		channelData[j] = (double) streamData[i].get(j);
        	}
        	allData[i] = channelData;
        }
        eegData.setData(allData);
        
        // set EEG markers
        IStreamData streamMarkers 		  = this.xdfData.getData(markerKey);
		List<Integer>[] markers = streamMarkers.getSamples();
		
		
        
        
        
        /*EEGMarker[] eegMarkers = new EEGMarker[markers.size()];
        int i = 0;
        for (cz.zcu.kiv.signal.EEGMarker m : markers) {
            eegMarkers[i] = new EEGMarker(m.getStimulus(), m.getPosition());
            i++;
        }
        eegData.setMarkers(Arrays.asList(eegMarkers));*/
		return true;
	}
	
	
	private void setEEGMarkerStreams(Map<Integer, StreamHeader> headers) {
		for (Map.Entry<Integer, StreamHeader> entry : headers.entrySet()) {
			StreamHeader header = entry.getValue();
			if (header.getName().equals(eegStreamName)) {
				this.eegKey = entry.getKey();
			}
			if (header.getName().equals(eegStreamName)) {
				this.markerKey = entry.getKey();
			}
		}
	}
	
	public EEGDataPackage getEEGDataPackage() {
		return this.eegData;
	}

}
