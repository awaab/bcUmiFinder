# bcUmiFinder
A pipeline for identifying cell barcodes and UMIs (unique moleculer identifiers) in ONT RNA long reads that were barcoded using the 10x Genomics platform to produce gene expression matrix.

## Steps
![Alt text](https://github.com/awaab/bcUmiFinder/blob/main/graph_bc_umi.PNG?raw=true)
* Find PolyA/PolyT in the expected location in the reads, and filter out reads that don't have PolyA/PolyT
* Get the barcode location relative to the PolyA/PolyT location, and create a list of barcodes.
* Cluster the barcodes using edit distance between barcodes in the distance matrix.
* Find UMIs relative to barcode location and cluster them in the same way the barcodes were clustered.
* Collapse reads with the same UMI found through clustering into one read to remove duplicates.
* Map the reads to the reference transcriptome and produce the gene expression matrix.
* Gene expression matrix can then be used for clustering and differential gene analysis.

## Tools
#### PolyAFinder.jar
Loops through the fastq file and finds where the polyA/polyT is while allowing a margin for error.
#### BCFinderFixedPos.jar
Given the result file of PolyAFinder.jar, the fastq file and its index file (fai), this tool produces the list of barcodes. A barcode is the 16BP length sequence found before 28BP of the PolyA/PolyT start.
#### ClusteringBC.jar
Calculates the distance matrix for barcode list generated by BCFinderFixedPos.jar and applies graph based clustering to group the similar barcodes.

#### UMIFinderFixedPos.jar
Given the result file of BCFinderFixedPos.jar, the fastq file and its index (fai), this tool produces the list of UMIs. A UMI is the 12BP length sequence found before thd start of the PolyA/PolyT.
#### ClusteringUMI.jar
Calculates the distance matrix for UMI list generated by UMIFinderFixedPos.jar and applies graph based clustering to group the similar UMIs.
