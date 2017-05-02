function [line1,line2,intersection,i1,i2] = TwoLineClustering(x,initialLine1,initialLine2)
% line is defined as [a b], a - intercept, b - slope

    l1 = initialLine1;
    l2 = initialLine2;
    
    eps = 1000;
    while(eps>0.001)
        l1m = l1;
        [i1,i2] = SplitByLineProximity(x,l1,l2);
        [v,u,s] = pca(x(i1,:),'NumComponents',1);
        l1 = convertPrincipalVector2Line(mean(x(i1,:)),v);
        [v,u,s] = pca(x(i2,:),'NumComponents',1);
        l2 = convertPrincipalVector2Line(mean(x(i2,:)),v);
        eps = norm(l1m-l1)/norm(l1);
        %display(sprintf('%f',eps));
    end

%     [i1,i2] = SplitByLineProximity(x,l1,l2);
%     plot(x(i1,1),x(i1,2),'ro'); hold on; plot(x(i2,1),x(i2,2),'bo');
%     plot(x(i1,1),l1(1)+l1(2)*x(i1,1),'r.');  
%     plot(x(i2,1),l2(1)+l2(2)*x(i2,1),'b.');
    
    line1 = l1;
    line2 = l2;
    intersection = (l1(1)-l2(1))/(l2(2)-l1(2));
end

function [i1, i2] = SplitByLineProximity(x,l1,l2)
    i1 = [];
    i2 = [];
    for i=1:size(x,1)
        d1 = Distance2Line(x(i,:),l1);
        d2 = Distance2Line(x(i,:),l2);
        if(d1<d2)
            i1(size(i1,2)+1) = i;
        else
            i2(size(i2,2)+1) = i;
        end
    end
end

function dist = Distance2Line(point,line)
    [c v] = convertLine2Vector(line);
    t = sum((point-c).*v);
    proj = c+t*v;
    dist = norm(point-proj);
end

function line = convertPrincipalVector2Line(mn,v)
    a = mn(2)-v(2)/v(1)*mn(1);
    b = v(2)/v(1);
    line = [a b];
end

function [c,v] = convertLine2Vector(line)
    a = line(1);
    b = line(2);
    v = [0,0];
    v(2) = b/sqrt(1+b*b);
    v(1) = sqrt(1-v(2)*v(2));
    c = [0 a];
end