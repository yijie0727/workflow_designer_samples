package cz.zcu.kiv.eeg.basil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.io.lsl.LSLOutputBlock;

public class LSLOutputTest {
	
	@Test
	public void testBlock() {
		OffLineDataProviderBlock odp = new OffLineDataProviderBlock();
		odp.setEegFileInputs(Arrays.asList(new File("src/test/resources/data/P300/LED_28_06_2012_104.vhdr")));
		try {
			assert odp != null;
			odp.process();
			EEGDataPackageList data = odp.getEegDataPackageList();
			assert data != null;
			LSLOutputBlock output = new LSLOutputBlock();
			assert output != null;
			output.setEegDataPackageList(data);
			output.process();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
