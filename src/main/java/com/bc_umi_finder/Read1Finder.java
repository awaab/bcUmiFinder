package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Read1Finder {
    private FastqIndexedParser fqp;
    private BufferedReader resReader;
    private int threads;
    private ExecutorService executor;
    private boolean[] read1;
    private boolean[] read1Comp;
    private final int querySeqLen = 10;
    private final String read1Str = "GATGTGCTGCATTGTAGAGTGT";
    private final String read1StrComp = "CTACACGACGCTCTTCCGATCT";
    private final int minEditDistance = 3;
    private PrintStream outStream;
    private PrintStream negOutStream;

    public Read1Finder(String fastqFile, String resFile, int threads) throws Exception {
        this.threads = threads;
        String fastqIndexFile = fastqFile + ".fai";
        System.out.println("loading fastq and index");
        this.fqp = new FastqIndexedParser(fastqFile, fastqIndexFile);
        System.out.println("loading fastq and index Done");
        this.resReader = new BufferedReader(new FileReader(resFile));
        this.read1 = Encoder.encode(read1Str.substring(0, querySeqLen));
        this.read1Comp = Encoder.encode(read1StrComp.substring(0, querySeqLen));

        this.outStream = new PrintStream(new FileOutputStream(fastqFile + ".read1.found.txt"));
        this.negOutStream = new PrintStream(new FileOutputStream(fastqFile + ".read1.notFound.txt"));
    }

    public String[][] next(int n) throws IOException {
        String[][] res = new String[n][];
        for (int i = 0; i < n; i++) {
            res[i] = next();
        }
        return res;
    }

    public String[] next() throws IOException {
        String line;

        while (true) {
            // System.out.println("=");
                line = resReader.readLine();
                if(line==null)return null;
                if (line.charAt(0) == '#') {
                    continue;
                }

                return line.split("\\s+");

 
        }
    }

    public void find() throws InterruptedException, ExecutionException, IOException {
        int readsSoFar = 0;
        while (true) {
            this.executor = Executors.newFixedThreadPool(threads);
            boolean foundNull = false;
            String[][] reads = next(threads);
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
                    SeqFindCallable callable = new SeqFindCallable(searchSeqs, encodedSeq, this.minEditDistance, 0,
                            endIndex);
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
                // this.outStream.println(Arrays.toString(seqs[iRead]));
                // this.outStream.println(seqs[iRead][seqs[iRead].length-1]);

                if (fut.get().size() == 0) {
                    this.negOutStream.println(reads[iRead][0]);
                } else {
                    for (Integer[] i : fut.get())
                        this.outStream.println(Utils.arrToStr(reads[iRead]) +
                                " " + Utils.arrToStr(i));
                }
                iRead++;
            }

            // if(foundNull || readsSoFar >= maxReads){
            if (foundNull) {
                break;
            }
        }
    }

    public static void main(String [] args) throws Exception {
        String resFile = args[0];
        String fqFile = args[1];
        int numThreads = Integer.parseInt(args[2]);
        Read1Finder rf = new Read1Finder(fqFile, resFile, numThreads);
        //String []s  = rfsf.next();
        rf.find();
    }
}
