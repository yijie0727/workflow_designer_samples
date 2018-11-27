package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import edu.ucsd.sccn.LSL.StreamOutlet;

public class LSLEEGOutput extends Thread {
	private StreamOutlet outletEEG;
	private double[][] eegData;

	public LSLEEGOutput(StreamOutlet outletEEG) {
		this.outletEEG = outletEEG;
	}
	
	public void setData(double[][] data) {
		this.eegData = data;
		
	}

	@Override
	public void run()  {
		if (this.eegData != null) {
			for (int i = 0; i < this.eegData.length; i++) {
				this.outletEEG.push_sample(this.eegData[i]);
				try {
					Thread.sleep(Math.round(1000 / this.outletEEG.info().nominal_srate()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	

}
