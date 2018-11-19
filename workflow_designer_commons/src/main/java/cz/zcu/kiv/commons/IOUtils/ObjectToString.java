package cz.zcu.kiv.commons.IOUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Type;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

@BlockType(type="ObjectToString",family = "IOUtils")
public class ObjectToString implements Serializable {

    @BlockInput(name="Object", type="OBJECT")
    private Object objectInput;

    @BlockOutput(name = "String", type = Type.STRING)
    private File fileOutput;

    @BlockExecute
    public void process() throws IOException {
        fileOutput = new File("file_"+new Date().getTime()+".tmp");
        FileUtils.writeStringToFile(fileOutput,objectInput.toString(),Charset.defaultCharset());
    }
}