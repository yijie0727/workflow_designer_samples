package cz.zcu.kiv.eeg.basil;

import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.io.XdfDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.ChannelSelectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Prokop on 04.03.2019.
 */
public class EpochExtractionTest {
    @Test
    public void textEpochExtractionBlock() throws Exception {
        String json= FileUtils.readFileToString(new File("src/test/resources/EpochExtractionTest.json"));
        JSONObject jsonObject = new JSONObject(json);

        JSONArray blocksArray = jsonObject.getJSONArray("blocks");
        List<String> blockTypes = new ArrayList<>();
        for(int i = 0; i<blocksArray.length(); i++){
            JSONObject blockObject = blocksArray.getJSONObject(i);
            blockTypes.add(blockObject.getString("type"));
        }

        Map<Class, String> moduleSource = new HashMap<>();
        PackageClass.assignModuleSource(moduleSource,blockTypes);
        JSONArray jsonArray = new BlockWorkFlow(ClassLoader.getSystemClassLoader(), moduleSource,null,"src/test/resources/data",3)
                .execute(jsonObject,"test_result",null);
        assert jsonArray !=null;
    }
}
