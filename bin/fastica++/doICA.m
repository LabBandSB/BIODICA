function doICA(folder,fn,ncomp,varargin)

global ICAFolder; 
global ICAFileName;
global ICANumberOfComponents;

ICAFolder = folder;
ICAFileName = fn;
ICANumberOfComponents = ncomp;

numiterations = 100;

    for i=1:2:length(varargin)
        if strcmpi(varargin{i},'NumIterations')
            numiterations = varargin{i+1};
        end
    end


x = load(strcat(folder,fn));
x = x';

[iq, A, W, S, sR]=icasso(x,numiterations,'g','pow3','lastEig',ncomp);
[ik,jk] = sort(iq,'descend');
A1 = A(:,jk);
S1 = S(jk,:)';
fnA = sprintf('%sA_%s_%i.num',folder,fn,ncomp);
fnS = sprintf('%sS_%s_%i.num',folder,fn,ncomp);
save(fnS,'S1','-ascii','-tabs');
save(fnA,'A1','-ascii','-tabs');

done = 1;
doneF = sprintf('%s_done',folder);
save(doneF,'done','-ascii');