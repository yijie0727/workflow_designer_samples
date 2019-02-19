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
	
	
	@BlockProperty(name="EEG Stream Name",type = Type.STRING)
	private String eegStreamName;
	
	@BlockProperty(name="Marker Stream Name",type = Type.STRING)
	private String markerStreamName;
    
	
	private XdfReader xdfReader;
	
	public XdfDataProviderBlock() {
	
	}
		
	public XdfDataProviderBlock(List<File> xdfFileInputs, String eegStreamName, String markerStreamName) {
		this.xdfFileInputs = xdfFileInputs;
		this.eegStreamName = eegStreamName;
		this.markerStreamName = markerStreamName;
	}






	@BlockExecute
	public void process() throws IOException {
		ArrayList<EEGDataPackage> eegDataList = new ArrayList<>();
		if (xdfFileInputs != null)  {
			for (File inputXdfFile: xdfFileInputs) {
				this.xdfReader = new XdfReader();
				boolean success = this.xdfReader.read(inputXdfFile.getAbsolutePath());
				
				if (success) {
					XdfFileData data = xdfReader.getXdfData();
					XdfTransformer xdfTransformer = new XdfTransformer(data, eegStreamName, markerStreamName);
					if (xdfTransformer.convertData()) { // conversion successful
						EEGDataPackage eegData = xdfTransformer.getEEGDataPackage();
						eegDataList.add(eegData);
					}
				}
			}
		}
		this.eegDataPackageList = new EEGDataPackageList(eegDataList);
	}

	public EEGDataPackageList getEegDataPackageList() {
		return eegDataPackageList;
	}

	public void setEegDataPackageList(EEGDataPackageList eegDataPackageList) {
		this.eegDataPackageList = eegDataPackageList;
	}
	
	
	
	
}
