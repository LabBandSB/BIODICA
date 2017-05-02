function [ broken_stick_dimension ] = brokenstickdim(eigen)

n = max(size(eigen,1),size(eigen,2));

distr=brokenstick_distribution(n);

broken_stick_dimension = 0;
for i=1:n
    if distr(i)>eigen(i)
        broken_stick_dimension = i; break;
    end
end


end

