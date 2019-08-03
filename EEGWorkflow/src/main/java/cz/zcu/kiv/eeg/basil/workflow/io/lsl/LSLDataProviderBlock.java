package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataMessage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import java.util.Arrays;




/**
 * Provides EEG data with markers using
 * two threads that collect both data streams 
 * and subsequently join the information together.
 * Uses LabStreamingLayer interface for collecting data. 
 * 
 * @author Lukas Vareka
 *
 */
@BlockType(type="LSLDataProvider", family = "DataProvider", runAsJar = true)
public class LSLDataProviderBlock implements Serializable  {
	
	
	
	@BlockProperty(name = "Buffer size", type = Type.NUMBER)
	private int buffer_size = 5000; /* size of a single data block before  transferring to observers */

//	@BlockOutput(name = "EEGData", type = "EEGDataList")
//	private EEGDataPackageList eegDataPackageList;

	@BlockOutput(name = "EEGData", type = "EEGDataPipeStream")
	private PipedOutputStream eegPipeOut = new PipedOutputStream();

	ObjectOutputStream eegObjectOut;


	private volatile double[][] data; /* data  - BLOCK */
	private volatile List<EEGMarker> markers; /* stimuli markers */ 
	private LSLEEGCollector eegCollector;   /* provider for EEG data */
	private LSLMarkerCollector markerCollector;  /* provider for markers */
	private int dataPointer; /* points at the current EEG data sample in an array */ 
	private int blockCounter; /* counter for blocks that have been sent to observers */
	private List<EEGDataPackage> eegDataList; 
	
	public LSLDataProviderBlock() {
		this.data           = null;
		this.markers        = new ArrayList<EEGMarker>();
		this.dataPointer    = 0;
		this.blockCounter   = 0;
		this.eegCollector    = new LSLEEGCollector(this);
		this.markerCollector = new LSLMarkerCollector(this);
	}
	
	
	@BlockExecute
	public void process() throws IOException, InterruptedException {

		eegObjectOut = new ObjectOutputStream(eegPipeOut);

		this.eegDataList = new ArrayList<>();
		this.eegCollector.start();
		this.markerCollector.start();
		
		this.eegCollector.join();
		this.markerCollector.join();
		//eegDataPackageList = new EEGDataPackageList(eegDataList);


		eegObjectOut.writeObject(null);
		eegObjectOut.flush();

		eegObjectOut.close();
		eegPipeOut.close();
	}

		
	public void stop() {
		this.eegCollector.terminate();
		this.markerCollector.terminate();
	}

    /**
	 * One EEG sample (from all channels has) has been received
	 * -> update
	 * 
	 * @param eegSample
     * @param configuration 
	 */
	public synchronized void addEEGSample(float[] eegSample, Configuration configuration) throws  IOException {
		if (data == null) {
			data = new double[eegSample.length][buffer_size];
		}
		
		// fill data array using the obtained sample
		for (int i = 0; i < eegSample.length; i++) {
			data[i][dataPointer] = eegSample[i];
		}
		dataPointer++;
		
		/* if maximum size is reached, transfer the data */
		if (dataPointer == buffer_size) {
			EEGDataPackage dataPackage = new EEGDataPackage(data, markers, null, configuration);
			//this.eegDataList.add(dataPackage);


			eegObjectOut.writeObject(dataPackage);
			eegObjectOut.flush();



			this.stop();
		}
	}

	/**
	 * Marker has been received, update the corresponding list
	 * 
	 * @param marker
	 */
	public synchronized void addMarker(String[] marker) {
		EEGMarker newMarker = new EEGMarker(marker[0], dataPointer);
		this.markers.add(newMarker);
	}


//	public EEGDataPackageList getEegDataPackageList() {
//		return eegDataPackageList;
//	}


	

	
	
}
