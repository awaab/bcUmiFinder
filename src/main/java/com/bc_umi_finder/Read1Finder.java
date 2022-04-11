package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Read1Finder{
    private FastqIndexedParser fqp;
    private int threads;
    private ExecutorService executor;
    private boolean[] read1;
    private boolean[] read1Comp;
    private final String read1Str = "GATGTGCTGCATTGTAGAGTGT";
    private final String read1StrComp = "CTACACGACGCTCTTCCGATCT";
    private ResultReader resultReader;

    private int maxEditDistance;
    private BufferedWriter output;
    private BufferedWriter negOutput;

    public Read1Finder(String fastqFile, String resFile, int threads, int querySeqLen, int maxEditDistance) throws Exception {
        this.threads = threads;
        String fastqIndexFile = fastqFile + ".fai";
        System.out.println("loading fastq and index");
        this.fqp = new FastqIndexedParser(fastqFile, fastqIndexFile);
        System.out.println("loading fastq and index Done");
        
        this.read1 = Encoder.encode(read1Str.substring(0, querySeqLen));
        this.read1Comp = Encoder.encode(read1StrComp.substring(0, querySeqLen));
        this.maxEditDistance = maxEditDistance;

        this.resultReader = new ResultReader(resFile);
        // Creates a BufferedWriter
        this.output = new BufferedWriter(new FileWriter(fastqFile + ".read1.found.txt"));

        this.negOutput = new BufferedWriter(new FileWriter(fastqFile + ".read1.notFound.txt"));
    }



    public void find() throws InterruptedException, ExecutionException, IOException {
        int readsSoFar = 0;
        while (true) {
            this.executor = Executors.newFixedThreadPool(threads);
            boolean foundNull = false;
            String[][] reads = this.resultReader.next(threads);
            ArrayList<Future<ArrayList<Integer[]>>> resultList = new ArrayList<Future<ArrayList<Integer[]>>>();
            for (int i = 0; i < reads.length; i++) {
                if (reads[i] == null) {
                    foundNull = true;
                } else {
                    readsSoFar++;
                    char[] seq = new char[0];
                    seq = fqp.getTranscriptSequence(fqp.getfastqEntry(reads[i][0]));
                    boolean[] encodedSeq = Encoder.encode(seq);
                    boolean[][] searchSeqs = new boolean[1][];
                    if (reads[i][1].charAt(0) == '0') // Poly T
                        searchSeqs[0] = read1Comp;

                    else if (reads[i][1].charAt(0) == '1') // Poly A
                        searchSeqs[0] = read1;
                    int endIndex = Integer.parseInt(reads[i][2]);
                    SeqFindCallable callable = new SeqFindCallable(searchSeqs, encodedSeq, this.maxEditDistance, 0,
                            endIndex, true, false);
                    Future<ArrayList<Integer[]>> future = executor.submit(callable);
                    resultList.add(future);
                }
            }
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                System.out.println(e);

            }
            int iRead = 0;
            for (Future<ArrayList<Integer[]>> fut : resultList) {
                if (reads[iRead] == null) {
                    System.out.println("YAHTZI");
                }
                // this.output.write(Arrays.toString(seqs[iRead]));
                // this.output.write(seqs[iRead][seqs[iRead].length-1]);

                if (fut.get().size() == 0) {
                    this.negOutput.write(reads[iRead][0]+'\n');
                } else {
                    for (Integer[] i : fut.get())
                        this.output.write(Utils.arrToStr(reads[iRead]) +
                                " " + Utils.arrToStr(i) +'\n');
                }
                iRead++;
            }

            // if(foundNull || readsSoFar >= maxReads){
            if (foundNull) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String resFile = args[0];
        String fqFile = args[1];
        int numThreads = Integer.parseInt(args[2]);
        int querySeqLen;
        int maxEditDistance;
        try {
            querySeqLen = Integer.parseInt(args[3]);
        } catch (IndexOutOfBoundsException e) {
            querySeqLen = 22;
        }
        try {
            maxEditDistance = Integer.parseInt(args[4]);
        } catch (IndexOutOfBoundsException e) {
            maxEditDistance = 5;
        }
        Read1Finder rf = new Read1Finder(fqFile, resFile, numThreads, querySeqLen, maxEditDistance);
        // String []s = rfsf.next();
        rf.find();
    }
}
