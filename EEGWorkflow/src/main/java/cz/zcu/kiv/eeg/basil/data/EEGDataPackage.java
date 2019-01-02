package cz.zcu.kiv.eeg.basil.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class represents EEG data. It remembers data processing steps performed on the data it carries.
 *
 * Created by Tomas Prokop on 04.07.2017.
 */
public class EEGDataPackage implements Serializable {

    private Configuration configuration;

    private String[] channelNames;

    /**
     * EEG data
     */
    private double[][] data;
    
    private List<EEGMarker> markers;

    private int outputClass;

    private double classificationResult;

    public EEGDataPackage(){
    }

    public EEGDataPackage(double[][] data, List<EEGMarker> markers, String[] channelNames,Configuration configuration) {
        this.data=data;
        this.markers=markers;
        this.channelNames=channelNames;
        this.configuration=configuration;
    }

    /**
     * Returns data
     * @return data
     */
    public double[][] getData() {
        return data;
    }


    public List<EEGMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<EEGMarker> markers) {
        this.markers = markers;
    }

    public void addMarker(EEGMarker marker){
        this.markers.add(marker);
    }

    public void setData(double[][] data) {
        this.data = data;
    }



	public String[] getChannelNames() {
		return channelNames;
	}

	public void setChannelNames(String[] channelNames) {
		this.channelNames = channelNames;
	}

    public int getOutputClass() {
        return outputClass;
    }

    public void setOutputClass(int outputClass) {
        this.outputClass = outputClass;
    }

    public double getClassificationResult() {
        return classificationResult;
    }

    public void setClassificationResult(double classificationResult) {
        this.classificationResult = classificationResult;
    }
    
    @Override
    public String toString() {
    	StringBuilder returnString = new StringBuilder();
    	boolean channelsOK = true;
    	if (channelNames == null || channelNames.length != data.length) {
    		returnString = new StringBuilder("ChannelNames missing or its size and data size are different!\n");
    		channelsOK = false;
       	}
    	
    	for (int i = 0; i < data.length; i++) {
    		returnString.append(channelsOK ? channelNames[i] : "?" + ": " + Arrays.toString(data[i]) + "\n");
    	}
    	return returnString.toString();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
