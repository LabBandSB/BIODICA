function plotAverageStability(folder)

ls = dir(folder);

count = 1;

for i=3:size(ls,1)
    name = ls(i).name;
    k = size(strfind(name,'stability.txt'),1);
    if(k>0)
        xx = load(strcat(folder,name));
        array(count,1) = size(xx,1);
        array(count,2) = mean(xx);
        count = count+1;
    end
end

bar(array(:,1),array(:,2)); title(sprintf('Average component stability for %s', folder)); ylim([0 1]);
