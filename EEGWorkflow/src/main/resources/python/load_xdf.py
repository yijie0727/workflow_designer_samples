import main.xdf as xdf

from cz.zcu.kiv.eeg.basil.workflow.io import XDFDataType




class XDFLoad(XDFDataType):
    def __init__(self):
        self.filename = None;
    
    def setFileName(self, filename):
        self.filename = filename;
        
    def getFileName(self):
        return self.filename;
    
    def loadXDF(self):
        return xdf.load(self.filename)
        
        
        