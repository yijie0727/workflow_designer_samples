package cz.zcu.kiv.commons.Stream;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.InputStream;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type ="StreamIO", family = "Stream", runAsJar = false)
public class streamIO {


    @BlockInput(name = "stream1", type = STREAM)
    private InputStream in;


    @BlockOutput(name = "stream2", type = STREAM)
    private InputStream output;


    @BlockExecute
    public void process() throws Exception {

        output = in;
    }


}
