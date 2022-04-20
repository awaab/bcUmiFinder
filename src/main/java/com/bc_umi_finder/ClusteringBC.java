package com.bc_umi_finder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.bc_umi_finder.FastqIndexedParser.fastqIndexEntry;

public class ClusteringBC {
    private FastqIndexedParser fqp;
    private ResultReader resultReader;
    private String[] reads;
    private boolean[][] barcodes;
    private final int bcLength = 16;
    private final int umiLength = 16;

    public ClusteringBC(String fastqFile, String resultFile) throws Exception {
        this.fqp = new FastqIndexedParser(fastqFile, fastqFile + ".fai");
        this.resultReader = new ResultReader(resultFile);
        this.loadBCReads();

    }

    // getter dfor reads
    public String[] getReads() {
        return reads;
    }

    // gettr for barcodes
    public boolean[][] getBarcodes() {
        return barcodes;
    }

    public void loadBCReads() throws IOException {
        ArrayList<boolean[]> bcList = new ArrayList<boolean[]>();
        ArrayList<String> readList = new ArrayList<String>();
        while (true) {
            String[] readRecord = this.resultReader.next();
            if (readRecord == null) {
                break;
            }
            String readID = readRecord[0];
            int offset = this.bcLength;
            int polyAStart = Integer.parseInt(readRecord[2]);
            int start = polyAStart - (this.bcLength + this.umiLength);
            if (start < 0) {
                continue;
            }
            char[] bcSeq = fqp.getTranscriptSubSequence(fqp.getfastqEntry(readID), start, offset);
            boolean[] bcSeqEncoded = null;
            bcSeqEncoded = Encoder.encode(bcSeq);
            bcList.add(bcSeqEncoded);
            readList.add(readID);
        }
        this.barcodes = bcList.toArray(new boolean[bcList.size()][]);
        this.reads = readList.toArray(new String[readList.size()]);
    }

    public int[][] distanceMatrix() {
        int[][] distanceMatrix = new int[this.barcodes.length][this.barcodes.length];
        for (int i = 0; i < this.barcodes.length; i++) {
            for (int j = 0; j < this.barcodes.length; j++) {
                distanceMatrix[i][j] = DistanceFinder.findDistance(this.barcodes[i], this.barcodes[j], 0,
                        this.barcodes[i].length, Integer.MAX_VALUE);
            }
        }
        return distanceMatrix;
    }

    public void writeDistanceMatrix(String fileName) throws IOException {

        // get distance and write in a file
        BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(fileName + "distance.txt"));
        for (int i = 0; i < this.barcodes.length; i++) {
            for (int j = i + 1; j < this.barcodes.length; j++) {
                int dist = DistanceFinder.findDistance(this.barcodes[i], this.barcodes[j], 0,
                        this.barcodes[i].length, Integer.MAX_VALUE);
                bw.write("" + dist +" ");
            }
            bw.write("\n");

        }
        bw.close();

        String outFile2 = fileName + ".BC.reads";
        BufferedWriter writer2 = new BufferedWriter(new java.io.FileWriter(outFile2));
        for (int i = 0; i < this.getReads().length; i++) {
            writer2.write(this.getReads()[i] + "\n");
        }
        writer2.close();
        // write barcodes in result file
        String outFile3 = fileName + ".BC.barcodes";
        BufferedWriter writer3 = new BufferedWriter(new java.io.FileWriter(outFile3));
        for (int i = 0; i < this.getBarcodes().length; i++) {
            writer3.write(Arrays.toString(this.getBarcodes()[i]) + "\n");
        }

    }

    // main function
    public static void main(String[] args) throws Exception {
        String fastqFile = args[0];// "D:/BC_UMI_FIND/small.SC_pass_combined.fastq";
        String resultFile = args[1];// "D:/BC_UMI_FIND/small.SC_pass_combined.fastq.polyA.found.txt";
        ClusteringBC bcFinder = new ClusteringBC(fastqFile, resultFile);
        bcFinder.writeDistanceMatrix(fastqFile);

        

    }
}
