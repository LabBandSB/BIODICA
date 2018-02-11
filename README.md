# BIODICA: Independent Component Analysis for BIg Omics Data

BIODICA is a user-friendly pipline for high-performant computation of independent components for omics data, 
using stability analysis and computing the optimal number of the components from their stabilities,
and performing analyses for interpreting the results of ICA application.

BIODICA can work as Graphical User Interface (GUI) or in the command line mode.

Our tests show that BIODICA is the most performant existing implementation of fastICA algorithm
(10-50 times faster than existing implementation in R).

## Installation

For installing BIODICA

1) clone the repository into a local folder, *with all files*. 
Make sure that the name of the local folder does not contain spaces and hypens: otherwise, certain functions won't work.
Do not use the name "BIODICA-master" which will be suggested by GitHub after downloading the repository!

2) check that the Java is installed. Java 1.8 or later version is required.

3) run *install.bat* (Windows) or *install.sh* in the root folder (this will copy the MATLAB RunTime binaries).

4) if you can use docker (recommended) for performing Matlab computations (then MATLAB or binaries are not required and no configuration of MATLAB Runtime is needed) then execute first 

```
docker pull auranic/biodica
```

and change 

```
UseDocker = true
```
in the *config* file.

5) For minimal configuration, edit the *config* file in the root folder for the way to use MATLAB (docker solution is used by default, otherwise see tutorial on how to configure).
One can change the 'MATLABFolder' parameter, specifying the path to MATLAB bin folder: in this case, the MATLAB installation will be used instead.
If you want to run the examples of the command line BIODICA uses ('cmdline_examples' folder), modify the 'cmdline_examples/config' file also.

## Running BIODICA

To list all options for the command line use, execute

```
java -cp BIODICA_GUI.jar BIODICAPipeLine
```

Otherwise, launch the BIODICA GUI by executing

```
java -jar BIODICA_GUI.jar
```
Look at few examples of BIODICA command line use in *cmdline_examples* folder.

## Tips and tricks:

It is not necessary to re-compute the ICA decomposition each time. Application of different procedures is possible for the results computed earlier, or even for the decompositions computed not with BIODICA.
For example, the toppgene analysis can be launched by executing
```
java -cp BIODICA_GUI.jar BIODICAPipeLine -config config -sfile [path_to_metagene_file] -dotoppgene
```





