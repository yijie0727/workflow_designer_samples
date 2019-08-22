package cz.zcu.kiv.eeg.basil;

import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.ChannelSelectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import cz.zcu.kiv.eeg.basil.workflow.visualization.EEGPlotBlock;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomas Prokop on 11.03.2019.
 */
public class EEGPlotTest {
    @Test
    public void textEEGPlotBlock() throws Exception {
        String json= FileUtils.readFileToString(new File("src/test/resources/EEGPlotTest.json"));
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
