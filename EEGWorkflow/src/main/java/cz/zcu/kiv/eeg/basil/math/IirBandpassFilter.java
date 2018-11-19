package cz.zcu.kiv.eeg.basil.math;

import cz.zcu.kiv.eeg.basil.math.filter.biz.*;


/**
 * This class simulates function of IIR IirBandpassFilter using DSP-collection library.
 * @author Jan Vampol
 * @version 1.00
 */
public class IirBandpassFilter {

    /** Instance of IirFilter */
    private IirFilter filter;

    /** Instance of IirFilterCoefficients with impulse response values stored */
    private final IirFilterCoefficients coefficients;

    /**
     * Lower frequency
     */
    private double lowerFreq;

    /**
     * Upper frequency
     */
    private double upperFreq;

    /**
     * Ripple in dB used in chebyshev filter. Must be negative value.
     */
    private double ripple = -1;

    /**
     * Filter order
     */
    private int order = 1;

    /**
     * Filter characteristics type
     */
    private FilterCharacteristicsType type;

    /**
     * Filter pass type
     */
    private FilterPassType passType;

    /**
     * Constructor calculates impulse response using DSP-collection and creates instance of IirFilter.
     * This constructor creates bandpass filter.
     *
     * @param lowerFreq Lower frequency - must be lower than upperFreq
     * @param upperFreq Upper frequency - must be lower than sampleRate/2
     * @param sampleRate sampling rate
     * @param type Filter type
     */
    public IirBandpassFilter(double lowerFreq, double upperFreq, int sampleRate, FilterCharacteristicsType type) {
        coefficients = setupFilter(lowerFreq, upperFreq, sampleRate, type, FilterPassType.bandpass, 1);
        filter = new IirFilter(coefficients);
    }

    /**
     * Constructor calculates impulse response using DSP-collection and creates instance of IirFilter.
     *
     * @param lowerFreq Lower frequency - must be lower than upperFreq
     * @param upperFreq Upper frequency - must be lower than sampleRate/2
     * @param sampleRate sampling rate
     * @param type Filter type
     * @param passType Filter pass type
     */
    public IirBandpassFilter(double lowerFreq, double upperFreq, int sampleRate, FilterCharacteristicsType type, FilterPassType passType) {
        coefficients = setupFilter(lowerFreq, upperFreq, sampleRate, type, passType, 1);
        filter = new IirFilter(coefficients);
    }

    /**
     * Constructor calculates impulse response using DSP-collection and creates instance of IirFilter.
     *
     * @param lowerFreq Lower frequency - must be lower than upperFreq
     * @param upperFreq Upper frequency - must be lower than sampleRate/2
     * @param sampleRate sampling rate
     * @param type Filter type
     * @param passType Filter pass type
     * @param order Filter order
     */
    public IirBandpassFilter(double lowerFreq, double upperFreq, int sampleRate, FilterCharacteristicsType type, FilterPassType passType, int order) {
        coefficients = setupFilter(lowerFreq, upperFreq, sampleRate, type, passType, order);
        filter = new IirFilter(coefficients);
    }

    /**
     * Constructor calculates impulse response using DSP-collection and creates instance of IirFilter.
     * This constructor creates bandpass chebyshev filter.
     *
     * @param lowerFreq Lower frequency - must be lower than upperFreq
     * @param upperFreq Upper frequency - must be lower than sampleRate/2
     * @param sampleRate Sampling rate of VisionRecorder
     */
    public IirBandpassFilter(double lowerFreq, double upperFreq, int sampleRate) {
        coefficients = setupFilter(lowerFreq, upperFreq, sampleRate, FilterCharacteristicsType.chebyshev, FilterPassType.bandpass, 1);
        filter = new IirFilter(coefficients);
    }

    /**
     * Method for filtering signal. Gets input sample and filters it
     * using DSP-collection library.
     *
     * @param inputSample Input data
     * @return double Output data
     */
    public double getOutputSample(double inputSample) {
        return filter.step(inputSample);
    }

    /**
     * Method sets up filter so it is ready to use. Calculates impulse response.
     *
     * @param lowerFreq Lower frequency
     * @param upperFreq Upper frequency
     * @param sampleRate Sampling rate of VisionRecorder
     * @param type Filter type
     * @param passType Filter pass type
     * @return double[] IirFilterCoefficients instance
     */
    private IirFilterCoefficients setupFilter(double lowerFreq, double upperFreq, int sampleRate, FilterCharacteristicsType type, FilterPassType passType, int order) {
        this.lowerFreq = lowerFreq;
        this.upperFreq = upperFreq;
        this.passType = passType;
        this.order = order;
        this.type = type;

        return IirFilterDesignFisher.design(passType, type, order, ripple, lowerFreq/sampleRate, upperFreq/sampleRate);
    }

    public double getLowerFreq() {
        return lowerFreq;
    }

    public double getUpperFreq() {
        return upperFreq;
    }

    public int getOrder() {
        return order;
    }

    public FilterCharacteristicsType getType() {
        return type;
    }

    public FilterPassType getPassType() {
        return passType;
    }

    public double getRipple() {

        return ripple;
    }

    public void setRipple(double ripple) {
        this.ripple = ripple;
    }

    public String toString() {
        String ret = "Filter Characteristics Type: " + type + "\n";
        ret += "Pass Type: " + passType + "\n";
        ret += "Order: " + order + "\n";
        ret += "Lower Frequency: " + lowerFreq + "\n";
        ret += "Upper Frequency: " + upperFreq + "\n";
        ret += type == FilterCharacteristicsType.chebyshev ? "Ripple: " + ripple + "\n" : "";
        return ret;
    }

    public void reset() {
        this.filter = new IirFilter(coefficients);
    }
}
