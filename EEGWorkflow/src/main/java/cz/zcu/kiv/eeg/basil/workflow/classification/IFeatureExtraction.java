package cz.zcu.kiv.eeg.basil.workflow.classification;

import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;

/**
 * 
 * Interface for extracting features from an epoch
 * 
 * @author Lukas Vareka
 *
 */
public interface IFeatureExtraction {
	
	/**
	 * Extracts features from given data
	 *
	 * @param data data
	 * @return array of features
	 */
    FeatureVector extractFeatures(EEGDataPackage data);

	/**
	 * Get feature vector dimension
	 * @return feature vector dimension
	 */
	int getFeatureDimension();
}
