package cz.zcu.kiv.eeg.basil;

import java.io.IOException;

import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.LSLDataProvider;

public class LSLTest {

	public static void main(String[] agrs) {
		LSLDataProvider lslData = new LSLDataProvider();
		try {
			lslData.process();
			EEGDataPackageList eegData = lslData.getEegDataPackageList();
			if (eegData != null) {
				System.out.println(eegData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
