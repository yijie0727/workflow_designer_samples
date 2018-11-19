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

@BlockType(type="StringToFile",family = "IOUtils")
public class StringToFile implements Serializable {

    @BlockInput(name="String", type=Type.STRING)
    private String stringInput;

    @BlockOutput(name = "File", type = "FILE")
    private File fileOutput;

    @BlockExecute
    public File process() throws IOException {
        fileOutput = new File("file_"+new Date().getTime()+".tmp");
        FileUtils.writeStringToFile(fileOutput,stringInput,Charset.defaultCharset());
        return fileOutput;
    }
}
