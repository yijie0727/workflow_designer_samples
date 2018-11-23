package cz.zcu.kiv.eeg.basil.workflow.io;

import org.python.core.PyObject;

public interface XDFDataType {

	public String getFileName();
	public String setFileName(String fileName);
	public PyObject loadXDF();
}
