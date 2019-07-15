package cz.zcu.kiv.commons.Stream;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.NUMBER;
import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type ="CutMusic", family = "Stream", runAsJar = false)
public class CutMusic {

    @BlockProperty(name = "FileName", type = "String")
    private String cutMusicFileName;

    @BlockProperty(name = "MusicFile", type = "FILE")
    private File musicFileInput;

    @BlockProperty(name = "startPos", type = NUMBER,defaultValue = "0", description = "startPos = x kbps(bitRate) * y seconds * 1024 / 8")
    private int startPos;

    @BlockProperty(name = "endPos", type = NUMBER, defaultValue = "245760", description = "endPos = x kbps(bitRate) * y seconds * 1024 / 8")
    private int endPos;

    @BlockOutput(name = "FileStream", type = STREAM)
    private InputStream swappedInputStream;

    @BlockExecute
    public File process() throws Exception{

        InputStream bIn = new BufferedInputStream(new FileInputStream(musicFileInput));

        File cutMusicFile = new File(cutMusicFileName);
        OutputStream  bOs = new BufferedOutputStream(new FileOutputStream(cutMusicFile,true));

        int total = 0;
        byte[] bytes= new byte[512];
        int len = 0;
        while((len = bIn.read(bytes)) != -1){
            total += len;

            if(total < startPos){  //
                continue;
            }

            bOs.write(bytes);
            if(total >= endPos){
                bOs.flush();
                break;
            }
        }


        swappedInputStream = new FileInputStream(cutMusicFile);

        bIn.close();
        bOs.close();
        return cutMusicFile;
    }




}
