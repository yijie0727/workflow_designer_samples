package cz.zcu.kiv.eeg.basil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cz.zcu.kiv.WorkflowDesigner.BlockWorkFlow;
import cz.zcu.kiv.WorkflowDesigner.WrongTypeException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import cz.zcu.kiv.WorkflowDesigner.FieldMismatchException;

public class XdfWorkflowTest {

	@Test
	public void testJSONLSL() throws  Exception  {
		String json = FileUtils.readFileToString(new File("src/test/resources/XDFFile.json"),Charset.defaultCharset());
		JSONObject jsonObject = new JSONObject(json);
		File outputFile = File.createTempFile("JSONLSL",".json");
		outputFile.deleteOnExit();

		JSONArray blocksArray = jsonObject.getJSONArray("blocks");
		List<String> blockTypes = new ArrayList<>();
		for(int i = 0; i<blocksArray.length(); i++){
			JSONObject blockObject = blocksArray.getJSONObject(i);
			blockTypes.add(blockObject.getString("type"));
		}
		Map<Class, String> moduleSource = new HashMap<>();
		PackageClass.assignModuleSource(moduleSource,blockTypes);


		JSONArray jsonArray = new BlockWorkFlow(ClassLoader.getSystemClassLoader(), moduleSource, null, "",5)
				.execute(jsonObject,"test_result",outputFile.getAbsolutePath());
		for (Object o: jsonArray) {
			System.out.println(o);
		}


	}
}
