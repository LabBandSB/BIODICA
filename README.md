# BIODICA Navigator: GUI for Independent Component Analysis for BIg Omics Data

[BIODICA Navigator](https://sysbio-curie.github.io/biodica-environment/) is a user-friendly environment for computation of independent components for omics data, 
using stability analysis and computing the optimal number of the components from their stabilities,
and performing analyses for interpreting the results of ICA application.

BIODICA can work as Graphical User Interface (GUI) or in the command line mode.

BIODICA can use two computational cores for computing ICA: one (default) using Python and one using MATLAB.

Here is the web-site of BIODICA with tutorials and GUI : https://sysbio-curie.github.io/biodica-environment/

## Installation

For installing BIODICA Navigator

1) clone the repository into a local folder, *with all files*. 
Make sure that the name of the local folder does not contain spaces and hypens: otherwise, certain functions won't work.
Do not use the name "BIODICA-master" which will be suggested by GitHub after downloading the repository!

2) check that the Java is installed. Java 1.8 or later version is required.

3) BIODICA Navigator uses Python implementation of ICA by default. For using it, one has to make sure that **python** command can be run from the command line and that [stabilized-ica Python package](https://github.com/ncaptier/stabilized-ica) is installed and accessible.

4) in case of using MATLAB ICA implementation (currently, it is of interest only for historical and compatibility reasons), the following instruction must be followed:

	4a) run *install.bat* (Windows) or *install.sh* in the root folder (this will copy the MATLAB RunTime binaries).

	4b) if you can use docker (recommended) for performing Matlab computations (then MATLAB or binaries are not required and no configuration of MATLAB Runtime is needed) then execute first 
	```
	docker pull auranic/biodica
	```
	and change 
	```
	UseDocker = true
	```
	in the *config* file.

## Running BIODICA Navigator interface

Launch the BIODICA GUI by executing

```
java -jar BIODICA_GUI.jar
```


## Running BIODICA from command line

To list all options for the command line use, execute

```
java -cp BIODICA_GUI.jar BIODICAPipeLine
```

Look at few examples of BIODICA command line use in *cmdline_examples* folder.

## Tips and tricks:

It is not necessary to re-compute the ICA decomposition each time. Application of different procedures is possible for the results computed earlier, or even for the decompositions computed not with BIODICA.
For example, the toppgene analysis can be launched by executing
```
java -cp BIODICA_GUI.jar BIODICAPipeLine -config config -sfile [path_to_metagene_file] -dotoppgene
```





