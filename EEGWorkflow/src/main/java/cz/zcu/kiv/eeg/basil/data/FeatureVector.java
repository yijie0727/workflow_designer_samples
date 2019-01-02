package cz.zcu.kiv.eeg.basil.data;

import java.io.Serializable;
import java.util.List;

import cz.zcu.kiv.eeg.basil.utils.SignalProcessing;

/**
 * Represents a feature vector as
 * an input for classification
 * <p>
 * Created by Tomas Prokop on 07.08.2017.
 */
public class FeatureVector implements Serializable {
    private double[][] featureVector;
    private double expectedOutput;
    private List<EEGMarker> markers;	

    public FeatureVector() {
        this.featureVector = null;
        this.expectedOutput = 0;
    }

    public FeatureVector(double[][] featureVector) {
        this();
        this.featureVector = featureVector;
    }

    public FeatureVector(double[][] featureVector, double expectedOutput) {
        this.featureVector = featureVector;
        this.expectedOutput = expectedOutput;
    }

    public FeatureVector(double[] featureVector) {
        this();
        this.featureVector = new double[1][featureVector.length];
        this.featureVector[0] = featureVector;
    }

    public FeatureVector(double[] featureVector, double expectedOutput) {
        this.featureVector = new double[1][];
        this.featureVector[0] = featureVector;
        this.expectedOutput = expectedOutput;
    }

    public FeatureVector(double[] features, List<EEGMarker> markers) {
		this(features);
		this.markers = markers;
	}

	/**
     * Join two feature vectors
     *
     * @param features feature vector
     */
    public void addFeatures(double[][] features) {
        if (featureVector == null)
            featureVector = features;
        else {

            if (features.length != featureVector.length)
                throw new IllegalArgumentException("Dimension of given features does not match dimension of current feature vector");

            double[][] copy = new double[featureVector.length][featureVector[0].length + features[0].length];
            for (int i = 0; i < copy.length; i++) {
                System.arraycopy(featureVector[i], 0, copy, 0, featureVector[i].length);
                System.arraycopy(features[i], 0, copy, featureVector[i].length, features[i].length);
            }

            featureVector = copy;
        }
    }

    /**
     * Join two feature vectors
     *
     * @param features feature vector
     */
    public void addFeatures(double[] features) {
        if (featureVector == null) {
            this.featureVector = new double[1][];
            this.featureVector[0] = features;
        } else {
            if (features.length != 1)
                throw new IllegalArgumentException("Dimension of given features does not match dimension of current feature vector");

            double[][] copy = new double[1][featureVector[0].length + features.length];
            System.arraycopy(featureVector[0], 0, copy, 0, featureVector[0].length);
            System.arraycopy(features, 0, copy, featureVector[0].length, features.length);

            featureVector = copy;
        }
    }

    /**
     * Join two feature vectors
     *
     * @param features feature vector
     */
    public void addFeatures(FeatureVector features) {
        if (featureVector == null) {
            featureVector = features.getFeatureMatrix();
        } else {
            addFeatures(features.getFeatureMatrix());
        }
    }

    public double[][] getFeatureMatrix() {
        return featureVector;
    }

    public double[] getFeatureArray() {

        if (featureVector.length == 1)
            return featureVector[0];

        int rows = featureVector.length;
        int cols = featureVector[0].length;
        double[] ret = new double[rows * cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(featureVector[i], 0, ret, i * cols, cols);
        }

        return ret;
    }

    public void normalize() {
        this.featureVector = SignalProcessing.normalize(featureVector);
    }

    public int size() {
        return featureVector == null ? 0 : featureVector.length * featureVector[0].length;
    }

    public double getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(double expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

	public List<EEGMarker> getMarkers() {
		return markers;
	}
}
