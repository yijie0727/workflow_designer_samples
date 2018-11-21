package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.WorkflowDesigner.Type;

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
@BlockType(type="EEGMarkerBlock",family = "Preprocessing", runAsJar = true)
public class EEGMarkerBlock implements Serializable {

    /**
     * Marker name
     */
    @BlockProperty(name="Name",type = Type.STRING)
    private String name;

    /**
     * Offset of marker in data
     */
    @BlockProperty(name="Offset",type=Type.NUMBER,defaultValue = "0")
    private int offset;

    @BlockOutput(name="marker",type="EEGMarker")
    EEGMarker eegMarker;

    public EEGMarkerBlock(){
        //Required Emtpy Default Constructor for Workflow Designer
    }

    @BlockExecute
    private void process(){
        eegMarker=new EEGMarker(name,offset);
    }

    /**
     * Creates new EEG marker
     * @param name marker name
     * @param offset marker offset
     */
    public EEGMarkerBlock(String name, int offset) {
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
