function labels = loadGeneNames

fid = fopen('skin/genes');
i=1;
while 1
    tline = fgetl(fid);
    if ~ischar(tline), break, end
    labels{i} = tline;
    i=i+1;
end
