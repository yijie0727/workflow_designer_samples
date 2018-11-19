package cz.zcu.kiv.commons.GraphUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.PlotlyGraphs.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING;
import static cz.zcu.kiv.WorkflowDesigner.WorkflowCardinality.ONE_TO_ONE;

@BlockType(type ="CSVto2dPlot", family = "MATH")
public class CSVto2dPlot implements Serializable {

    @BlockProperty(name="HasHeaders", type=Type.BOOLEAN , defaultValue = "")
    private Boolean hasHeaders=false;

    @BlockProperty(name ="CsvFile", type = Type.FILE ,defaultValue = "")
    private File csvFile;

    public static Graph graph;

    @BlockExecute
    public Graph process() throws IOException {
        Graph graph = new Graph();
        List<Trace> traces=new ArrayList<>();
        Trace trace = new Trace();
        trace.setGraphType(GraphType.SCATTER);
        Marker marker=new Marker();
        marker.setSize(12.0);
        trace.setMarker(marker);

        List<Point>points=new ArrayList<>();
        String csv=FileUtils.readFileToString(csvFile,Charset.defaultCharset());

        String rows[]=csv.split("\\n");

        for(int i=hasHeaders?1:0;i<rows.length;i++){
            String columns[]=rows[i].split(",");
            Double x=Double.parseDouble(columns[0]);
            Double y=Double.parseDouble(columns[1]);
            points.add(new Point(new Coordinate(x,y)));
        }

        trace.setPoints(points);
        trace.setTraceMode(TraceMode.MARKERS_ONLY);
        traces.add(trace);
        graph.setTraces(traces);

        Layout layout = new Layout();
        layout.setTitle(csvFile.getName());

        Axis xAxis=new Axis();
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis=new Axis();
        yAxis.setMin(0);
        yAxis.setMax(100);

        layout.setXaxis(xAxis);
        layout.setYaxis(yAxis);

        graph.setLayout(layout);
        this.graph=graph;
        return graph;
    }

}
