package cz.zcu.kiv.commons.MathUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.Serializable;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;

@BlockType(type ="DelayedConstant", family = "MATH", description = "Provides a constant after a time delay")
public class DelayedConstant implements Serializable {


    @BlockProperty(name = "Value", type = NUMBER, defaultValue = "0")
    private int val=7;

    @BlockProperty(name = "Delay", type = NUMBER, defaultValue = "0", description = "Time Delay in Milliseconds")
    private int delay=0;

    @BlockOutput(name = "Operand", type = NUMBER)
    private int op=0;

    @BlockExecute
    public void process(){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        op=val;
    }
}
