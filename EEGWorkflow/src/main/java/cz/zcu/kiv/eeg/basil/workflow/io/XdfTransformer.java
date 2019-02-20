package cz.zcu.kiv.eeg.basil.workflow.io;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.IStreamData;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.StreamHeader;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfFileData;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;


/**
 * Converts XDF file data structure into
 * EEGDataPackage with EEG data and markers that 
 * fits into the Workflow designer.
 * 
 * @author Lukas Vareka
 *
 */
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
		this.eegKey = -1;
		this.markerKey = -1;
	}

	/**
	 * Converts the data by searching for EEG and marker streams and
	 * then adding them to the EEGDataPackage structure
	 * 
	 * @return conversion success and data are not empty
	 */
	public boolean convertData() {
		if (this.xdfData == null) {
			return false;
		}
			
		// find EEG stream
		// find marker stream
		setEEGMarkerStreams(this.xdfData.getStreamHeaders());
	
		// if no corresponding EEG data stream found
		if (this.xdfData.getStreamHeaders().get(eegKey) == null) {
			return false;
		}
		
		
		// set sampling rate		
		Configuration configuration = new Configuration();
        configuration.setSamplingInterval(this.xdfData.getStreamHeaders().get(eegKey).getSampling());
        eegData.setConfiguration(configuration);
        
        // set EEG data stream
        IStreamData eegStream 		  = this.xdfData.getData(eegKey);
        List<Float>[] streamData  = eegStream.getSamples();
        double startTimeStamp     = (double) eegStream.getTimeStamps().get(0);
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
			
		// only if markers are found
		if (streamMarkers != null && streamMarkers.getSamples() != null) {
			List<Integer>[] markers = streamMarkers.getSamples();
			List<Double> timeStamps = streamMarkers.getTimeStamps();
			EEGMarker[] eegMarkers = new EEGMarker[markers[0].size()];
			for (int i = 0; i < markers[0].size(); i++) {
				eegMarkers[i] = new EEGMarker("" + markers[0].get(i), (int) Math.round(timeStamps.get(i) - startTimeStamp));
			}
			eegData.setMarkers(Arrays.asList(eegMarkers));
		}
		return eegData.getData() != null && eegData.getData().length > 0;
	}
	
	
	/**
	 * 
	 * Finds IDs of EEG data and marker streams
	 * and stores them into the fields eegStreamName
	 * and markerStreamName
	 * 
	 * @param headers stream metadata 
	 */
	private void setEEGMarkerStreams(Map<Integer, StreamHeader> headers) {
		for (Map.Entry<Integer, StreamHeader> entry : headers.entrySet()) {
			StreamHeader header = entry.getValue();
			if (header.getName().equals(eegStreamName)) {
				this.eegKey = entry.getKey();
			}
			if (header.getName().equals(markerStreamName)) {
				this.markerKey = entry.getKey();
			}
		}
	}
	
	public EEGDataPackage getEEGDataPackage() {
		return this.eegData;
	}

}
