# Introduction
The project aims at providing Java-based portable software solution for the 2016 - 2019 Czech-Bavarian BCI project.  
Current version is 1.3.1, which is modified to deal with the EEGDataPackage as ObjectStream, sent and received by 
PipedOutputStream and PipedInputStream. It provides basic functionality to support:

- data acquisition (either off-line - from BrainVision files, or on-line - using Lab Streaming Layer API, or BrainVision
RDA API),
- storing the data into a buffer
- segmentation (either into fixed-size segments for continuous data, or EEG marker-based epochs)
- pre-processing (such as channel selection, baseline removal, frequency filtering, and others)
- feature extraction (windowed means, downsampling, and others)
- classification (preferably using methods from deep learning category based on the Deeplearning4j library, such as stacked autoencoders)
- interpretation and evaluation of the results

# Dependencies
Required libraries are handled by Maven. Sample off-line training and testing data based on the Guess the number experiment are a part of the project.

# Directory structure
- <strong>src/main/java</strong> - contains source codes of the project structured into Java packages (note that preprocessing, feature extraction and classification methods are located in the cz.zcu.kiv.eeg.basil.processing.preprocessing, cz.zcu.kiv.eeg.basil.processing.featureextraction, cz.zcu.kiv.eeg.basil.processing.classification packages, respectively).

- <strong>src/main/resources</strong> - contains   
  1. <strong>data/classifiers</strong> folder with stored classification configurations
  2. <strong>data/numbers</strong>     folder with a large amount of data for classification training and testing from the 'Guess the number' experiment
  3. <strong>liblsl64.dll</strong> for LSL library communication
  
- <strong>src/main/test</strong>  - contains tests for basic communication and a simple workflow test
