function doICAtxt(folder,fn,ncomp)

javaclasspath({'VDAOEngine.jar'});
process = vdaoengine.ProcessTxtData;
javaMethod('callFromMatlab',process,{sprintf('%s%s%%%s%%%s',folder,fn,'-center','-prepare4ICA')});

fn1 = strrep(fn,'.txt','');
fn1 = sprintf('%s_ica_numerical.txt',fn1);
fn2 = strrep(fn1,'_numerical.txt','');

x = load(strcat(folder,fn1));
x = x';

global ICAFolder; 
global ICAFileName;
global ICANumberOfComponents;

ICAFolder = folder;
ICAFileName = fn;
ICANumberOfComponents = ncomp;

[iq, A, W, S, sR]=icasso(x,5,'g','pow3','lastEig',ncomp);
[ik,jk] = sort(iq,'descend');
A1 = A(:,jk);
S1 = S(jk,:)';
fnA = sprintf('%sA_%s_%i.num',folder,fn1,ncomp);
fnS = sprintf('%sS_%s_%i.num',folder,fn1,ncomp);
save(fnS,'S1','-ascii','-tabs');
save(fnA,'A1','-ascii','-tabs');

javaMethod('callFromMatlab',process,{sprintf('-assembleICAresults%%%s;%s',folder,fn2)});

done = 1;
doneF = sprintf('%s_done',folder);
save(doneF,'done','-ascii');