package cz.zcu.kiv.eeg.basil.workflow.processing;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static cz.zcu.kiv.WorkflowDesigner.Type.STRING_ARRAY;

@BlockType(type="ChannelSelection",family = "Preprocessing", runAsJar = true)
public class ChannelSelectionBlock implements Serializable {

    @BlockProperty(name="channels",type = STRING_ARRAY)
    private List<String> selectedChannels;

    @BlockInput(name = "EEGData", type = "EEGDataList")
    @BlockOutput(name = "EEGData", type = "EEGDataList")
    private EEGDataPackageList eegDataPackageList;


    @BlockExecute
    public void process() throws Exception {
        for(EEGDataPackage eegData:eegDataPackageList.getEegDataPackage()){
            String[] channels = eegData.getChannelNames();
            if (channels == null || channels.length == 0)
                throw new Exception("No channels Selected"); // no channel selection possible - names missing in the data

            List<String> currentChannelNames  = new ArrayList<>(Arrays.asList(channels));
            List<String> selectedChannelNames = new ArrayList<>(selectedChannels);
            List<Integer> selectedPointers    = new ArrayList<>();

            for (String selectedChannel : selectedChannels) {
                int index = currentChannelNames.indexOf(selectedChannel);
                if(index > -1)
                    selectedPointers.add(index);
                else
                    selectedChannelNames.remove(selectedChannel);
            }

            double[][] originalEegData = eegData.getData();
            double[][] reducedData = new double[selectedPointers.size()][originalEegData[0].length];

            for (int i = 0; i < selectedPointers.size(); i++) {
                System.arraycopy(originalEegData[selectedPointers.get(i)], 0, reducedData[i], 0, originalEegData[0].length);
            }

            eegData.setData(reducedData);
            String[] array = new String[selectedChannelNames.size()];
            eegData.setChannelNames(selectedChannelNames.toArray(array));
        }

    }

    public List<String> getSelectedChannels() {
        return selectedChannels;
    }

    public void setSelectedChannels(List<String> selectedChannels) {
        this.selectedChannels = selectedChannels;
    }

    public EEGDataPackageList getEegDataPackageList() {
        return eegDataPackageList;
    }

    public void setEegDataPackageList(EEGDataPackageList eegDataPackageList) {
        this.eegDataPackageList = eegDataPackageList;
    }
}
