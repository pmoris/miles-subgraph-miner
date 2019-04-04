# SiGMAp - Significant subGraph Mining App

<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

<!-- code_chunk_output -->

* [SiGMAp - Significant subGraph Mining App](#sigmap-significant-subgraph-mining-app)
	* [Purpose of the tool](#purpose-of-the-tool)
	* [Getting started](#getting-started)
		* [Prerequisites](#prerequisites)
		* [Setup](#setup)
		* [Compiling from source](#compiling-from-source)
	* [How to use SiGMap](#how-to-use-sigmap)
		* [Input files](#input-files)
		* [Options](#options)
		* [Output](#output)
		* [Command line options](#command-line-options)
	* [Example datasets](#example-datasets)
	* [Implementation of subgraph discovery algorithm](#implementation-of-subgraph-discovery-algorithm)
	* [What is an associated subgraph and what are the statistics behind it?](#what-is-an-associated-subgraph-and-what-are-the-statistics-behind-it)
		* [Interestingness measure](#interestingness-measure)
		* [Hypergeometric distribution](#hypergeometric-distribution)
		* [Hypergeometric test](#hypergeometric-test)
		* [Equivalence to Fisher's exact test](#equivalence-to-fishers-exact-test)
			* [Multiple testing correction](#multiple-testing-correction)
	* [History of the algorithm](#history-of-the-algorithm)
	* [Authors](#authors)

<!-- /code_chunk_output -->

INSERT IMAGE HERE

## Purpose of the tool

SiGMAp is a Java tool to retrieve **subgraphs** (or patterns/motifs) from a single graph (or network) that are **significantly associated** with a given **set of vertices** (or nodes). In other words, the goal is to search for subgraphs that are enriched in a selected subset of vertices compared to the graph as a whole. A subgraph can either consist of a topological structure (e.g. a feed-forward/back loop in a regulatory network) or it can include relevant biological labels (e.g. a self-regulating transcription factor). For more information on associated subgraphs and how they are measured, please refer to the [statistics section](#what-is-an-enriched-subgraph-and-what-are-the-statistics-behind-it).

Graph data is becoming more and more prevalent in various fields, including biology and bio-medicine, and subgraph discovery methods can provide valuable insight into these, often large and complex, data structures.

SiGMAp should work on most regular-sized biological networks with any number of selected vertices. Nodes can be unlabelled or (multi)-labelled. It can deal with a large variety of data types, ranging from regulatory gene networks to protein-protein interaction networks and many other situation where graph data arises, since it does not rely on any specific type of input data beyond edge and label mappings in a simple flat text format.

The included [example datasets](#examples) are for illustrative and testing purposes.

This implementation is provided for free for research purposes. Some bugs may be present within the software and no guarantees are given!

We would appreciate any comments, bug descriptions, suggestions or success stories regarding the tool.

## Getting started

### Prerequisites

The standard JAR version of SiGMAp requires Java version 8 or higher. Installation instructions for various operating systems can be found on the [Java website](https://www.java.com/en/download/help/download_options.xml).

### Setup

The latest version of the runnable JAR file is available from our [release page]().

SiGMAp requires no true installation. The JAR file can be launched directly to open the GUI version of the tool. Alternatively, it can be invoked via the [command line](#command-line-options). The required input files and analysis options are described [below](#usage).

### Compiling from source

These instructions will give you a copy of the project on your local machine for development and testing purposes.

- Clone the repository from GitHub or download the source archive from the [release page]().
- Compile the source code via:
    - [Apache Ant](http://ant.apache.org/): `ant all`.
    - Or an IDE such as Eclipse (don't forget to add the external jar libraries in the `lib` directory and JUnit 5 to the classpath).

## How to use SiGMap

### Input files

- **Graph or network**: a space or tab separated text file where each line describes the edge between two nodes.
```
2	1
1	3
3	3
2	3
2	4
5	4
```

- **Nodes of interest**: a text file listing the names of the nodes of interest, one node per line.
```
2
5
4
```

- **Labels (optional)**: a space or tab separated text file where each line lists a node name followed by a label. Nodes with multiple labels should be split across multiple lines.
```
1	S
2	C
2	S
3	S
4	S
```

- **Background (optional)**: a text file listing the names of the nodes for the reduced background representation, one node per line. This option can be used to compare the occurrence of subgraphs in the selected set of nodes against a more limited background set, instead of the entire network. E.g. comparing a set of differentially expressed transcription factors against the entire set of transcription factors in a protein-protein interaction network.
```
6
7
10
15
```

### Options

Several analysis options can be selected:

- The type of subgraph discovery algorithm: `base`, `fsg` and `gSpan`. The `base` algorithm (described in the [original publication](#publication)) is recommended for most scenarios.
- The support threshold can be manually set or left blank, in which case an appropriate threshold will be selected automatically.
- The p-value cut-off (before multiple testing correction).
- The maximum size of subgraphs.
- A single label mode for graphs where each node has exactly one label (e.g. for molecular structures encoded as graphs).
- An undirected mode that does not consider directionality of edges.
- A nested p-value mode.

For further information on these options, please consult the [command line section](#command-line-options) of the documention.

### Output

describe example output and how to read it

give visualisation option

### Command line options

The following parameters can be selected on the command line:

* `-h`/`--help` -> Shows these options.
* `-g`/`--graph` -> Path to the graph file.
* `-l`/`--labels` -> Path to the labels file (optional).
* `-i`/`--interest` -> Path to the interesting nodes/vertices file. For frequent subgraph mining, this argument should be omitted (i.e. all nodes will be considered interesting).
* `-o`/`--output` -> Output file location where significant motifs are stored.
* `-b`/`--background` -> Path to a background file, a pre-selected reduced subset of the graph to which the selected nodes are compared (optional).
* `-s`/`--support` -> Support threshold the subgraphs must meet (support is defined as the number of instances of the subgraph in the selected subset or equivalently the number of valid source vertices in the set of interesting nodes). If this option is omitted a threshold will be calculated automatically as described in the [original publication](#publication).
* `-p`/`--pvalue` -> Maximum p-value to use (default = `0.05`).
* `-m`/`maxsize` -> Maximum number of vertices allowed.
* `--singlelabel` -> Perform a single label run. Use this when all nodes in the network have exactly one label, e.g. for molecular structures encoded as graphs.
* `-u`/`--undirected` -> When present, runs analysis using an undirected configuration, e.g. where `A->B = B->A` and self-loops aren't allowed.
* `-n`/`--nestedpvalue` -> Run with a nested p-value configuration.
* `-a`/`--algorithm` -> The algorithm to use, the options are: `base` (default), `gspan` and `fsg`.
* `-v`/`--verbose` -> Verbose option to print more intermediary output.
* `--statistics` -> Display additional memory usage statistics.

## Example datasets

Three examples are provided along with this project in the folder dataset

* Example dataset

```
java -jar ./build/jar/subgraphmining.jar --graph datasets/example/example_graph.txt --labels datasets/example/example_labels.txt --interest datasets/example/example_vertexset.txt --support 2 -maxsize 4 --algorithm fsg --verbose
```

* PDB dataset

```
java -Xms64m -Xmx4096m -jar ./build/jar/subgraphmining.jar --graph ./datasets/pdb/SSM_GR.txt --labels ./datasets/pdb/SSM_LA.txt --interest ./datasets/pdb/SSM_MN.txt --support 25 --maxsize 2 --algorithm gspan --singlelabel
```

* Yeast dataset

```
time java -Xms64m -Xmx16384m -jar ./build/jar/subgraphmining.jar --graph ./datasets/yeast/yeastract_edges.txt --labels ./datasets/yeast/yeast_gocat_mutiple.txt --interest ./datasets/yeast/node_duplicate.txt --support 10 --maxsize 4 --algorithm base --statistics ./yeastS10M4SingleStats.txt --output ./yeastS10M4Single.txt
```

* Bacteria dataset

```
time java -Xms64m -Xmx16384m -jar ./build/jar/subgraphmining.jar --graph ./datasets/bact/full_net.txt --interest ./datasets/bact/phor.txt --background ./datasets/bact/tfs.txt --maxsize 5 --support 10 --algorithm base --output ./bactS5M5.txt --statistics ./bactS5M5Stats.txt
```

## Implementation of subgraph discovery algorithm

Three different algorithm implementations are provided in this tool:

1) the Significant Subgraph Miner (SSM) (or base) algorithm.
2) an adaptation of gSpan.
3) an adaptation of FSG.

The algorithms were adapted to run in the current problem domain and as well to run in both directed and undirected graphs, and single and multiple-label graphs too.

## What is an associated subgraph and what are the statistics behind it?

### Interestingness measure

The aim of the significant subgraph mining algorithm is to **retrieve those subgraphs in a graph that are significantly associated with a specific set of nodes or vertices**. More specifically, given a set of nodes of interest $n$, are there subgraph patterns $S$ that contain vertices in $n$ more often than expected compared to the un-selected vertices in the rest of the graph, or in other words, are the nodes of interest $n$ enriched for a specific subgraph pattern $S$? Note that the concept of _"subgraph patterns that contain a vertex"_ is more strictly defined as _"a vertex being the source vertex of an instance of a specific subgraph pattern"_. An example is given below:

![](./enriched_subgraph_example.svg)

Suppose this toy graph represents a regulatory network. The vertex shape denotes the label of the proteins (e.g. the type of regulator: [] = transcription factors, o = kinases). The light green vertices were selected as the set of interest, based on some criteria (e.g. they are known to be involved in a specific disease). The goal is to find subgraph patterns (or motifs) that are specific to the disease-associated proteins. Upon closer inspection of the network, it becomes clear that the pattern where a kinase regulates a transcription factor, which in turn self-regulates, occurs for all light green vertices. Since it only occurs once in the rest of the network, we can conclude that this motif is associated with the light green disease-associated proteins.

### Hypergeometric distribution

To more formally answer this question of association, the **hypergeometric distribution** is utilised. A classic example of this distribution is the process of drawing marbles from an urn that contains black and white marbles. Drawing a white marble is defined as a success, and a black one as a failure. This is similar to the binomial distribution, with the key difference that the marbles are not placed back into the urn after each draw. This is called _sampling without replacement_.

The hypergeometric distribution arises in various situations where we are interested in two different aspects of our data, e.g. the colour of the marbles and the process of drawing them. Or more generally, _the representation (draws) of specific sub-populations (colours) in a sample_.

A well known biological example is the GO enrichment test for a set of differentially expressed genes. The urn with marbles now represents all measured genes and the colours represent a gene either being differentially expressed (white) or not (black). The drawing of marbles is now analogous to specific genes being annotated with a specific GO term. We are interested in the proportion of annotated genes in the set of differentially expressed genes, compared to the rest of the genes.
<!-- Here we are interested in the proportion of genes that are annotated with a specific GO term (drawing marbles), in the set of differentially expressed genes (white marbles), compared to the background set, i.e. the remaining measured genes (black marbles). -->

Similarly, this distribution also describes our enriched subgraph situation. In this case, source vertices for instances of the subgraph pattern $S$ are discovered (_the process of drawing marbles_) in a graph containing both vertices of interest (_white marbles_) and background vertices (_black marbles_).
<!-- drawing vertices (i.e. discovering instances of the subgraph patterns $S$) from a graph containing both vertices of interest (_white marbles_) and unselected/background vertices (_black marbles_). -->

The probability of such a sample is given by the probability mass function of the hypergeometric distribution:

$$pmf: \Pr(X = k) = \frac{\binom{K}{k} \binom{N - K}{n-k}}{\binom{N}{n}}$$

It describes the probability of observing $k$ successes (_i.e. a selected/interesting vertex being the source vertex for a given subgraph pattern $S$_) in $n$ random draws (_i.e. the number of instances of a subgraph $S$_), _without replacement_, from a population of size $N$ (_i.e. the size of the entire graph_) that contains $K$ successes in total (_i.e. the number of selected vertices_).

### Hypergeometric test

The **hypergeometric test** can then be used to calculate the statistical significance of a specific configuration of observations. Going back to the marble example, we can expect that drawing five or more white marbles out of ten draws is rather unlikely if the urn only contained ten white marbles and fifty black ones to begin with. White marbles are over-represented in the final sample.

In the subgraph setting, are the instances of the subgraph pattern $S$ over-represented in the selected vertices of interest (or are the selected vertices enriched with subgraph instances)?

The test answers the question: **are the instances of the subgraph pattern $S$ over-represented in the selected vertices of interest _or_ are the selected vertices enriched with subgraph instances?**

> **DISCLAIMER**: technically this is also equivalent to the following question: are the selected vertices of interest over-represented in the discovered source vertex instances of the subgraph pattern $S$? This way of looking at things is more in line with the way the probability mass function was defined earlier and is analogous to checking whether a specific colour of marble is over-represented in a draw (high proportion of $K$).
>
> The test does not discern between the direction (or causality) of the two aspects of the data we are considering (selection and occurrence of subgraph instances).

The p-value ($X >= k$) thus gives the probability that we discover $k$ or more selected vertices as source vertices of $S$, under the null hypothesis that all vertices in the graph have the same probability of being a source vertex (_~ the source vertices are uniformly distributed_). If the p-value falls below our chosen significance level, we can reject the null hypothesis and can conclude that the selected vertices are enriched for the subgraph pattern $S$.

### Equivalence to Fisher's exact test

The hypergeometric test is equivalent to a **one-tailed Fisher's exact test** for the following two-way contingency table:

|   |**Vertices of interest**|**Background set**||
|---|---|---|---|
|**Source vertices for the subgraph $S$**|$k$|$n-k$|$n$|
|**Non-source vertices**|$K-k$|$N-K-n+k$|$N-n$|
||$K$|$N-K$|$N$|

The null hypothesis is then defined as there being no association between a vertex belonging to the selected set of interest (_i.e. the marble colour_) and a vertex being a source vertex for an instance of the subgraph pattern $S$ (_i.e. being drawn from the urn_). A low p-value indicates that it is unlikely to obtain $k$ or more source vertices in the selected vertices $K$ if the occurrence of the subgraph pattern is independent from the selected vertices.

<!-- the probability of $k$ successes (i.e. a selected vertex of interest being the source vertex for a given subgraph pattern $S$) in $n$ random draws (i.e. the number of selected vertices), _without replacement_, from a population of size $N$ (i.e. the size of the entire graph) that contains $K$ successes (i.e. the number of source vertices/discovered subgraphs $S$). -->

#### Multiple testing correction

Note that the test as described above applies to the situation where we are comparing the distribution of instances of _one specific subgraph pattern_ $S$ across the graph. A separate test will be conducted for different unique subgraph patterns. Because of this, a multiple testing correction procedure is required. For this, the (rather conservative) **Bonferroni** method is employed, which retains the desired family-wise error rate by testing each individual hypothesis at a significance level of $\frac{\alpha}{m}$. In our case, the selected p-value threshold will be divided by the number of subgraph patterns that are tested.

## History of the algorithm

The provided Java implementation was worked on by Gerardo Orellana during his master thesis at the Adrem Data Lab research group of the UAntwerp, and further adapted by Pieter Moris and Danh Bui-Thi.

The original Significant Subgraph Miner algorithm was implemented in Perl by Dr. Pieter Meysman and is still available at the Adrem Data Lab website and on BitBucket, alongside an example dataset: [http://adrem.ua.ac.be/sigsubgraph](http://adrem.ua.ac.be/sigsubgraph) & [https://bitbucket.org/pmeysman/sigsubgraphminer](https://bitbucket.org/pmeysman/sigsubgraphminer).

<a name="publication"></a>
<!-- > P. Meysman, Y. Saeys, E. Sabaghian, W. Bittremieux, Y. Van de Peer, B. Goethals and K. Laukens. Discovery of Significantly Enriched Subgraphs Associated with Selected Vertices in a Single Graph. Proceedings of the 14th International Workshop on Data Mining in Bioinformatics (BioKDD15), Sydney, 2015. -->
> Meysman, Pieter, Yvan Saeys, Ehsan Sabaghian, Wout Bittremieux, Yves van de Peer, Bart Goethals, and Kris Laukens. 2016. “Mining the Enriched Subgraphs for Specific Vertices in a Biological Graph.” IEEE/ACM Transactions on Computational Biology and Bioinformatics, 1–1. https://doi.org/10.1109/TCBB.2016.2576440.

## Authors

* [Dr. Pieter Meysman - UAntwerpen](https://www.uantwerpen.be/en/staff/pieter-meysman/)
* [Pieter Moris - UAntwerpen](https://www.uantwerpen.be/en/staff/pieter-moris/)
* [Danh Bui-Thi - UAntwerpen](https://www.uantwerpen.be/en/staff/danh-bui-thi/)
* [Gerardo Orellana](https://github.com/geraore)
