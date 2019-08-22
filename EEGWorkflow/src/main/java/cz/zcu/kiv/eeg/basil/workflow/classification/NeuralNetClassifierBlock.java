package cz.zcu.kiv.eeg.basil.workflow.classification;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.Table;
import cz.zcu.kiv.eeg.basil.data.ClassificationStatistics;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.data.FeatureVector;

import org.deeplearning4j.eval.Evaluation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

/**
 * Average a list of trainingEEGData using one stimuli marker
 * @author lvareka
 *
 */
@BlockType(type="NeuralNetClassifier",family = "Classification", runAsJar = true)
public class NeuralNetClassifierBlock implements Serializable {

	@BlockInput(name = "Markers",type="EEGMarker[]")
	private List<EEGMarker> markers;

	@BlockInput (name = "TrainingFeatureVectors", type = STREAM)
    private PipedInputStream trainingPipeIn = new PipedInputStream();

    @BlockInput(name = "TestingFeatureVectors", type = STREAM)
    private PipedInputStream testingPipeIn  = new PipedInputStream();

    private List<FeatureVector> trainingEEGData;

    private List<FeatureVector> testingEEGData;

    @BlockInput(name="Layers", type="NeuralNetworkLayerChain")
    private NeuralNetworkLayerChain layerChain;

    @BlockOutput(name="ClassificationResult", type ="ClassificationStatistics")
    private ClassificationStatistics statistics;

	public NeuralNetClassifierBlock(){
		//Required Empty Default Constructor for Workflow Designer
	}


	public class StreamReader implements Runnable {
	    private ObjectInputStream objectIn;
        private List<FeatureVector> EEGDataList;
        private String s;

        public StreamReader(ObjectInputStream objectIn, List<FeatureVector> EEGDataList, String s) {
            this.objectIn = objectIn;
            this.EEGDataList = EEGDataList;
            this.s = s;
        }

        @Override
        public void run() {
            FeatureVector fv = null;

            try {
                while ((fv = (FeatureVector) objectIn.readObject()) != null) {
                    EEGDataList.add(fv);
                    System.out.println(s);
                }
            } catch (ClassNotFoundException | IOException e) {
                System.err.println("Failed to read feature vectors from stream: " +  e.getMessage());
            }

        }
    }


	@BlockExecute
    public Object process() throws Exception{

        trainingEEGData = new ArrayList<>();
        testingEEGData  = new ArrayList<>();
        ObjectInputStream  trainObjectIn  = new ObjectInputStream(trainingPipeIn);
        ObjectInputStream  testObjectIn   = new ObjectInputStream(testingPipeIn);

        System.out.println("before");
        Thread readTest  = new Thread(new StreamReader(testObjectIn,   testingEEGData,  "receive testVector" ));
        Thread readTrain = new Thread(new StreamReader(trainObjectIn,  trainingEEGData, "receive trainVector"));
        readTest.start();
        readTrain.start();

        readTest.join();
        readTrain.join();


        System.out.println("after");

        trainObjectIn.close();
        testObjectIn.close();
        testingPipeIn.close();
        trainingPipeIn.close();

        SDADeepLearning4jClassifier classification = new SDADeepLearning4jClassifier(layerChain.layerArraylist);
        Evaluation eval = classification.train(trainingEEGData, 10);
        if(testingEEGData != null && testingEEGData.size() != 0) {
        	// collect expected labels
        	List<Double> expectedLabels = new ArrayList<Double>();
        	for (FeatureVector featureVector: testingEEGData) {
        		expectedLabels.add(featureVector.getExpectedOutput());
        	}
            statistics = classification.test(testingEEGData, expectedLabels);
        	System.out.println(statistics.toString());
            return statistics.toString();

        }

        Table table = new Table();
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("Precision",String.valueOf(eval.precision())));
        rows.add(Arrays.asList("Recall",String.valueOf(eval.recall())));
        rows.add(Arrays.asList("Accuracy",String.valueOf(eval.accuracy())));
        rows.add(Arrays.asList("F1 Score",String.valueOf(eval.f1())));

        //Confusion Matrix
        rows.add(Arrays.asList("True +ve",String.valueOf(eval.getTruePositives().totalCount())));
        rows.add(Arrays.asList("True -ve",String.valueOf(eval.getTrueNegatives().totalCount())));
        rows.add(Arrays.asList("False +ve",String.valueOf(eval.getFalsePositives().totalCount())));
        rows.add(Arrays.asList("False -ve",String.valueOf(eval.getFalseNegatives().totalCount())));
        table.setRows(rows);
        table.setCaption("Testing Dataset Results");

        return table;
    }


    public List<EEGMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<EEGMarker> markers) {
        this.markers = markers;
    }

    public void setTrainingEEGData(List<FeatureVector> trainingEEGData) {
        this.trainingEEGData = trainingEEGData;
    }
}
