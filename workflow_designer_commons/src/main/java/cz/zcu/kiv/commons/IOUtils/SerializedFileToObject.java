package cz.zcu.kiv.commons.IOUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@BlockType(type="SerializedFileToObject",family = "IOUtils")
public class SerializedFileToObject implements Serializable {
    @BlockInput(name = "File", type = "FILE")
    private File fileInput;

    @BlockOutput(name="Object", type="OBJECT")
    private Object objectOutput;

    @BlockExecute
    public void process() throws IOException {
            objectOutput = SerializationUtils.deserialize(FileUtils.readFileToByteArray(fileInput));
    }
}
