function doICA(folder,fn,ncomp,varargin)

global ICAFolder; 
global ICAFileName;
global ICANumberOfComponents;

if isdeployed
     ncomp = str2num(ncomp);
end

vis = 'basic';
measure = 'pow3';
%'approach', 'symm', 'g', 'pow3', 'maxNumIterations', 100, 'vis', 'basic'

ICAFolder = folder;
ICAFileName = fn;
ICANumberOfComponents = ncomp;

numiterations = 100;
maxNumIterations = 100;
approach = 'symm';
measure = 'pow3';

dataprovided = 0;

    for i=1:2:length(varargin)
        if strcmpi(varargin{i},'NumIterations')
            numiterations = varargin{i+1};
		if isdeployed
     			numiterations = str2num(numiterations);
        end
        end
        if strcmpi(varargin{i},'maxNumIterations')
            maxNumIterations = varargin{i+1};
		if isdeployed
     			maxNumIterations = str2num(maxNumIterations);
        end
        end
        
        if strcmpi(varargin{i},'vis')
            vis = varargin{i+1};
        end
        if strcmpi(varargin{i},'approach')
            approach = varargin{i+1};
        end
        if strcmpi(varargin{i},'g')
            measure = varargin{i+1};
        end
        
        
        if strcmpi(varargin{i},'data')
            x = varargin{i+1};
            display(sprintf('Data provided (%i x %i)...',size(x,1),size(x,2)));
            dataprovided = 1;
        end
        
    end

if ~dataprovided
    display('Loading data...');
    x = load(strcat(folder,fn));
    x = x';
    display(sprintf('Data loaded (%i x %i)...',size(x,1),size(x,2)));
else
    %i1 = find(strcmp(varargin,'data'));
    %k = setdiff(1:length(varargin),[i1 i1+1]);
    %varargin = varargin(k);
end
    
% <<V1.2>> if the data is the same we can compute the PCA once 
display('Computing PCA once to use in all further iterations');
tic;
[pcaE,pcaD] = pcamat(x, 1, ncomp, 'off', 'off');
display('Finished computing PCA.');
toc;

[iq, A, W, S, sR, convergences]=icasso(x,numiterations,'g',measure,'lastEig',ncomp,'vis',vis,'pcaE',pcaE,'pcaD',pcaD,'maxNumIterations',maxNumIterations,'approach',approach,'g',measure);
[ik,jk] = sort(iq,'descend');
A1 = A(:,jk);
S1 = S(jk,:)';
fnA = sprintf('%sA_%s_%i.num',folder,fn,ncomp);
fnS = sprintf('%sS_%s_%i.num',folder,fn,ncomp);
fnConvs = sprintf('%sConvergence_%s_%i.num',folder,fn,ncomp);
save(fnS,'S1','-ascii','-tabs');
save(fnA,'A1','-ascii','-tabs');
convergences = convergences';
%save(fnConvs,'convergences','-ascii','-tabs');

done = 1;
doneF = sprintf('%s_done',folder);
save(doneF,'done','-ascii');

close all;