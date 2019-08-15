package cz.zcu.kiv.eeg.basil.workflow.visualization;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.PlotlyGraphs.*;
import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;

import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type="EEGPlot",family="Visualization", runAsJar = true)
public class EEGPlotBlock implements Serializable {

    @BlockInput(name = "EEGData", type = STREAM)
    private PipedInputStream eegPipeIn  = new PipedInputStream();

    private EEGDataPackageList eegDataList;

    @BlockExecute
    public Graph process() throws Exception {
        Graph graph=new Graph();
        Layout layout=new Layout();
        layout.setTitle("EEG signal visualization");
        Axis xAxis=new Axis();
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis=new Axis();
        yAxis.setMin(0);
        yAxis.setMax(100);
        layout.setXaxis(xAxis);
        layout.setYaxis(yAxis);
        graph.setLayout(layout);
        List<Trace> traces=new ArrayList<>();

        ObjectInputStream eegObjectIn  = new ObjectInputStream(eegPipeIn);
        EEGDataPackage eegData;
        ArrayList<EEGDataPackage> list = new ArrayList<>();
        while ((eegData = (EEGDataPackage) eegObjectIn.readObject())!= null) {
            list.add(eegData);

            Configuration cfg = eegData.getConfiguration();
            int startSample =  (int)((0.001 * cfg.getPreStimulus()) /* time in s */ * cfg.getSamplingInterval());
            for (int i = 0; i < eegData.getData().length; i++) {
                Trace trace = new Trace();
                
                if (eegData.getChannelNames() == null)
                	trace.setName("Channel: " + i);
                else
                	trace.setName(eegData.getChannelNames()[i]);
                
                
                List<Point>points = new ArrayList<>();
                double data[] = eegData.getData()[i];
                for (int j = 0; j < data.length; j++){
                    Point point = new Point(new Coordinate((double)j + startSample, data[j]), "");
                    points.add(point);
                }
                trace.setPoints(points);
                traces.add(trace);
                
            }
        }
        eegDataList = new EEGDataPackageList(list);

        eegObjectIn.close();
        eegPipeIn.close();

        graph.setTraces(traces);
        return graph;
    }

    public EEGDataPackageList getEegDataList() {
        return eegDataList;
    }

    public void setEegDataList(EEGDataPackageList eegDataList) {
        this.eegDataList = eegDataList;
    }
}
