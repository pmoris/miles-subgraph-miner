# SiGMAp - Significant subGraph Mining App

## Purpose of the tool

SiGMAp is a tool to find subgraphs in a single graph that are significantly associated with a given set of vertices. In other words, the goal is to search for subgraphs that are enriched in a chosen subset of vertices compared to the the graph as a whole.

Graph data is becoming more and more prevalent in various fields, including biology and bio-medicine. Subgroup discovery can provide valuable insight into these complex data structures.

This tool can handle a large variety of graph dataset types and vertex selections. It efficiently progresses through the search space, so that most problems should be computable.

The accompanying Java implementation should work on most regular-sized biological networks with any number of selected vertices and several node labels as demonstrated in the accompanying publications.

## Implementation

Three different algorithm implementations are provided in this tool:

1) the Significant Subgraph Miner (SSM) (or base) algorithm.
2) an adaptation of gSpan.
3) an adaptation of FSG.

The algorithms were adapted to run in the current problem domain and as well to run in both directed and undirected graphs, and single and multiple-label graphs too.

This implementation is provided for free for research purposes. Some bugs may be present within the software and no guarantees are given!

The included example datasets are for illustrative and testing purposes.

We would appreciate any comments, bug descriptions, suggestions or success stories regarding the tool.

## History of the algorithm

The provided Java implementation was originally created by Gerardo Orellana during his master thesis at Adrem Data Lab research group and further adapted by the research group.

The original Significant Subgraph Miner algorithm was implemented in Perl by Pieter Meysman and is still available at the Adrem Data Lab (UAntwerp) website or on bitbucket, alongside an example dataset: [http://adrem.ua.ac.be/sigsubgraph](http://adrem.ua.ac.be/sigsubgraph) & [https://bitbucket.org/pmeysman/sigsubgraphminer](https://bitbucket.org/pmeysman/sigsubgraphminer).

> P. Meysman, Y. Saeys, E. Sabaghian, W. Bittremieux, Y. Van de Peer, B. Goethals and K. Laukens. Discovery of Significantly Enriched Subgraphs Associated with Selected Vertices in a Single Graph. Proceedings of the 14th International Workshop on Data Mining in Bioinformatics (BioKDD15), Sydney, 2015.


## Getting started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

To get the project set and ready the following tools are necessary:

* [Apache Ant](http://ant.apache.org/) - The build tool for this project
* [Java](https://www.java.com/en/) - The Java language

### Installing

To build the project, run the following command on the root directory

```
ant all
```

## Command line options

The following parameters can be selected at the command line:

* `-h`/`--help` -> Shows these options.
* `-g`/`--graphs` -> Path to the graph file.
* `-l`/`--labels` -> Path to the labels file (optional).
* `-i`/`--interesting` -> Path to the interesting nodes/vertices file. For frequent subgraph mining, this should contain all vertices.
* `-b`/`--background` -> Path to a background file, a preselected reduced representation of the graph to search from.
* `-s`/`--support` -> Support threshold.
* `--singlelabel` -> Perform a single label run. Use this when all nodes in the network have exactly one label, e.g. for molecular structures encoded as graphs.
* `-u`/`--undirected` -> When present, runs analysis using an undirected configuration, e.g. where `A->B = B->A` and self-loops aren't allowed.
* `-p`/`--pvalue` -> Maximum P-value to  use (default = `0.05`).
* `-m`/`maxsize` -> Maximum number of vertices allowed.
* `-v`/`--verbose` -> Verbose option to print more intermediary output.
* `-n`/`--nestedpvalue` -> Run with a nested p-value configuration.
* `-o`/`--output` -> Output file location where significant motifs are stored.
* `-a`/`--algorithm` -> The algorithm to use, the options are: `base`, `gspan` and `fsg`.
* `--statistics` -> Display additional memory usage statistics.

## Running the example datasets

Three examples are provided along with this project in the folder dataset

* Example dataset

```
java -jar ./build/jar/subgraphmining.jar -g ../../datasets/example/example_graph.txt -l ../../datasets/example/example_labels.txt -p ../../datasets/example/example_vertexset.txt -s 2 -m 4 -a fsg -d
```

* PDB dataset

```
java -Xms64m -Xmx4096m -jar ./build/jar/subgraphmining.jar -g ./datasets/pdb/SSM_GR.txt -l ./datasets/pdb/SSM_LA.txt -p ./datasets/pdb/SSM_MN.txt -s 25 -m 2 -a gspan -i
```

* Yeast dataset

```
time java -Xms64m -Xmx16384m -jar ./build/jar/subgraphmining.jar -g ./datasets/yeast/yeastract_edges.txt -l ./datasets/yeast/yeast_gocat_mutiple.txt -p ./datasets/yeast/node_duplicate.txt -s 10 -m 4 -a base -o ./yeastS10M4Single.txt -t ./yeastS10M4SingleStats.txt
```

* Bacteria dataset

```
time java -Xms64m -Xmx16384m -jar ./build/jar/subgraphmining.jar -g ./datasets/bact/full_net.txt -p ./atasets/bact/phor.txt -b ./datasets/bact/tfs.txt -o ./bactS5M5.txt -t ./bactS5M5Stats.txt -m 5 -s 10 -a base
```

## Authors

* [Pieter Meysman - UAntwerpen](https://www.uantwerpen.be/nl/personeel/pieter-meysman/)
* [Gerardo Orellana](https://github.com/geraore)
* [Pieter Moris - UAntwerpen](https://www.uantwerpen.be/nl/personeel/pieter-moris/)
