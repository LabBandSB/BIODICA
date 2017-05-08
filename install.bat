xcopy bin\fastica++\binaries\doICA.windows   bin\fastica++\doICA\* /y
move bin\fastica++\doICA\doICA.windows bin\fastica++\doICA\doICA.exe

xcopy bin\fastica++\binaries\doICAInstaller.windows  bin\fastica++\doICA\installers\* /y
move bin\fastica++\doICA\installers\doICAInstaller.windows bin\fastica++\doICA\installers\doICAInstaller.exe

xcopy bin\fastica++\binaries\plotAverageStability.windows  bin\fastica++\doICA\installers\* /y
move bin\fastica++\doICA\installers\plotAverageStability.windows bin\fastica++\doICA\installers\plotAverageStability.exe
 
