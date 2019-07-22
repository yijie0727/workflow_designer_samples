package cz.zcu.kiv.commons.IOUtils;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.WorkflowDesigner.Type;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import static cz.zcu.kiv.WorkflowDesigner.Type.FILE;

@BlockType(type="FileToString",family = "IOUtils")
public class FileToString implements Serializable {
    @BlockProperty(name = "File", type = FILE ,defaultValue = "")
    private File fileInput;

    @BlockOutput(name="String", type=Type.STRING)
    private String stringOutput;

    @BlockExecute
    public String process() throws IOException {
            stringOutput = FileUtils.readFileToString(fileInput,Charset.defaultCharset());
            return stringOutput;
    }
}
