package cz.zcu.kiv.eeg.basil.workflow.io;


import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class XDFDataProvider {

	private PyObject xdfLoader;

	public XDFDataProvider() {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.exec("from main import xdf");
		this.xdfLoader = interpreter.get("xdf");
	}
	
	public XDFDataType getData(String filename) {
		PyObject data = this.xdfLoader.__call__(new PyString(filename));
		return (XDFDataType)data.__tojava__(XDFDataType.class);
	}
	
	
}
