package cz.zcu.kiv.commons.Stream;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type ="StreamToFile", family = "Stream", runAsJar = false)
public class StreamToFile {


    @BlockProperty(name = "FileName", type = "String", defaultValue = "test.txt")
    private String fileName;


    @BlockInput(name = "Input", type = STREAM)
    private InputStream in;


    @BlockOutput(name = "File", type ="FILE")
    private File outputFile;


    @BlockExecute
    public File process() throws Exception{

        outputFile = new File(fileName);


        OutputStream out = new FileOutputStream(outputFile);
        byte[] bytes= new byte[1024];

        while(in.read(bytes)!=-1){

            out.write(bytes);
            out.flush();
        }

        String s = "\nThis is the Test of Output of StreamToFile.";
        out.write(s.getBytes());
        out.flush();

        if(out != null)  out.close();
        if(in != null)   in.close();


        return outputFile;
    }




}
