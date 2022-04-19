package com.bc_umi_finder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.bc_umi_finder.FastqIndexedParser.fastqIndexEntry;

public class ClusteringBC {
    private FastqIndexedParser fqp;
    private ResultReader resultReader;
    private String [] reads;
    private boolean [][] barcodes;
    private final int bcLength = 16;
    private final int umiLength = 16;
    public ClusteringBC(String fastqFile, String resultFile) throws Exception{
        this.fqp = new FastqIndexedParser(fastqFile, fastqFile + ".fai");
        this.resultReader = new ResultReader(resultFile);
        this.loadBCReads();
        
    }
    public void loadBCReads() throws IOException{
        ArrayList<boolean[]> bcList =  new ArrayList<boolean[]>();
        ArrayList<String> readList =  new ArrayList<String>();
        while(true){
            String[] readRecord = this.resultReader.next();
            if(readRecord == null){
                break;
            }
            String readID = readRecord[0];
            int offset = this.bcLength;
            int polyAStart = Integer.parseInt(readRecord[2]);
            int start  =  polyAStart - (this.bcLength + this.umiLength);
            char [] bcSeq = fqp.getTranscriptSubSequence(fqp.getfastqEntry(readID),start,offset);
            boolean [] bcSeqEncoded = Encoder.encode(bcSeq);
            bcList.add(bcSeqEncoded);
            readList.add(readID);
        }
        this.barcodes = bcList.toArray(new boolean[bcList.size()][]);
        this.reads = readList.toArray(new String[readList.size()]);
    }
    public int [][] distanceMatrix(){
        int [][] distanceMatrix = new int[this.barcodes.length][this.barcodes.length];
        for(int i = 0; i < this.barcodes.length; i++){
            for(int j = 0; j < this.barcodes.length; j++){
                distanceMatrix[i][j] = DistanceFinder.findDistance(this.barcodes[i], this.barcodes[j], 0, this.barcodes[i].length, Integer.MAX_VALUE);
            }
        }
        return distanceMatrix;
    }
    // main function
    public static void main(String[] args) throws Exception {
        String fastqFile = args[0];// "D:/BC_UMI_FIND/small.SC_pass_combined.fastq";
        String resultFile = args[1];// "D:/BC_UMI_FIND/small.SC_pass_combined.fastq.polyA.found.txt";
        ClusteringBC bcFinder = new ClusteringBC(fastqFile, resultFile);
        int [][] distanceMatrix = bcFinder.distanceMatrix();
        
        // write matrix in result file
        String outFile = fastqFile + ".BC.matrix";
        BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(outFile));
        for(int i = 0; i < distanceMatrix.length; i++){
            for(int j = 0; j < distanceMatrix[i].length; j++){
                writer.write(distanceMatrix[i][j] + "\t");
            }
            writer.write("\n");
        }
        writer.close();
    }
}
