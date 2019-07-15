package cz.zcu.kiv.commons.Stream;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type ="MergeMusic", family = "Stream", runAsJar = false)
public class MergeMusic {


    @BlockProperty(name = "FileName", type = "String", defaultValue = "newMusic.mp3")
    private String newMusicFileName;


    @BlockInput(name = "Stream1", type = STREAM)
    private InputStream in1;

    @BlockInput(name = "Stream2", type = STREAM)
    private InputStream in2;


    @BlockOutput(name = "MusicFile", type ="FILE")
    private File outputMusic;


    @BlockExecute
    public File process() throws Exception{

        outputMusic = new File(newMusicFileName);
        OutputStream bOs =  new FileOutputStream(outputMusic,true); //true means add at end, not start from beginning

        byte[] bytes1= new byte[512];
        while( (in1.read(bytes1)) != -1 ){//write first music
            bOs.write(bytes1);
            bOs.flush();
        }


        byte[] bytes2= new byte[512];
        while( (in2.read(bytes2)) != -1 ){//write second music
            bOs.write(bytes2);
            bOs.flush();
        }




        if(in1 != null) in1.close();
        if(in2 != null) in2.close();
        if(bOs != null) bOs.close();
        return outputMusic;
    }






}
