package cz.zcu.kiv.eeg.basil.workflow.classification;

import java.io.*;
import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

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

	@BlockInput (name = "FeatureVectors", type = STREAM)
	private PipedInputStream featureVecPipeIn = new PipedInputStream();

	@BlockOutput(name = "FeatureVectors", type = STREAM)
	private PipedOutputStream featureVecPipeOut = new PipedOutputStream();

	@BlockExecute
	public void process() throws IOException, ClassNotFoundException {
		ITrainCondition trainCondition = new ErpTrainCondition();

		ObjectInputStream  featureVecObjectIn  = new ObjectInputStream(featureVecPipeIn);
		ObjectOutputStream featureVecObjectOut = new ObjectOutputStream(featureVecPipeOut);
// System.out.println("10 before while");

		FeatureVector featureVector;
		while ((featureVector = (FeatureVector) featureVecObjectIn.readObject())!= null) {
//System.out.println("10 in while");
			String currentMarker;
			if (featureVector == null || featureVector.getMarkers() == null || featureVector.getMarkers().get(0) == null)
				continue;
			currentMarker = featureVector.getMarkers().get(0).getName();

			for( EEGMarker markerBlock: targetMarkers) {
				trainCondition.addSample(featureVector, markerBlock.getName(), currentMarker);
//System.out.println("10 send ");
				// finally, send feature vectors with labeled ones
				featureVecObjectOut.writeObject(featureVector);
				featureVecObjectOut.flush();
			}


		}

		featureVecObjectOut.writeObject(null);
		featureVecObjectOut.flush();

		featureVecObjectIn.close();
		featureVecObjectOut.close();
		featureVecPipeIn.close();
		featureVecPipeOut.close();
	}

}
