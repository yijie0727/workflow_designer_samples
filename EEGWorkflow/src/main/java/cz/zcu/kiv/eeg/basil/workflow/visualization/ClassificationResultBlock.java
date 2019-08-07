package cz.zcu.kiv.eeg.basil.workflow.visualization;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import cz.zcu.kiv.WorkflowDesigner.Type;
import cz.zcu.kiv.eeg.basil.data.ClassificationStatistics;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tomas Prokop on 17.06.2019.
 */
@BlockType(type="ClassificationResult",family = "Visualization", runAsJar = true)
public class ClassificationResultBlock  implements Serializable {

    @BlockInput(name="ClassificationResult", type ="ClassificationStatistics")
    @BlockOutput(name="ClassificationResult", type ="ClassificationStatistics")
    private ClassificationStatistics statistics;

    @BlockProperty(name = "Stimuli numbers", type = Type.STRING_ARRAY)
    private List<String> stimuliNumbers;

    @BlockProperty(name = "Stimuli names", type = Type.STRING_ARRAY)
    private List<String> stimuliNames;

    @BlockExecute
    public String proces(){
        if(statistics == null)
            return "";

        int[] results = new int[stimuliNumbers.size()];
        Arrays.fill(results,0);
        int size = statistics.getResults().getResults().size();
        for (int i = 0; i < size; i++) {
            String marker = statistics.getResults().getMarkers().get(i);
            int index = stimuliNumbers.indexOf(marker);
            if(index > -1){
                results[index] += statistics.getResults().getResults().get(i);
            }
        }

        double max = Double.MIN_VALUE;
        int index = -1;
        for(int i = 0; i < results.length; i++){
            if(results[i] > max){
                max = results[i];
                index = i;
            }
        }

        if(index > -1 && stimuliNames.size() > index)
            return stimuliNames.get(index);

        return "Result not found in given set of stimuli." + index + "    " + max + "   " + stimuliNames.size();
    }
}
