package cz.zcu.kiv.commons.GraphUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.PlotlyGraphs.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING;
import static cz.zcu.kiv.WorkflowDesigner.WorkflowCardinality.ONE_TO_ONE;

@BlockType(type ="LineEquation", family = "MATH")
public class LineEquation implements Serializable {

    @BlockInput(name = "IntRangeStart", type = NUMBER)
    public int op1=0;

    @BlockInput(name = "IntRangeEnd", type = NUMBER)
    public int op2=0;

    @BlockInput(name = "IntRangeIncrement", type = NUMBER)
    public int op3=0;

    @BlockProperty(name ="Expression", type = STRING ,defaultValue = "x")
    public String expression="";

    @BlockExecute
    public Graph process() throws IOException {
        if(op1>op2)throw new IOException("Operand1 should be less than Operand2");
        Graph graph = new Graph();
        List<Trace> traces=new ArrayList<>();
        Trace trace = new Trace();
        trace.setGraphType(GraphType.SCATTER);
        Marker marker=new Marker();
        marker.setSize(12.0);
        trace.setMarker(marker);

        List<Point>points=new ArrayList<>();
        for(int i=op1;i<op2;i+=op3){
            Expression e = new ExpressionBuilder(expression)
                    .variables("x")
                    .build()
                    .setVariable("x", i);
            double result = e.evaluate();
            points.add(new Point(new Coordinate((double) i,result)));
        }

        trace.setPoints(points);
        trace.setTraceMode(TraceMode.MARKERS_ONLY);
        traces.add(trace);
        graph.setTraces(traces);

        Layout layout = new Layout();
        layout.setTitle("y="+expression);

        Axis xAxis=new Axis();
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis=new Axis();
        yAxis.setMin(0);
        yAxis.setMax(100);

        layout.setXaxis(xAxis);
        layout.setYaxis(yAxis);

        graph.setLayout(layout);
        return graph;
    }

}
