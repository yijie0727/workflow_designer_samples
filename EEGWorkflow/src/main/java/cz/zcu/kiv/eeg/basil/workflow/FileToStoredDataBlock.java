package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.FILE;
import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type="FileToStoredData", family = "File" )
public class FileToStoredDataBlock {

    @BlockInput(name = "StoredFile ",  type = FILE)
    private File file;

    @BlockOutput(name = "PipeOut",  type = STREAM)
    private PipedOutputStream pipedOut = new PipedOutputStream();


    @BlockExecute()
    public void process() throws Exception {


        FileInputStream fileInStream = new FileInputStream(file);

        int b;
        while((b = fileInStream.read())!= 0) {
            pipedOut.write(b);
        }

        fileInStream.close();
        pipedOut.close();
    }




}
