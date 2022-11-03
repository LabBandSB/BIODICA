import argparse
import logging
import os
from datetime import timedelta
from timeit import default_timer as timer

import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sica._whitening import whitening
from sica.base import StabilizedICA

# Set parameters for matplotlib
mpl.rcParams["font.size"] = 30
mpl.rcParams["xtick.major.size"] = 30


def main(arguments):
    # Config logging
    logging.basicConfig(
        format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
        level=logging.INFO,
        datefmt="%m/%d/%Y %I:%M:%S %p",
    )
    # Create a stability directory to save the results
    os.makedirs(arguments.output_folder, exist_ok=True)

    components = [int(s) for s in arguments.components.split(",")]

    # Load the data
    X = pd.read_csv(
        filepath_or_buffer=arguments.input_folder + arguments.input_file,
        header=None,
        index_col=None,
        sep="\s+",
    ).to_numpy()
    logging.info("X shape: %s", X.shape)
    prefix = arguments.input_file.replace("_numerical.txt", "")
    with open(arguments.input_folder + prefix + "_ids.txt", "r") as f:
        ids = f.readlines()
        ids = [s[:-1] for s in ids]
        logging.info("Number of objects: %s", len(ids))
    with open(arguments.input_folder + prefix + "_samples.txt", "r") as f:
        samples = f.readline().split("\t")[:-1]
        logging.info("Number of vars: %s", len(samples))

    # Whiten the data with the maximum number of components to avoid repeating this step for each number of components
    start = timer()
    logging.info("Whitening...")
    X_w, _ = whitening(
        X,
        n_components=components[-1],
        svd_solver="auto",
        chunked=False,
        chunk_size=None,
        zero_center=True,
    )
    end = timer()
    logging.info("Time elapsed for whitening: %s sec", timedelta(seconds=end - start))
    logging.info("Shape of the whitened matrix: %s", X_w.shape)

    # Iterate through the different numbers of components
    for i in components:
        start = timer()

        logging.info(
            "Computing ICA with Python, number of components = %s, algorithm = %s, non_linearity = %s",
            i,
            arguments.algorithm,
            arguments.non_linearity,
        )
        sICA = StabilizedICA(
            n_components=i,
            n_runs=arguments.n_runs,
            fun=arguments.non_linearity,
            algorithm=arguments.algorithm,
            max_iter=2000,
            whiten=False,
            n_jobs=-1,
        )
        sICA.fit(X_w[:, :i].T)
        A = sICA.transform(X.T)
        end = timer()
        logging.info("Time elapsed: %s sec", timedelta(seconds=end - start))

        fig, ax = plt.subplots(figsize=(10, 7))
        ax.plot(
            range(1, len(sICA.stability_indexes_) + 1), sICA.stability_indexes_, "ko-"
        )
        ax.set_title("Index stability, order=" + str(i))
        ax.set_xlabel("Number of components")
        ax.set_ylim([0, 1])
        fig.savefig(
            arguments.output_folder
            + arguments.input_file
            + "_"
            + str(i)
            + "_stability.png"
        )

        fig, ax = plt.subplots(figsize=(10, 7))
        start = timer()
        sICA.projection(ax=ax, method=arguments.type_of_visualization)
        logging.info("type_of_visualization: %s", arguments.type_of_visualization)
        if arguments.type_of_visualization == "umap":
            ax.set_title("UMAP for ICA components, order=" + str(i))
        elif arguments.type_of_visualization == "tsne":
            ax.set_title("tSNE for ICA components, order=" + str(i))
        elif arguments.type_of_visualization == "mds":
            ax.set_title("MDS for ICA components, order=" + str(i))
        end = timer()
        logging.info(
            "Time elapsed for visualization: %s sec", timedelta(seconds=end - start)
        )
        fig.savefig(
            arguments.output_folder + arguments.input_file + "_" + str(i) + ".png"
        )

        # Save the metagenes, the metasamples and the stability indexes
        name = "".join(arguments.input_file.split(".")[:-1]) + ".txt_" + str(i)
        np.savetxt(arguments.output_folder + "A_" + name + ".num", A, delimiter="\t")
        np.savetxt(
            arguments.output_folder + "S_" + name + ".num", sICA.S_.T, delimiter="\t"
        )
        np.savetxt(
            arguments.output_folder + name + "_stability.txt",
            np.flip(sICA.stability_indexes_).reshape((-1, 1)),
            delimiter="\t",
        )
    return


if __name__ == "__main__":
    args = argparse.ArgumentParser(description="biodica_sica")
    args.add_argument(
        "-c",
        "--components",
        default="5,10,20,30",
        type=str,
        help="list of number of ICA components to test",
    )
    args.add_argument("-nr", "--n_runs", default=100, type=int, help="number of runs")
    args.add_argument(
        "-a",
        "--algorithm",
        default="fastica_par",
        type=str,
        help="algorithm to use for solving ICA",
    )
    args.add_argument(
        "-nl", "--non_linearity", default="cube", type=str, help="non linearity"
    )
    args.add_argument(
        "-v",
        "--type_of_visualization",
        default="umap",
        type=str,
        help="type of visualization",
    )
    args.add_argument(
        "-ifolder",
        "--input_folder",
        default="C:/MyPrograms/BIODICA_GUI/",
        type=str,
        help="input folder",
    )
    args.add_argument(
        "-ifile",
        "--input_file",
        default="OVCA_ica_numerical.txt",
        type=str,
        help="input file",
    )
    args.add_argument(
        "-ofolder", "--output_folder", default=None, type=str, help="output folder"
    )

    main(args.parse_args())
