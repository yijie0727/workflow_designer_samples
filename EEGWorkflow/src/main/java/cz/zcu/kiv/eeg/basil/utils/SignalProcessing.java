package cz.zcu.kiv.eeg.basil.utils;

public class SignalProcessing {

    /**
     * Decimates input signal
     * @param inputSignal signal
     * @param factor decimation factor
     * @return decimated signal
     */
    public static double[] decimate(double[] inputSignal, int factor) {
        int decimatedLength = inputSignal.length / factor;
        double[] output = new double[decimatedLength];
        for (int i = 0; i < decimatedLength; i++) {
            output[i] = inputSignal[i * factor];
        }
        return output;
    }

    /**
     * Normalizes feature vector
     * @param features feature vector
     * @return normalized features
     */
    public static double[] normalize(double[] features) {
        double size =  getSizeOfVector(features);
        for (int i = 0; i < features.length; i++) {
            features[i] = features[i] / size;
        }
        return features;
    }

    /**
     * Normalizes feature vector
     * @param features feature vector
     * @return normalized features
     */
    public static double[][] normalize(double[][] features) {

        for (int i = 0; i < features.length; i++) {
            double size =  getSizeOfVector(features[i]);
            for (int j = 0; j < features.length; j++) {
                features[i][j] = features[i][j] / size;
            }
        }
        return features;
    }

    private static double getSizeOfVector(double[] features) {
        double size = 0;
        for (int i = 0; i < features.length; i++) {
            size += Math.pow(features[i], 2);
        }
        return Math.sqrt(size);
    }
    
    /**
     * 
     * Finds the averaged value from a selected part of the signal
     * 
     * @param inputSignal
     * @param start index in the signal to start averaging
     * @param end   index in the signal to stop averaging
     * @return average value in the selected part of the signal
     */
    public static double average(double[] inputSignal, int start, int end) {
    	double sum = 0;
    	for (int i = start; i <= end && i < inputSignal.length; i++) {
    		sum += inputSignal[i];
    	}
    	return sum / (end - start + 1);
    }
}
