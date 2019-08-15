package cz.zcu.kiv.commons.StoreData;

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

        FileOutputStream fileOut = new FileOutputStream(file);

//        int b;
//        while((b = pipeIn.read())!= 0) {
//            fileOut.write(b);
//            fileOut.flush();
//        }
        byte[] bytes= new byte[1];
        while(pipeIn.read(bytes)!=-1){

            fileOut.write(bytes);
            fileOut.flush();
        }

        fileOut.close();
        pipeIn.close();

        return file;





    }

}
