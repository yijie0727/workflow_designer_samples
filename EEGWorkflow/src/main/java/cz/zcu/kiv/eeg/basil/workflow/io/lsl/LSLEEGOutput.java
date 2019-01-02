package cz.zcu.kiv.eeg.basil.workflow.io.lsl;

import java.util.Arrays;

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
		int nchannels = this.outletEEG.info().channel_count();
		if (this.eegData != null) {
			for (int i = 0; i < this.eegData[0].length; i++) {
				double[] sample = new double[nchannels];
				for (int j = 0; j < eegData.length; j++) {
					sample[j] = eegData[j][i];
				}
				this.outletEEG.push_sample(sample);
			//	System.out.println("Sending a sample: " + Arrays.toString(sample));
				try {
					Thread.sleep(Math.round(1000.0 / this.outletEEG.info().nominal_srate()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	

}
