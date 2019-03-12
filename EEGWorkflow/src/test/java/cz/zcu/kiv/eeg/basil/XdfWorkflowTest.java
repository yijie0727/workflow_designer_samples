package cz.zcu.kiv.eeg.basil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import cz.zcu.kiv.WorkflowDesigner.FieldMismatchException;
import cz.zcu.kiv.WorkflowDesigner.Workflow;

public class XdfWorkflowTest {

	@Test
    public void testJSONLSL() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FieldMismatchException {
	    String json = FileUtils.readFileToString(new File("src/test/resources/XDFFile.json"),Charset.defaultCharset());
	    JSONObject jsonObject = new JSONObject(json);
	    File outputFile = File.createTempFile("JSONLSL",".json");
	    outputFile.deleteOnExit();
	    JSONArray jsonArray = new Workflow(ClassLoader.getSystemClassLoader(), ":cz.zcu.kiv", null, "").execute(jsonObject,"test_data",outputFile.getAbsolutePath());
	    for (Object o: jsonArray) {
	      	System.out.println(o);
	    }

	 
	}
}
