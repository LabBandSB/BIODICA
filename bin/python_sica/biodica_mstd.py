from os import listdir
from os.path import isfile, join
import sys
import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np

mpl.rcParams['font.size']=30
mpl.rcParams['xtick.major.size']=30

output_folder = ''

for i,arg in enumerate(sys.argv):
    if arg=='--output_folder':
        output_folder = sys.argv[i+1]

onlyfiles = [f for f in listdir(output_folder) if isfile(join(output_folder, f)) if 'stability.txt' in f]
fig, ax = plt.subplots(1 , 1 , figsize = (10, 11))
mean = []
orders = []
for f in onlyfiles:
    with open(output_folder+f,'r') as fin:
        stabs = fin.readlines()
        stabs = [float(s[:-1]) for s in stabs]
        stabs.sort(reverse=True)
    orders.append(len(stabs))
    mean.append(np.mean(np.array(stabs)))
    ax.plot(range(1 , len(stabs)+1) , stabs , 'k')
ax.set_title("Index stability",fontsize=40)
ax.set_xlabel("Number of components",fontsize=20)  
ax.set_ylim([0,1])
plt.savefig(output_folder  + '_MSTD_estimate.png')
#plt.show()

inds = np.argsort(orders)
fig, ax = plt.subplots(1 , 1 , figsize = (10, 11))
ax.plot(np.array(orders)[inds],np.array(mean)[inds],'bo-',markersize=10)
ax.set_title("Mean stability",fontsize=40)
ax.set_xlabel("Number of components",fontsize=20)
ax.set_ylim([0,1])
#plt.subplots_adjust(bottom=0.25)
plt.savefig(output_folder  + '_AverageStability.png')
#plt.show()