function doICABatch(folder,fn,ncompvalues,varargin)

for i=1:size(ncompvalues,2)
    doICA(folder,fn,ncompvalues(i),varargin{:});
    close all;
end
plotAverageStability(folder);

