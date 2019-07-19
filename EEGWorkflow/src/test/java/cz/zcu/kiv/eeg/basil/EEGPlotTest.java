package cz.zcu.kiv.eeg.basil;

import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.ChannelSelectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import cz.zcu.kiv.eeg.basil.workflow.visualization.EEGPlotBlock;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tomas Prokop on 11.03.2019.
 */
public class EEGPlotTest {
    @Test
    public void textEEGPlotBlock() throws Exception {
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

        EEGPlotBlock plot = new EEGPlotBlock();
        plot.setEegDataList(eeb.getEegDataPackageList());
        plot.process();
    }
}
