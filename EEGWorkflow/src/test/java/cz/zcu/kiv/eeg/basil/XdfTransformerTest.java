package cz.zcu.kiv.eeg.basil;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.XdfDataProviderBlock;

public class XdfTransformerTest {

    //@Test
    public void textXdfTransform() throws IOException {
        XdfDataProviderBlock provider = new XdfDataProviderBlock(Arrays.asList(new File("src/test/resources/data/xdf/2.xdf")), "EEG", "psychopy_stimuli");
        provider.process();
        EEGDataPackageList output = provider.getEegDataPackageList();
        assertTrue(output != null && output.getEegDataPackage().size() > 0);
    }
}
