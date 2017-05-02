function [distr] = brokenstick_distribution(dim)

for i=1:dim
    distr(i)=0;
    for j=i:dim
        distr(i)=distr(i)+1/j;
    end
    distr(i)=distr(i)/dim;
end

end

