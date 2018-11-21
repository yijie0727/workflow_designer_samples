package cz.zcu.kiv.eeg.basil.workflow.visualization;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.PlotlyGraphs.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@BlockType(type="EEGPlot",family="Visualization", runAsJar = true)
public class EEGPlotBlock implements Serializable {

    @BlockInput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList eegDataList;

    @BlockExecute
    public Graph process(){
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
        for(EEGDataPackage eegData:eegDataList.getEegDataPackage()){
            int channel=0;
            for(String name:eegData.getChannelNames()){
                Trace trace = new Trace();
                trace.setName(name);
                List<Point>points=new ArrayList<>();
                double data[]=eegData.getData()[channel];
                for(int i=0;i<data.length;i++){
                    Point point=new Point(new Coordinate((double)i,data[i]),"");
                    points.add(point);
                }
                trace.setPoints(points);
                traces.add(trace);
                channel++;
            }
        }
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
