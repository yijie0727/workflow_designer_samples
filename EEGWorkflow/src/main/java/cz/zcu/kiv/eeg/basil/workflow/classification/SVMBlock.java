package cz.zcu.kiv.eeg.basil.workflow.classification;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.ClassificationStatistics;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Prokop on 20.05.2019.
 */
@BlockType(type="SVM Classifier",family = "Classification", runAsJar = true)
public class SVMBlock implements Serializable {

    @BlockInput(name = "Markers",type="EEGMarker[]")
    private List<EEGMarker> markers;

    @BlockInput(name = "TrainingFeatureVectors", type = "List<FeatureVector>")
    private List<FeatureVector> trainingEEGData;

    @BlockInput(name = "TestingFeatureVectors", type = "List<FeatureVector>")
    private List<FeatureVector> testingEEGData;

    private LibSVM classifier;

    /**
     * dataset for WEKA instances in format ".arfff"
     */
    private Instances instances;

    @BlockExecute
    public Object process(){
        classifier = createClassifier();
        createDataset(trainingEEGData);

        if(testingEEGData != null) {
            createDataset(testingEEGData);

            // collect expected labels
            List<Double> expectedLabels = new ArrayList<Double>();
            for (FeatureVector featureVector: testingEEGData) {
                expectedLabels.add(featureVector.getExpectedOutput());
            }
            ClassificationStatistics statistics = test(testingEEGData, expectedLabels);
            return statistics.toString();
        }

        return null;
    }

    private LibSVM createClassifier(){
        LibSVM classifier = new LibSVM();
        String[] options;
        try {
            options = weka.core.Utils.splitOptions("-S 0 -K 0 -C 425 -M 40.0 -W 1 -seed 1");
            classifier.setOptions(options);
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong input value! Check that input values are correct.");
        }

        return  classifier;
    }

    public void train(List<FeatureVector> featureVectors, int numberOfiter) {
        /* creating of special dataset for WEKA classificator*/
        createDataset(featureVectors);
        try {
            classifier.buildClassifier(instances);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fault during creating of dataset!");
        }
    }

    public double classify(FeatureVector fv) {
        double[] feature = fv.getFeatureArray();
        Instance instance = new Instance(1, feature);
        instances.add(instance);
        instances.setClassIndex(instances.numAttributes() - 1);
        Object predictedClassValue;
        try {
            predictedClassValue = this.classifier.classifyInstance(instances.lastInstance());
        } catch (Exception e) {
            throw new IllegalArgumentException("Fault during classifying one of the epoch! Try to check dataset.");
        }
        return (Double) predictedClassValue;
    }

    public ClassificationStatistics test(List<FeatureVector> featureVectors, List<Double> targets) {
        ClassificationStatistics resultsStats = new ClassificationStatistics();
        for (int i = 0; i < instances.numInstances() - 1; i++) {
            Object predictedClassValue;
            try {
                predictedClassValue = classifier.classifyInstance(instances
                        .instance(i));
            } catch (Exception e) {
                throw new IllegalArgumentException("Fault during classifying one of the epoch! Try to check dataset.");
            }
            Object realClassValue = instances.instance(i).classValue();
            System.out.println(instances.instance(i).classValue());
            resultsStats.add((Double) realClassValue,
                    (Double) predictedClassValue);
        }

        return resultsStats;
    }

    /**
     * Method for creating arff dataset file used for training LibSVM classifier.
     * ARFF FORMAT
     *
     * @ name_of_attribute_1 type_of_attribute (f.e. numeric)
     * @ name_of_attribute_2 type_of_attribute (f.e. numeric)
     * @ name_of_attribute_3 type_of_attribute (f.e. {0,1})
     *
     * @ DATA
     * 2.1,3.5,1
     *
     * @param featureVectors list of feature vectors
     */
    private void createDataset(List<FeatureVector> featureVectors) {
        FastVector attributes;
        Instances helpDataset;
        double[] values;
        attributes = new FastVector();

        FeatureVector first = featureVectors.get(0);
        Instance firstInstance = new Instance(first.getExpectedOutput(), first.getFeatureArray());
        int numValues = firstInstance.numValues();
        /* creating fields of attribute of dataset */
        for (int i = 0; i < firstInstance.numValues(); i++) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }

        attributes.addElement(new Attribute("target"));
        helpDataset = new Instances("ESDN", attributes, 0);

        int j = 0;
        /* creating the rest of dataset */
        for (FeatureVector fv : featureVectors) {
            double[] features = fv.getFeatureArray();
            values = new double[helpDataset.numAttributes()];
            System.arraycopy(features, 0, values, 0, numValues);
            values[numValues] = fv.getExpectedOutput();
            j++;
            helpDataset.add(new Instance(1.0, values));
        }
        instances = helpDataset;
        /* dataset needs class variable in nominal format - last (class variable) is convert from numeric to nominal */
        NumericToNominal convert = new NumericToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "last";
        try {
            convert.setOptions(options);
            convert.setInputFormat(instances);
            instances = Filter.useFilter(instances, convert);
        } catch (Exception e) {
            throw new IllegalArgumentException("Converting of numeric class index failed!");
        }
        instances.setClassIndex(numValues);
    }
}
