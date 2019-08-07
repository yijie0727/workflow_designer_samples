package cz.zcu.kiv.eeg.basil.workflow.io;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockExecute;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockOutput;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockProperty;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfFileData;
import cz.zcu.kiv.eeg.basil.workflow.io.xdf.XdfReader;

import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

/**
 * Provider of the XDF file data.
 * Reads the XDF data structure and
 * then applies a converter to EEGDataPackageLists.
 * 
 * Currently stores all the data at once and does not support streaming.
 * 
 * 
 * Created by Tomas Prokop on 05.11.2018.
 * By Lukas Vareka transformed into a block. 19. 02. 2019
 * 
 */
@BlockType(type="XdfDataProvider", family = "DataProvider", runAsJar = true)
public class XdfDataProviderBlock {
	
	@BlockProperty(name = "XDF File", type = Type.FILE_ARRAY)
    private List<File> xdfFileInputs;
	
	@BlockProperty(name="EEG Stream Name",type = Type.STRING)
	private String eegStreamName;
	
	@BlockProperty(name="Marker Stream Name",type = Type.STRING)
	private String markerStreamName;

	@BlockOutput(name = "EEGData", type = STREAM)
	private PipedOutputStream eegPipeOut = new PipedOutputStream();

	private EEGDataPackageList eegDataPackageList;
	
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
		ObjectOutputStream eegObjectOut = new ObjectOutputStream(eegPipeOut);

		List<EEGDataPackage> eegDataList = new ArrayList<>();
		if (xdfFileInputs != null)  {
			for (File inputXdfFile: xdfFileInputs) {
				this.xdfReader = new XdfReader();
				boolean success = this.xdfReader.read(inputXdfFile.getAbsolutePath());

				// data were loaded successfully
				if (success) {
					XdfFileData data = xdfReader.getXdfData();
					XdfTransformer xdfTransformer = new XdfTransformer(data, eegStreamName, markerStreamName);
					if (xdfTransformer.convertData()) { // conversion successful
						EEGDataPackage eegData = xdfTransformer.getEEGDataPackage();
						eegDataList.add(eegData);

						eegObjectOut.writeObject(eegData);
						eegObjectOut.flush();

					}
				}
			}
		}
		this.eegDataPackageList = new EEGDataPackageList(eegDataList);

		eegObjectOut.writeObject(null);
		eegObjectOut.flush();

		eegObjectOut.close();
		eegPipeOut.close();

	}

	public EEGDataPackageList getEegDataPackageList() {
		return eegDataPackageList;
	}

	
	
	
	
}
