function doICABatch(folder,fn,ncompvalues)

for i=1:size(ncompvalues,2)
    doICA(folder,fn,ncompvalues(i));
    close all;
end

plotAverageStability(folder);

