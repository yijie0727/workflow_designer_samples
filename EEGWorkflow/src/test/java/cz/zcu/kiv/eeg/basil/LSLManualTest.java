package cz.zcu.kiv.eeg.basil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cz.zcu.kiv.WorkflowDesigner.Visualizations.PlotlyGraphs.Graph;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackage;
import cz.zcu.kiv.eeg.basil.data.EEGDataPackageList;
import cz.zcu.kiv.eeg.basil.data.EEGMarker;
import cz.zcu.kiv.eeg.basil.workflow.io.OffLineDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.io.lsl.LSLDataProviderBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.AveragingBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.BaselineCorrectionBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EEGMarkerBlock;
import cz.zcu.kiv.eeg.basil.workflow.processing.EpochExtractionBlock;
import cz.zcu.kiv.eeg.basil.workflow.visualization.EEGPlotBlock;

public class LSLManualTest {

	@Test
	public void testLSLWorkflow() throws IOException, InterruptedException {
		// read data
		OffLineDataProviderBlock dataProvider = new OffLineDataProviderBlock();
		dataProvider.setEegFileInputs(Arrays.asList(new File("src/test/resources/data/P300/LED_28_06_2012_104.eeg")));
		dataProvider.process();
		EEGDataPackageList data = dataProvider.getEegDataPackageList();

		// epoch extraction and averaging
		EEGMarker eegMarker = new EEGMarker("S  2", 0);
		EpochExtractionBlock epochExtraction = new EpochExtractionBlock();
		epochExtraction.setPreStimulus(-100);
		epochExtraction.setPostStimulus(1000);
		epochExtraction.setEegDataPackageList(data);
		epochExtraction.process();
		
		// baseline correction
		BaselineCorrectionBlock basCor = new BaselineCorrectionBlock();
		basCor.setStartTime(0);
		basCor.setEndTime(100);
		basCor.setEegDataPackageList(epochExtraction.getEpochs());
		basCor.process();
		
		AveragingBlock averagingBlock = new AveragingBlock();
		averagingBlock.setEpochs(basCor.getEegDataPackageList());
		averagingBlock.setMarkers(Arrays.asList(eegMarker));
		averagingBlock.process();
		
		// plot the results
		List<EEGDataPackage> finalAvgData = averagingBlock.getEegData().getEegDataPackage();
		
		
		EEGPlotBlock plot = new EEGPlotBlock();
		plot.setEegDataList(averagingBlock.getEegData());
		Graph graph = plot.process();
		assert graph != null;
		
		
	}

}
