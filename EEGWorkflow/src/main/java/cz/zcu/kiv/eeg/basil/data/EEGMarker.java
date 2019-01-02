package cz.zcu.kiv.eeg.basil.data;


import java.io.Serializable;

/**
 * Created by Tomas Prokop on 17.07.2017.
 * 
 * Represents one EEG event marker that is defined by
 * name (such as 'S  1', offset in samples relative to the beginning
 * of the data in a package / buffer) and isTarget that 
 * can be used to evaluate the class label e.g. in P300
 * experiments
 * 
 */
public class EEGMarker implements Serializable {

    /**
     * Marker name
     */
    private String name;

    /**
     * Offset of marker in data
     */
    private int offset;

    EEGMarker eegMarker;

    public EEGMarker(){
        //Required Emtpy Default Constructor for Workflow Designer
    }

    private void process(){
        eegMarker=this;
    }

    /**
     * Creates new EEG marker
     * @param name marker name
     * @param offset marker offset
     */
    public EEGMarker(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    /**
     * Get marker name
     * @return marker name
     */
    public String getName() {
        return name;
    }

    /**
     * Get marker offset
     * @return offset
     */
    public int getOffset() {
        return offset;
    }
    
    @Override
    public String toString() {
    	return name + ", offset: " + offset;
    }

    /**
     * Increments marker offset
     * @param length Offset is increase by given length
     */
	public void incrementOffset(int length) {
		this.offset += length;
	}

    /**
     * Decrement marker offset
     * @param length Offset is descreased by given length
     */
	public void decrementOffset(int length) {
		this.offset = this.offset - length;
	}
}
