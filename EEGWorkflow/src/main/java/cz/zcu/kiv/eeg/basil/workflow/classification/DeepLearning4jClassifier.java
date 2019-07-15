package cz.zcu.kiv.eeg.basil.workflow.classification;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import cz.zcu.kiv.eeg.basil.data.ClassificationStatistics;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.List;

/**
 * Created by Tomas Prokop on 15.01.2018.
 */
public abstract class DeepLearning4jClassifier implements IClassifier,Serializable {

    protected MultiLayerNetwork model;            //multi layer neural network with a logistic output layer and multiple hidden neuralNets
    protected int iterations;                    //Iterations used to classify

    /*Parametric constructor */
    public DeepLearning4jClassifier() {
    }

    // method for testing the classifier.
    @Override
    public ClassificationStatistics test(List<FeatureVector> featureVectors, List<Double> targets) {
        ClassificationStatistics resultsStats = new ClassificationStatistics(); // initialization of classifier statistics
        String marker = null;
        FeatureVector vector = null;
        for (int i = 0; i < featureVectors.size(); i++) {   //iterating epochs
            vector = featureVectors.get(i);
            double output = this.classify(vector);   //   output means score of a classifier from method classify
            if(vector.getMarkers() != null && vector.getMarkers().size() > 0){
                marker = vector.getMarkers().get(0).getName();
            }
            resultsStats.add(output, targets.get(i), marker);   // calculating statistics
        }
        return resultsStats;    //  returns classifier statistics
    }

    protected DataSet createDataSet(List<FeatureVector> featureVectors) {

        // Customizing params of classifier
        final int numRows = featureVectors.get(0).size();   // number of targets on a line
        final int numColumns = 2;   // number of labels needed for classifying

        double[][] labels = new double[featureVectors.size()][numColumns]; // Matrix of labels for classifier
        double[][] features_matrix = new double[featureVectors.size()][numRows]; // Matrix of features
        for (int i = 0; i < featureVectors.size(); i++) { // Iterating through epochs
            double[] features = featureVectors.get(i).getFeatureArray(); // Feature of each epoch
            for (int j = 0; j < numColumns; j++) {   //setting labels for each column
                labels[i][0] = featureVectors.get(i).getExpectedOutput(); // Setting label on position 0 as target
                labels[i][1] = Math.abs(1 - labels[i][0]);  // Setting label on position 1 to be different from label[0]
            }
            features_matrix[i] = features; // Saving features to features matrix
        }

        // Creating INDArrays and DataSet
        INDArray output_data = Nd4j.create(labels); // Create INDArray with labels(targets)
        INDArray input_data = Nd4j.create(features_matrix); // Create INDArray with features(data)
        DataSet dataSet = new DataSet(input_data, output_data); // Create dataSet with features and labels

        return dataSet;
    }

    protected DataSet createDataSet2(List<FeatureVector> featureVectors) {

        try {
            double[][] m = featureVectors.get(0).getFeatureMatrix();
            int[] shape = {featureVectors.size(), m.length, m[0].length};
            //DataSet 3d = Nd4j.create(shape);
            DataSet ds = new DataSet(Nd4j.create(featureVectors.get(0).getFeatureMatrix()[0].length), Nd4j.create(2));

            int i = 0;
            for (FeatureVector fv : featureVectors) {
                DataSet d;
                INDArray matrix = Nd4j.create(fv.getFeatureMatrix());
                double[] l = {fv.getExpectedOutput(),Math.abs(1 - fv.getExpectedOutput())};
                INDArray label = Nd4j.create(l);
                d = new DataSet(matrix, label);
                //ds.addFeatureVector(matrix, (int) fv.getExpectedOutput());
                if(ds.getFeatures() != null) {
                    //ds.addRow(d, i++);
                    ds.addFeatureVector(matrix, (int) fv.getExpectedOutput());
                }
                else {
                 ds.setFeatures(matrix);
                 ds.setLabels(label);
                }
            }

            return ds;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // method not implemented. For loading use load(String file)
    @Override
    public void load(InputStream is) {
        throw new NotImplementedException();
    }

    // method not implemented. For saving use method save(String file)
    @Override
    public void save(OutputStream dest) {
        throw new NotImplementedException();
    }

    /**
     * Save Model to zip file
     * using save methods from library deeplearning4j
     *
     * @param pathname path name and file name. File name should end with .zip extension.
     */
    public void save(String pathname) {
        File locationToSave = new File(pathname);      //Where to save the network. Note: the file is in .zip format - can be opened externally
        boolean saveUpdater = true;   //Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you want to train your network more in the future
        try {
            ModelSerializer.writeModel(model, locationToSave, saveUpdater);
            System.out.println("Saved network params " + model.params());
            System.out.println("Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads Model from file.
     * It uses load methods from library deepalerning4j
     *
     * @param pathname pathname and file name of loaded Model
     */
    public void load(String pathname) {
        File locationToLoad = new File(pathname);
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(locationToLoad);
            System.out.println("Loaded");
            System.out.println("Loaded network params " + model.params());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
