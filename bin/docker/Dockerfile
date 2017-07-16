# Docker for BIODICA command line environment
#
# Since BIODICA uses compiled Matlab code to perform the computations,
# Matlab RunTime v8.1 is also installed, together with the latest Java version.
#
# 
#

FROM colinrhodes/matlab-mcr

MAINTAINER Andrei Zinovyev <zinovyev@gmail.com>

RUN mkdir /BIODICA

RUN cd /BIODICA && \
    wget -nv --no-check-certificate https://github.com/LabBandSB/BIODICA/raw/master/bin/fastica%2B%2B/binaries/run_plotAverageStability_linux.sh && \
    wget -nv --no-check-certificate https://github.com/LabBandSB/BIODICA/raw/master/bin/fastica%2B%2B/binaries/doICA_linux && \
    wget -nv --no-check-certificate https://github.com/LabBandSB/BIODICA/raw/master/bin/fastica%2B%2B/binaries/run_doICA_linux.sh && \
    wget -nv --no-check-certificate https://github.com/LabBandSB/BIODICA/raw/master/bin/fastica%2B%2B/binaries/plotAverageStability_linux && \
    chmod 777 doICA_linux && \
    chmod 777 run_doICA_linux.sh && \
    chmod 777 plotAverageStability_linux && \
    chmod 777 run_plotAverageStability_linux.sh 

CMD /BIODICA/run_doICA_linux.sh /opt/mcr/v81/