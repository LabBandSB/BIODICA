# BIODICA: Independent Component Analysis for BIg Omics Data

BIODICA is a user-friendly pipline for high-performant computation of independent components for omics data, and performing analyses for interpreting the results of ICA application.

BIODICA can work as a Graphical User Interface (GUI) or in the command line mode.

For installing BIODICA, clone the repository into a local folder, with all files, and check that the Java is installed.
Current version of BIODICA requires MATLAB installed (will be changed to MATLAB RunTime in the future).

For minimal configuration, edit the config file in the root folder, and change the 'MATLABFolder' parameter, specifying the path to MATLAB bin folder.
If you want to run the examples of the command line BIODICA uses ('cmdline_examples' folder), modify the 'cmdline_examples/config' file also.

To list all options for the command line use, execute

```
java -cp BIODICA_GUI.jar BIODICAPipeLine
```

Otherwise, launch the BIODICA GUI by executing

```
java -jar BIODICA_GUI.jar
```







