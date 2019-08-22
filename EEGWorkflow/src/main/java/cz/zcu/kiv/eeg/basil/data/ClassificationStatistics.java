package cz.zcu.kiv.eeg.basil.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Holds results of classification if the target data with expected classes is
 * provided. Can calculate accuracy.
 *
 * @author Lukas Vareka
 *
 */
public class ClassificationStatistics implements Serializable {

    private int truePositives;
    private int trueNegatives;
    private int falsePositives;
    private int falseNegatives;
    private double MSE;

    // only for testing
    private double class1sum;
    private double class2sum;

    private ClassificationResults results;

    public ClassificationStatistics() {
        this.truePositives = 0;
        this.trueNegatives = 0;
        this.falsePositives = 0;
        this.falseNegatives = 0;
        this.MSE = 0;

        this.class1sum = 0;
        this.class2sum = 0;

        results = new ClassificationResults();
    }

    public ClassificationStatistics(int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {
        this.truePositives = truePositives;
        this.trueNegatives = trueNegatives;
        this.falsePositives = falsePositives;
        this.falseNegatives = falseNegatives;
        this.MSE = 0;
        this.class1sum = 0;
        this.class2sum = 0;
    }

    public double calcAccuracy() {
        return ((double) truePositives + trueNegatives) / getNumberOfPatterns();
    }

    public int getNumberOfPatterns() {
        return (truePositives + trueNegatives + falsePositives + falseNegatives);
    }

    public void add(double realOutput, double expectedOutput, String marker) {
        int res = 0;
        this.MSE += Math.pow(expectedOutput - realOutput, 2);
        if (Math.round(expectedOutput) == 0 && Math.round(expectedOutput) == Math.round(realOutput)) {
            this.trueNegatives++;
            class1sum += realOutput;
        } else if (Math.round(expectedOutput) == 0 && Math.round(expectedOutput) != Math.round(realOutput)) {
            this.falsePositives++;
            class1sum += realOutput;
        } else if (Math.round(expectedOutput) == 1 && Math.round(expectedOutput) == Math.round(realOutput)) {
            res = 1;
            this.truePositives++;
            class2sum += realOutput;
        } else if (Math.round(expectedOutput) == 1 && Math.round(expectedOutput) != Math.round(realOutput)) {
            this.falseNegatives++;
            class2sum += realOutput;
        }

        results.Add(expectedOutput, realOutput, marker, res);
    }

    public ClassificationResults getResults(){
        return results;
    }

    @Override
    public String toString() {
        return "Number of patterns: " + getNumberOfPatterns() + "\n"
                + "True positives: " + this.truePositives + "\n"
                + "True negatives: " + this.trueNegatives + "\n"
                + "False positives: " + this.falsePositives + "\n"
                + "False negatives: " + this.falseNegatives + "\n"
                + "Accuracy: " + calcAccuracy() * 100 + "%\n"
                + "MSE: " + this.MSE / getNumberOfPatterns() + "\n"
                + "Non-targets: " + class1sum + "\n"
                + "Targets: " + class2sum + "\n";
    }

}
