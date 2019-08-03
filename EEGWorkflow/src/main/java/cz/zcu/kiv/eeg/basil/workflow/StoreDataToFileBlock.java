package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockInput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING;

@BlockType(type="StoreDataToFile", family = "File" )
public class StoreDataToFileBlock {



    @BlockProperty(name = "FileName", type = STRING)
    private String fileName;


    @BlockInput(name = "PipeIn",  type = STREAM)
    private PipedInputStream pipeIn = new PipedInputStream();


    @BlockExecute()
    public File process() throws Exception {
        int random = (int)(Math.random()*100000);
        File file = new File(random+"_"+fileName);

//        FileOutputStream fileOut = new FileOutputStream(file, true);
//        int b;
//        while((b = pipeIn.read())!= 0) {
//
//            fileOut.write(b);
//
//        }
//
//
//        fileOut.close();
//        pipeIn.close();
//
//        return file;


        //Or
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        int b;
        while((b = pipeIn.read())!= 0) {

            byteOut.write(b);

        }


        FileUtils.writeByteArrayToFile(file, byteOut.toByteArray());


        byteOut.close();
        pipeIn.close();
        return file;

    }




}
