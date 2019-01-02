package cz.zcu.kiv.eeg.basil.data;


import java.io.Serializable;
import java.util.List;

public class EEGDataPackageList implements Serializable {
    List<EEGDataPackage> eegDataPackage;

    public EEGDataPackageList(List<EEGDataPackage> eegDataPackage) {
        this.eegDataPackage = eegDataPackage;
    }

    public List<EEGDataPackage> getEegDataPackage() {
        return eegDataPackage;
    }

    public void setEegDataPackage(List<EEGDataPackage> eegDataPackage) {
        this.eegDataPackage = eegDataPackage;
    }
}
