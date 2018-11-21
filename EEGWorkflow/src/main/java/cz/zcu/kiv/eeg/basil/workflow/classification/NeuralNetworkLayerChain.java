package cz.zcu.kiv.eeg.basil.workflow.classification;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.STRING;
import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

@BlockType(type="NeuralNetworkLayer", family = "classification", runAsJar = true)
public class NeuralNetworkLayerChain implements Serializable {
    static Log log=LogFactory.getLog(NeuralNetworkLayerChain.class);

    @BlockInput(name ="LayerChain",type="NeuralNetworkLayerChain")
    @BlockOutput(name ="LayerChain",type="NeuralNetworkLayerChain")
    NeuralNetworkLayerChain neuralNetworkLayerChain;

    List<Layer> layerArraylist;

    @BlockProperty(name="NumberOfInputs", type=NUMBER)
    Integer nIn;

    @BlockProperty(name="NumberOfOutputs", type=NUMBER)
    Integer nOut;

    @BlockProperty(name="LayerType", type=STRING)
    String layerType;

    @BlockProperty(name="DropOut", type=NUMBER)
    Double dropOut;

    @BlockProperty(name="ActivationFunction", type=STRING)
    String activationFunction;

    @BlockProperty(name="LossFunction", type=STRING)
    String lossFunction;

    @BlockExecute
    void process(){
        if(neuralNetworkLayerChain==null){
            neuralNetworkLayerChain=this;
        }
        if(neuralNetworkLayerChain.layerArraylist==null){
            neuralNetworkLayerChain.layerArraylist=new ArrayList<>();
        }
        Layer layer;
        switch (layerType){
            case "output" : layer=
                    new OutputLayer.Builder()
                            .nIn(nIn)
                            .nOut(nOut)
                           // .dropOut(dropOut)
                            .activation(parseActivation(activationFunction))
                            .lossFunction(parseLossFunction(lossFunction))
                            .build();
                    break;
            case "dense" : layer=
                    new DenseLayer.Builder()
                            .nIn(nIn)
                            .nOut(nOut)
                          //  .dropOut(dropOut)
                            .activation(parseActivation(activationFunction))
                            .build();
                break;
            case "auto_encoder" : layer=
                    new AutoEncoder.Builder()
                            .nIn(nIn)
                            .nOut(nOut)
                          //  .dropOut(dropOut)
                            .activation(parseActivation(activationFunction))
                            .build();
                break;
//            case "rbm" : layer=
//                    new RBM.Builder()
//                            .nIn(nIn)
//                            .nOut(nOut)
//                            .dropOut(dropOut)
//                            .activation(parseActivation(activationFunction))
//                            .build();
//                break;
            case "graves_lstm" : layer=
                    new GravesLSTM.Builder()
                            .nIn(nIn)
                            .nOut(nOut)
                            .dropOut(dropOut)
                            .activation(parseActivation(activationFunction))
                            .build();
                break;
            default:
                log.fatal("Unsupported layer type");
                layer=null;

        }
        if(layer!=null)
            neuralNetworkLayerChain.layerArraylist.add(layer);

    }

    private WeightInit parseWeightInit(String command){
    	WeightInit weightInit = null;
    	try {
    		weightInit = WeightInit.valueOf(command);
    	} catch (IllegalArgumentException|NullPointerException e) {
    		weightInit =  WeightInit.RELU;
    	}
        return weightInit;
    }

    private Updater parseUpdater(String command){
    	Updater updater;
    	try {
    		updater = Updater.valueOf(command);
    	} catch (IllegalArgumentException|NullPointerException e) {
    		updater = Updater.NESTEROVS;
    	}
		return updater;
	}

    private OptimizationAlgorithm parseOptimizationAlgo(String command){
    	OptimizationAlgorithm optimizationAlgorithm = null;
    	try {
    		optimizationAlgorithm = OptimizationAlgorithm.valueOf(command.toUpperCase());
    	} catch (IllegalArgumentException|NullPointerException e) {
    		optimizationAlgorithm = OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT; // default
    	}
    	return optimizationAlgorithm;
    }

    public static LossFunctions.LossFunction parseLossFunction(String command){
    	LossFunctions.LossFunction lossFunction;
    	try {
    		lossFunction = LossFunctions.LossFunction.valueOf(command.toUpperCase());
    	} catch (IllegalArgumentException|NullPointerException e) {
    		lossFunction = LossFunctions.LossFunction.MSE; // default
    	}
       return lossFunction;
    }


    public static Activation parseActivation(String command){
    	Activation activation  = null;
    	try {
    		activation = Activation.valueOf(command.toUpperCase());
    	} catch (IllegalArgumentException|NullPointerException e) {
    		activation = Activation.SIGMOID; // default
    	}
    	return activation;
    }

    public List<Layer> getLayerArraylist() {
        return layerArraylist;
    }

}
