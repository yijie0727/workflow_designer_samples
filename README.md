# INCF Workflow Designer samples

[![N|Solid](https://4.bp.blogspot.com/-AY7eIsmbH0Y/WLRdpe78DJI/AAAAAAAABDU/lsb2XqcmyUsLqYo6yzo9HYMY4vLn3q_OgCLcB/s1600/vertical%2BGSoC%2Blogo.jpg)](http://zedacross.com/gsoc2018)

This project hosts a few examples of how to use the Workflow Designer annotation library. These examples consists of 
different design patterns and project architectures in which the library can be used.

Clone this repository and open any project folder in IntellijIDEA to get started.


# workflow_desinger_test(1,2)!

  - These projects hosts the most basic arithmetic block examples and the exponential block.
  - This is a complete bare-bones project and is a great starting point to experiment.
  - We recommend studying the project for JAVA beginners, intermediate users may go to the commons project directly.


# workflow_desinger_commons!

  - The commons project is a rather extensive project that contains samples of all the inbuilt feature of the library 
  like file upload, String manipulation, Arithmetic, Table output, Graph output, Stream processing etc.
  - This is a great reference project to create your own libraries and see how customized outputs can be generated.
  - This also a great project to develop test cases for people contributing to the project.
  - Current version is 1.2.1

# EEGDataAnalyticsPackage!

  - The EEGDataAnalytics project is an Apache Spark based project
  - The [original project](https://github.com/NEUROINFORMATICS-GROUP-FAV-KIV-ZCU/EEG_DataAnalysisPackage) is available here.
  - Compare the source code of this project and how i### Copyright

 
  This file is part of the Workflow Designer project

  ==========================================
 
  Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
 
 ***********************************************************************************************************************
 
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  the License. You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  specific language governing permissions and limitations under the License.t has been modified to fit into the workflow designer design pattern.
  - This is a nice place to understand how the runAsJar feature works.
  - Check that the classes are all serializable
  - Note: You will need a hadoop cluster to run this project
  - This project also uses data visualization using graphs and tables

# EEGWorkflow!

  - The EEGWorkflow is an implementation of the BASIL project by INCF
  - The [original project](https://github.com/NEUROINFORMATICS-GROUP-FAV-KIV-ZCU/Basil_BCI) is available here.
  - Compare the source code of this project and how it has been modified to fit into the workflow designer design pattern.
  - This is a nice place to understand how machine learning workflows can be created.
  - There are examples how infinite chains of workflows can be created (e.g. Neural Network Layers)
  - This is a great place for advanced users to understand the real merit of the project
  - Current version is 1.3.1, which is modified to deal with the EEGDataPackage as ObjectStream, sent and received by 
 PipedOutputStream and PipedInputStream.

For more information visit http://www.zedacross.com/gsoc2018/

### Copyright

 
  This file is part of the Workflow Designer project

  ==========================================
 
  Copyright (C) 2018 by University of West Bohemia (http://www.zcu.cz/en/)
 
 ***********************************************************************************************************************
 
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  the License. You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  specific language governing permissions and limitations under the License.

