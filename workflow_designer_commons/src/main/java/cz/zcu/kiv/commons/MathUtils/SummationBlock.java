package cz.zcu.kiv.commons.MathUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.Serializable;
import java.util.List;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER_ARRAY;
import static cz.zcu.kiv.WorkflowDesigner.WorkflowCardinality.ONE_TO_MANY;

@BlockType(type ="SUMMATION", family = "MATH")
public class SummationBlock implements Serializable {

    @BlockInput(name = "Operand1", type = NUMBER_ARRAY)
    private List<Integer> op;

    @BlockExecute
    public String process(){
        int sum=0;
        for(int i=0;i<op.size();i++){
            sum+=op.get(i);
        }
        return String.valueOf(sum);
    }
}
