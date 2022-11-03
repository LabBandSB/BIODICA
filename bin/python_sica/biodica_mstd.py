import argparse
import sys
from os import listdir
from os.path import isfile, join

import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np

mpl.rcParams["font.size"] = 30
mpl.rcParams["xtick.major.size"] = 30


def main(arguments):
    onlyfiles = [
        f
        for f in listdir(arguments.output_folder)
        if isfile(join(arguments.output_folder, f))
        if "stability.txt" in f
    ]

    fig, ax = plt.subplots(figsize=(10, 11))
    mean = []
    orders = []
    for f in onlyfiles:
        with open(arguments.output_folder + f, "r") as fin:
            stabs = fin.readlines()
            stabs = [float(s[:-1]) for s in stabs]
            stabs.sort(reverse=True)
        orders.append(len(stabs))
        mean.append(np.mean(np.array(stabs)))
        ax.plot(range(1, len(stabs) + 1), stabs, "k")
    ax.set_title("Index stability", fontsize=40)
    ax.set_xlabel("Number of components", fontsize=20)
    ax.set_ylim([0, 1])
    fig.savefig(arguments.output_folder + "_MSTD_estimate.png")

    inds = np.argsort(orders)
    fig, ax = plt.subplots(figsize=(10, 11))
    ax.plot(np.array(orders)[inds], np.array(mean)[inds], "bo-", markersize=10)
    ax.set_title("Mean stability", fontsize=40)
    ax.set_xlabel("Number of components", fontsize=20)
    ax.set_ylim([0, 1])
    fig.savefig(arguments.output_folder + "_AverageStability.png")
    return


if __name__ == "__main__":
    args = argparse.ArgumentParser(description="biodica_mstd")
    args.add_argument(
        "-ofolder", "--output_folder", default="", type=str, help="output folder"
    )

    main(args.parse_args())
