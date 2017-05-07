function [averageStability, nums, slopes, slopes0, intercepts, residues, residues0, goodness, total] = plotAverageStability(folder)

ls = dir(folder);

count = 1;

total = [0 0];

for i=3:size(ls,1)
    name = ls(i).name;
    k = size(strfind(name,'stability.txt'),1);
    if(k>0)
        xx = load(strcat(folder,name));
        array(count,1) = size(xx,1);
        array(count,2) = mean(xx);
        array(count,3) = mean(xx(1:min(10,size(xx,1))));
        array(count,4) = mean(xx(1:min(20,size(xx,1))));
        array(count,5) = mean(xx(1:min(30,size(xx,1))));
        
        x = sort(xx,'descend'); [fo,gdn] = fit([1:size(x,1)]',x,'poly1');
        array(count,6) = fo.p1; 
        array(count,7) = fo.p2;
        array(count,8) = -mean((1-x)./[2:size(x,1)+1]');
        
        array(count,9) = sum((x-fo.p2-fo.p1*[1:size(x,1)]').^2)/count;
        array(count,9) = sqrt(array(count,9));
        
        array(count,10) = sum((x-1+array(count,8)*[1:size(x,1)]').^2)/count;
        array(count,10) = sqrt(array(count,10));
        
        array(count,11) = gdn.adjrsquare;
        
        count = count+1;
        
        tot = size(total,1);
        for l=1:size(x,1)
            total(tot+l-1,1) = l;
            total(tot+l-1,2) = x(l);
        end
        
%         if(size(xx,1)==27)
%             mstd6 = xx;
%         end
%         if(size(xx,1)==60)
%             mstd5 = xx;
%         end
%         if(size(xx,1)==8)
%             mstd8 = xx;
%         end
            %plot(sort(xx,'descend'),'k-'); hold on; 
            %[h,p] = hist(xx);
            %plot(p,h,'k-'); hold on;
    end
end


%[h,p] = hist(mstd);
            %plot(p,h,'r-','LineWidth',5); hold on;
            
[xs, inds] = sort(array(:,1));
averageStability = array(inds,2);
nums = array(inds,1);
slopes = array(inds,6);
intercepts= array(inds,7);
slopes0 = array(inds,8);
residues = array(inds,9);
residues0 = array(inds,10);
goodness = array(inds,11);            
[sminvalues, mstd] = computeMSTDProfile(nums,averageStability);
        nmstd5 = mstd(find(abs(sminvalues-0.5)<0.001));
        nmstd6 = mstd(find(abs(sminvalues-0.6)<0.001));
        nmstd7 = mstd(find(abs(sminvalues-0.7)<0.001));
        nmstd8 = mstd(find(abs(sminvalues-0.8)<0.001));

if isdeployed|1
figure('doublebuffer','off','Visible','Off');
else
figure;
end

ttl = [total(:,1)/std(total(:,1)) total(:,2)/std(total(:,2))];
[line1,line2,inters] = TwoLineClustering(ttl,[0 0],[0 1000]);
MSTDT = round(inters*std(total(:,1)));
display(sprintf('MSTDT = %f',MSTDT));     

for i=3:size(ls,1)
    name = ls(i).name;
    k = size(strfind(name,'stability.txt'),1);
    if(k>0)
        xx = load(strcat(folder,name));
        n = size(xx,1);
        plot(sort(xx,'descend'),'k-','Color',[0.4 0.4 0.4]); hold on;
        if(n==nmstd5)
            mstd5 = xx;
        end
        if(n==nmstd6)
            mstd6 = xx;
        end
        if(n==nmstd7)
            mstd7 = xx;
        end
        if(n==nmstd8)
            mstd8 = xx;
        end
        
    end
end

% plot(sort(mstd5,'descend'),'g-','LineWidth',5);
% plot(sort(mstd6,'descend'),'r-','LineWidth',5);
% plot(sort(mstd7,'descend'),'y-','LineWidth',5);
% plot(sort(mstd8,'descend'),'m-','LineWidth',5);

plot([MSTDT MSTDT],[0 1],'k--');

line1(1) = line1(1)*std(total(:,2));
line1(2) = line1(2)*std(total(:,2))/std(total(:,1));
line2(1) = line2(1)*std(total(:,2));
line2(2) = line2(2)*std(total(:,2))/std(total(:,1));

mx = -line2(1)/line2(2);

plot([0 max(nums)],[line1(1) line1(1)+max(nums)*line1(2)],'r--','LineWidth',3); hold on;
plot([0 mx],[line2(1) line2(1)+mx*line2(2)],'b--','LineWidth',3);

xlabel('Component rank'); ylabel('Component stability'); set(gca,'FontSize',16);

set(gcf,'Position',[0   100   600   500]);
ylim([0 1]);

text(max(nums)*0.7, 0.9,sprintf('MSTD=%i',MSTDT),'FontSize',20);

fn = sprintf('%s_MSTD_estimate.png',folder);
print('-dpng',fn,'-r300');

if isdeployed|1
figure('doublebuffer','off','Visible','Off');
else
figure;
end

%plot(nums(3:end),goodness(3:end),'ko-'); hold on;

% plot(nmstd5,goodness(find(nums==nmstd5)),'go','LineWidth',5);
% plot(nmstd6,goodness(find(nums==nmstd6)),'ro','LineWidth',5);
% plot(nmstd7,goodness(find(nums==nmstd7)),'yo','LineWidth',5);
% plot(nmstd8,goodness(find(nums==nmstd8)),'mo','LineWidth',5);

% xlabel('Number of components'); ylabel('Goodness of linear fit'); set(gca,'FontSize',16);
% gmax = find(goodness==max(goodness));
% plot([nums(gmax) nums(gmax)],[min(goodness(3:end)) 1],'r--');
% set(gcf,'Position',[400   300   400   300]);
% title(sprintf('Max goodness of fit = %i',nums(gmax)));
% 
% figure;

% plot(sminvalues,mstd,'mo-','LineWidth',2);
% for i=1:size(sminvalues,2)
%     text(sminvalues(i),mstd(i),sprintf('%i',mstd(i)));
% end
% xlabel('Smin'); ylabel('MSTD'); set(gca,'FontSize',16);
% set(gcf,'Position',[60    83   400   172]);
% 
% figure;
%bar(array(:,1),array(:,2)); hold on;
set(gcf,'Position',[600   100   600   500]);

plot(array(inds,1),array(inds,2),'b-','LineWidth',5); hold on;

% scatter(nmstd5,averageStability(find(nums==nmstd5)),'go','filled');
% scatter(nmstd6,averageStability(find(nums==nmstd6)),'ro','filled');
% scatter(nmstd7,averageStability(find(nums==nmstd7)),'yo','filled');
% scatter(nmstd8,averageStability(find(nums==nmstd8)),'mo','filled');


smin = 0.6;
plot([min(array(:,1)) max(array(:,1))],[smin smin],'k--');
smin = 0.5;
plot([min(array(:,1)) max(array(:,1))],[smin smin],'k--');
smin = 0.8;
plot([min(array(:,1)) max(array(:,1))],[smin smin],'k--');
smin = 0.7;
plot([min(array(:,1)) max(array(:,1))],[smin smin],'k--');

plot(array(inds,1),array(inds,3),'ro-'); hold on; 
plot([MSTDT MSTDT],[0 1],'k--');

%plot(array(:,1),array(:,4),'bs'); hold on; 
%plot(array(:,1),array(:,5),'g^'); hold on; 
title(sprintf('Average component stability for %s', folder)); ylim([0.3 1]);

xlabel('Effective dimension');
ylabel('Average component stability');

set(gca,'FontSize',16);

fn = sprintf('%s_AverageStability.png',folder);
print('-dpng',fn,'-r300');


done = 1;
doneF = sprintf('%s_done',folder);
save(doneF,'done','-ascii');

end

function [sminvalues, mstd] = computeMSTDProfile(nums,averageStability)
    avsorted = averageStability;
    k = 1;
    localmaxima = [];
    for i=2:size(avsorted,1)-1
        if((avsorted(i-1)<avsorted(i))&(avsorted(i+1)<avsorted(i)))
            localmaxima(k) = i;
            k = k+1;
        end
    end
    sminvalues = 0.5:0.02:0.8;
    mstd = zeros(1,size(sminvalues,2));
    for i=1:size(sminvalues,2)
        for k=1:size(localmaxima,2)
            if(avsorted(localmaxima(k))>sminvalues(i))
                mstd(i) = nums(localmaxima(k));
            end
        end
    end
end