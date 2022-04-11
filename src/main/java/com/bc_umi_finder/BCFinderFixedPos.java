package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BCFinderFixedPos {
    private boolean[][] bcWhiteListArr;
    private ResultReader resultReader;
    private int threads;
    private FastqIndexedParser fqp;
    private int maxEditDistance;
    private BufferedWriter output;
    private BufferedWriter negOutput;
    private ExecutorService executor;

    public BCFinderFixedPos(String bcFile, String resFile, String fastqFile, int threads, int maxEditDistance)
            throws Exception {
        this.threads = threads;
        this.maxEditDistance = maxEditDistance;
        String fastqIndexFile = fastqFile + ".fai";
        System.out.println("loading fastq and index");
        this.fqp = new FastqIndexedParser(fastqFile, fastqIndexFile);
        System.out.println("loading fastq and index Done");

        List<boolean[]> bcWhiteList = new ArrayList<boolean[]>();
        BufferedReader in = new BufferedReader(new FileReader(bcFile));
        String str;
        System.out.println("Reading BC whitelist...");
        while ((str = in.readLine()) != null) {
            bcWhiteList.add(Encoder.encode(str));
        }
        in.close();
        bcWhiteListArr = bcWhiteList.toArray(new boolean[bcWhiteList.size()][]);
        System.out.println("Reading BC whitelist Done...");
        this.resultReader = new ResultReader(resFile);

        this.output = new BufferedWriter(new FileWriter(fastqFile + ".BC.found.txt"));

        this.negOutput = new BufferedWriter(new FileWriter(fastqFile + ".BC.notFound.txt"));

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

                    int endIndex = Integer.parseInt(reads[i][2]) - 12; // polyT index - UMI len
                    int startIndex = Integer.parseInt(reads[i][2]) - 28; // polyT index - UMI len - BC len
                    
                    SeqFindCallable callable = new SeqFindCallable(this.bcWhiteListArr, encodedSeq,
                            this.maxEditDistance, startIndex,
                            endIndex, false, false);
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

                if (fut.get().size() == 0) {
                    this.negOutput.write(reads[iRead][0] + '\n');
                } else {
                    for (Integer[] i : fut.get())
                        this.output.write(Utils.arrToStr(reads[iRead]) +
                                " " + Utils.arrToStr(i) + '\n');
                }
                iRead++;
            }

            // if(foundNull || readsSoFar >= maxReads){
            if (foundNull) {
                break;
            }
        }
    }

    public static void main(String args[]) {
        String []args2 = {"D:/BC_UMI_FIND/737K-april-2014_rc.txt", "D:/BC_UMI_FIND/SC_pass_combined.fastq.polyA.found.txt", "D:/BC_UMI_FIND/SC_pass_combined.fastq", "4","2"};
        try {
            if (args.length < 5)
            args = args2;
            BCFinderFixedPos bcFinder = new BCFinderFixedPos(args[0],
                    args[1],
                    args[2],
                    Integer.parseInt(args[3]), 
                    Integer.parseInt(args[4]));
            bcFinder.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}