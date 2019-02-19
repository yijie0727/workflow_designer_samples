package cz.zcu.kiv.eeg.basil.workflow.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.Configuration;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfFileData;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfReader;

/**
 * Created by Tomas Prokop on 05.11.2018.
 * By Lukas Vareka transformed into a block.
 */
@BlockType(type="XdfDataProvider", family = "DataProvider", runAsJar = true)
public class XdfDataProviderBlock {
	
	@BlockProperty(name = "XDF File", type = Type.FILE_ARRAY)
    private List<File> xdfFileInputs;
	
	@BlockOutput(name = "EEGData", type = "EEGDataList")
	private EEGDataPackageList eegDataPackageList;
    
	
	private XdfReader xdfReader;
	
	
	@BlockExecute
	public void process() throws IOException {
		ArrayList<EEGDataPackage> eegDataList = new ArrayList<>();
		if (xdfFileInputs != null)  {
			for (File inputXdfFile: xdfFileInputs) {
				this.xdfReader = new XdfReader();
				boolean success = this.xdfReader.read(inputXdfFile.getAbsolutePath());
				
				if (success) {
					XdfFileData data = xdfReader.getXdfData();
					
					EEGDataPackage eegData = new EEGDataPackage();
			        //eegData.setData(data.getData(0).getSamples());
			        //eegData.setChannelNames(channelNames);
			        Configuration configuration=new Configuration();
			        //configuration.setSamplingInterval(Double.parseDouble(getProperty("samplinginterval", data.getF)));
			        eegData.setConfiguration(configuration);
	
			        /*EEGMarker[] eegMarkers = new EEGMarker[markers.size()];
			        int i = 0;
			        for (cz.zcu.kiv.signal.EEGMarker m : markers) {
			            eegMarkers[i] = new EEGMarker(m.getStimulus(), m.getPosition());
			            i++;
			        }
			        eegData.setMarkers(Arrays.asList(eegMarkers));*/
					
				}
			}
		}
		this.eegDataPackageList = new EEGDataPackageList(eegDataList);
		   
	}
}
