package cz.zcu.kiv.eeg.basil;

import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.io.XdfDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.ChannelSelectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Prokop on 04.03.2019.
 */
public class EpochExtractionTest {
    @Test
    public void textEpochExtractionBlock() throws Exception {
        OffLineDataProviderBlock provider = new OffLineDataProviderBlock();
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("src/test/resources/data/numbers/17ZS/17ZS_14_4_2015_02.eeg"));
        provider.setEegFileInputs(files);

        provider.process();
        EEGDataPackageList lst = provider.getEegDataPackageList();

        ChannelSelectionBlock chsb = new ChannelSelectionBlock();
        chsb.setEegDataPackageList(lst);
        ArrayList<String> channelLst = new ArrayList<>();
        channelLst.add(("Pz"));
        chsb.setSelectedChannels(channelLst);
        chsb.process();
        lst = chsb.getEegDataPackageList();

        EpochExtractionBlock eeb = new EpochExtractionBlock();
        eeb.setEegDataPackageList(lst);
        eeb.setPreStimulus(-100);
        eeb.setPostStimulus(1000);
        eeb.process();
    }
}
