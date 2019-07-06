package cz.zcu.kiv.eeg.basil;
import cz.zcu.kiv.WorkflowDesigner.Annotations.BlockType;
import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import cz.zcu.kiv.WorkflowDesigner.FieldMismatchException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;


/***********************************************************************************************************************
 *
 * This file is part of the Workflow Designer project

 * ==========================================
 *
 * Copyright (C) 2019 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WorkflowDesignerTest, 2018/17/05 6:32 Joey Pinto, 2019 GSoC P2 Yijie Huang
 * This test verifies the creation of all available blocks in the designer
 * The test.jar used for testing is the packaged version of the current project with its dependencies.
 **********************************************************************************************************************/
public class ChainTest {



    @Test
    public void testChainWorkflow() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FieldMismatchException, InterruptedException, ExecutionException {
        String json=FileUtils.readFileToString(new File("src/test/resources/chain.json"));
        JSONObject jsonObject = new JSONObject(json);

        JSONArray blocksArray = jsonObject.getJSONArray("blocks");
        List<String> blockTypes = new ArrayList<>();
        for(int i = 0; i<blocksArray.length(); i++){
            JSONObject blockObject = blocksArray.getJSONObject(i);
            blockTypes.add(blockObject.getString("type"));
        }

        Map<Class, String> moduleSource = new HashMap<>();
        PackageClass.assignModuleSource(moduleSource,blockTypes);
        JSONArray jsonArray = new BlockWorkFlow(ClassLoader.getSystemClassLoader(), moduleSource,null,"src/test/resources/data",3).execute(jsonObject,"test_result",null);
        assert jsonArray !=null;
    }

    @Test
    public void testEEGTestJSON() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FieldMismatchException, InterruptedException, ExecutionException {

        String json = FileUtils.readFileToString(new File("src/test/resources/EEGTest.json"), Charset.defaultCharset());

        JSONObject jsonObject = new JSONObject(json);
        File outputFile = File.createTempFile("EEGTest__", ".json");
        //outputFile.deleteOnExit();

        JSONArray blocksArray = jsonObject.getJSONArray("blocks");
        List<String> blockTypes = new ArrayList<>();
        for (int i = 0; i < blocksArray.length(); i++) {
            JSONObject blockObject = blocksArray.getJSONObject(i);
            blockTypes.add(blockObject.getString("type"));
        }
        Map<Class, String> moduleSource = new HashMap<>();
        PackageClass.assignModuleSource(moduleSource, blockTypes);


        JSONArray jsonArray = new BlockWorkFlow(ClassLoader.getSystemClassLoader(), moduleSource, null, "src/test/resources/data", 5).execute(jsonObject, "test_data", outputFile.getAbsolutePath());
        for (Object o : jsonArray) {
            System.out.println(o);
        }


    }



}

