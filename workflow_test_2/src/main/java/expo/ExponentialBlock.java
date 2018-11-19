package expo;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;


import java.io.Serializable;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;


@BlockType(type ="ExponentialBlock", family = "MATH")
public class ExponentialBlock implements Serializable {

    @BlockInput(name = "Operand1", type = NUMBER)
    private int op1=0;

    @BlockInput(name = "Operand2", type = NUMBER)
    private int op2=0;

    @BlockOutput(name = "Operand3", type = NUMBER)
    private static int op3=0;


    @BlockExecute
    public String process()  {
        op3=(int)Math.pow(op1,op2);
        return String.valueOf(op3);
    }

    public static int getOp3() {
        return op3;
    }
}
