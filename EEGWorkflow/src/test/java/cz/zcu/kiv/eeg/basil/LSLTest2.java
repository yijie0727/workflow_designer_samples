package cz.zcu.kiv.eeg.basil;

import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSLTest2 {

    public static void main(String[] args) throws Exception {

        System.out.println("Test LSLDataProviderBlock: ");

        String json = FileUtils.readFileToString(new File("src/test/resources/LSLIOTest2.json"), Charset.defaultCharset());

        JSONObject jsonObject = new JSONObject(json);
        File outputFile = File.createTempFile("lslEEGTest__", ".json");
        //outputFile.deleteOnExit();

        JSONArray blocksArray = jsonObject.getJSONArray("blocks");
        List<String> blockTypes = new ArrayList<>();
        for (int i = 0; i < blocksArray.length(); i++) {
            JSONObject blockObject = blocksArray.getJSONObject(i);
            blockTypes.add(blockObject.getString("type"));
        }
        Map<Class, String> moduleSource = new HashMap<>();
        PackageClass.assignModuleSource(moduleSource, blockTypes);


        JSONArray jsonArray = new BlockWorkFlow(ClassLoader.getSystemClassLoader(), moduleSource, null, "src/test/resources/data", 9)
                .execute(jsonObject, "test_result", outputFile.getAbsolutePath());


    }
}
