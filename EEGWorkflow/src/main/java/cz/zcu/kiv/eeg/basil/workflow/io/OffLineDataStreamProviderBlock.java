package cz.zcu.kiv.eeg.basil.workflow.io;

import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.signal.ChannelInfo;
import cz.zcu.kiv.signal.DataTransformer;
import cz.zcu.kiv.signal.EEGDataTransformer;

import java.io.*;
import java.nio.ByteOrder;
import java.util.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;


@BlockType(type="OffLineDataStreamProvider", family = "DataProvider", runAsJar = true)
public class OffLineDataStreamProviderBlock implements Serializable {

    private static final String VHDR_EXTENSION = ".vhdr";
    private static final String VMRK_EXTENSION = ".vmrk";

    @BlockProperty(name = "EEG File", type = Type.FILE_ARRAY)
    private List<File> eegFileInputs;

//    @BlockOutput(name = "EEGData", type = "EEGDataList")
//    private EEGDataPackageList eegDataPackageList;

    @BlockOutput(name = "EEGDataStreams", type = STREAM)
    private ObjectInputStream EEGsOIS;


    @BlockExecute
    public void process() throws IOException {

        //TODO : eegDataList gradually filled with data:  eegDataList.add(eegData);
        // Instead of inserting into an array,
        // send eegData to stream right away.

        ByteArrayOutputStream bytesOutStream = new ByteArrayOutputStream();
        ObjectOutputStream EEGsOOS = new ObjectOutputStream(bytesOutStream);

        ByteOrder order = ByteOrder.LITTLE_ENDIAN;
        for(File eegFileInput: eegFileInputs){
            File vhdrFile = new File(eegFileInput.getParentFile().getAbsolutePath()+File.separator+eegFileInput.getName().split("\\.")[0]+VHDR_EXTENSION);
            File vmrkFile = new File(eegFileInput.getParentFile().getAbsolutePath()+File.separator+eegFileInput.getName().split("\\.")[0]+VMRK_EXTENSION);

            DataTransformer dt = new EEGDataTransformer();

            List<ChannelInfo> channels = dt.getChannelInfo(vhdrFile.getAbsolutePath());

            List<cz.zcu.kiv.signal.EEGMarker> markers = dt.readMarkerList(vmrkFile.getAbsolutePath());

            int len = channels.size();
            double[][] data = new double[len][];
            String[] channelNames=new String[len];
            for (int i = 0; i < channels.size(); i++) {
                data[i] = dt.readBinaryData(vhdrFile.getAbsolutePath(), eegFileInput.getAbsolutePath(), i + 1, order);
                channelNames[i]=channels.get(i).getName();
            }
            EEGDataPackage eegData=new EEGDataPackage();
            eegData.setData(data);
            eegData.setChannelNames(channelNames);
            Configuration configuration=new Configuration();
            configuration.setSamplingInterval(Double.parseDouble(getProperty("samplinginterval", dt)));
            eegData.setConfiguration(configuration);

            EEGMarker[] eegMarkers = new EEGMarker[markers.size()];
            int i = 0;
            for (cz.zcu.kiv.signal.EEGMarker m : markers) {
                eegMarkers[i] = new EEGMarker(m.getStimulus(), m.getPosition());
                i++;
            }
            eegData.setMarkers(Arrays.asList(eegMarkers));

            //eegDataList.add(eegData);

            EEGsOOS.writeObject(eegData);
            EEGsOOS.flush();
        }

        EEGsOOS.close();

        //eegDataPackageList=new EEGDataPackageList(eegDataList);
        byte[] eegPackages = bytesOutStream.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(eegPackages);
        EEGsOIS = new ObjectInputStream(in);
    }



    private String getProperty(String propName, DataTransformer dt) {
        HashMap<String, HashMap<String, String>> props = dt.getProperties();
        for (Map.Entry<String, HashMap<String, String>> entry : props.entrySet()) {
            for (Map.Entry<String, String> prop : entry.getValue().entrySet()) {
                if (prop.getKey().equalsIgnoreCase(propName)) {
                    return prop.getValue();
                }
            }
        }

        return null;
    }

    public List<File> getEegFileInputs() {
        return eegFileInputs;
    }

    public void setEegFileInputs(List<File> eegFileInputs) {
        this.eegFileInputs = eegFileInputs;
    }





}
