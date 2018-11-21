package cz.zcu.kiv.eeg.basil.workflow.classification;

import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;

/**
 * 
 * Labels feature vectors under assumption that the markers 
 * are references in the feature vector object structure. 
 * Feature vectors associated with "targetMarkers" are labeled as 1, 0 otherwise.
 * The numbers of examples in both output classes are equalized.
 * 
 * @author Lukas Vareka
 *
 */
@BlockType(type="FeatureLabelingBlock", family = "FeatureExtraction", runAsJar = true)
public class FeatureLabeling {
	
	@BlockInput(name = "Markers",type="EEGMarker[]")
	private List<EEGMarker> targetMarkers; 
	
	
	@BlockInput(name = "FeatureVectors", type = "List<FeatureVector>")
	@BlockOutput(name = "FeatureVectors", type = "List<FeatureVector>")
	private List<FeatureVector> featureVectors;
	
	
	@BlockExecute
	public void process() {
		ITrainCondition trainCondition = new ErpTrainCondition();
		for (FeatureVector featureVector : featureVectors) {

			String currentMarker;
			if (featureVector == null || featureVector.getMarkers() == null || featureVector.getMarkers().get(0) == null)
				continue;
			currentMarker = featureVector.getMarkers().get(0).getName();
		
			for( EEGMarker markerBlock: targetMarkers) {
                trainCondition.addSample(featureVector, markerBlock.getName(), currentMarker);
            }
		}
		// finally, replace feature vectors with labeled ones
		this.featureVectors = trainCondition.getFeatureVectors();
	}

}
