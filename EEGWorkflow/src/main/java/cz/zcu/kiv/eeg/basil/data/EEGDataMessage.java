/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eeg.basil.data;

import java.util.Arrays;

/**
 * EEG data message used to send data from data provider
 *
 * @author Prokop
 */
public class EEGDataMessage extends EEGMessage {

    /**
     * Markers
     */
    private final EEGMarker[] markers;

    /**
     * Data
     */
    private final double[][] data; /* NUMBER_OF_CHANNELS x BLOCK_SIZE */

    /**
     * Creates new EEG data message
     * @param messageId ID
     * @param markers array of markers
     * @param data data
     */
    public EEGDataMessage(int messageId, EEGMarker[] markers, double[][] data) {
        super(messageId);
        this.markers = markers;
        this.data = data;
    }

    /**
     * Get markers
     * @return markers
     */
    public EEGMarker[] getMarkers() {
        return markers;
    }

    /**
     * Get data
     * @return data
     */
    public double[][] getData() {
        return data;
    }
    
    @Override
    public String toString() {
		return "Markers: " + Arrays.toString(markers) + "\n " + "Data: " + Arrays.deepToString(data);
    	
    }
}