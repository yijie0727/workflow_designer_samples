package cz.zcu.kiv.eeg.basil;
import cz.zcu.kiv.WorkflowDesigner.FieldMismatchException;
import cz.zcu.kiv.WorkflowDesigner.Visualizations.Table;
import cz.zcu.kiv.WorkflowDesigner.Workflow;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.workflow.*;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.AveragingBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.BaselineCorrectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.ChannelSelectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import cz.zcu.kiv.eeg.basil.workflow.visualization.EEGDataTableVisualizer;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;


/***********************************************************************************************************************
 *
 * This file is part of the Workflow Designer project

 * ==========================================
 *
 * Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * WorkflowDesignerTest, 2018/17/05 6:32 Joey Pinto
 *
 * This test verifies the creation of all available blocks in the designer
 * The test.jar used for testing is the packaged version of the current project with its dependencies.
 **********************************************************************************************************************/
public class ChainTest {

    @Test
    public void testBlock() {
        OffLineDataProviderBlock dataProviderBlock=new OffLineDataProviderBlock();
        dataProviderBlock.setEegFileInputs(Arrays.asList(new File[]{new File("src/test/resources/data/P300/LED_28_06_2012_104.eeg")}));
        try {
            dataProviderBlock.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert dataProviderBlock.getEegDataPackageList().getEegDataPackage().get(0).getData()!=null;
        assert dataProviderBlock.getEegDataPackageList().getEegDataPackage().get(0).getChannelNames()!=null;

        int oldChannels = dataProviderBlock.getEegDataPackageList().getEegDataPackage().get(0).getChannelNames().length;

        ChannelSelectionBlock channelSelectionBlock=new ChannelSelectionBlock();
        channelSelectionBlock.setEegDataPackageList(dataProviderBlock.getEegDataPackageList());
        channelSelectionBlock.setSelectedChannels(Arrays.asList(new String[]{"Cz","Fz","Pz"}));

        try {
            channelSelectionBlock.process();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        assert  channelSelectionBlock.getEegDataPackageList().getEegDataPackage().get(0).getData()!=null;

        int newChannels = channelSelectionBlock.getEegDataPackageList().getEegDataPackage().get(0).getChannelNames().length;

        assert oldChannels>newChannels;



        EpochExtractionBlock epochExtractionBlock = new EpochExtractionBlock();
        epochExtractionBlock.setPreStimulus(100);
        epochExtractionBlock.setPostStimulus(1000);
        epochExtractionBlock.setEegDataPackageList(channelSelectionBlock.getEegDataPackageList());

        epochExtractionBlock.process();

        BaselineCorrectionBlock baselineCorrectionBlock=new BaselineCorrectionBlock();
        baselineCorrectionBlock.setEegDataPackageList(epochExtractionBlock.getEegDataPackageList());
        baselineCorrectionBlock.setStartTime(0);
        baselineCorrectionBlock.setEndTime(100);

        baselineCorrectionBlock.process();

        assert baselineCorrectionBlock.getEegDataPackageList().getEegDataPackage().size()!=0;

        assert epochExtractionBlock.getEpochs()!=null&&epochExtractionBlock.getEpochs().getEegDataPackage().size()>0;

        AveragingBlock averagingBlock = new AveragingBlock();
        averagingBlock.setEpochs(epochExtractionBlock.getEpochs());
        averagingBlock.setMarkers(Arrays.asList(new EEGMarker("S  2", -1)));
        averagingBlock.process();

        assert averagingBlock.getEegData()!=null;

        EEGDataTableVisualizer eegDataTableVisualizer = new EEGDataTableVisualizer();
        eegDataTableVisualizer.setEegDataPackageList(averagingBlock.getEegData());
        Table table = eegDataTableVisualizer.proces();

        assert table!=null;


    }

    @Test
    public void testChainWorkflow() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FieldMismatchException {
        String json=FileUtils.readFileToString(new File("src/test/resources/chain.json"));
        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray = new Workflow(ClassLoader.getSystemClassLoader(), ":cz.zcu.kiv.eeg.basil",null,"src/test/resources/data").execute(jsonObject,"test_result",null);
        assert jsonArray !=null;
    }
    @Test
    public void testAnnWorkflow() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FieldMismatchException {
        String json=FileUtils.readFileToString(new File("src/test/resources/ann.json"));
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = new Workflow(ClassLoader.getSystemClassLoader(), ":cz.zcu.kiv.eeg.basil",null,"src/test/resources/data").execute(jsonObject,"test_result",null);
        assert jsonArray !=null;
    }





}

