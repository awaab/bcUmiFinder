package com.bc_umi_finder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class PolyAFinder {
    private FastqParser fqParser;
    private int threads;
    private boolean[][] searchSeqs;
    private int maxEditDistance;
    private ExecutorService executor;
    private BufferedWriter output;
    private BufferedWriter negOutput;
    String [] searchSeqsStr = {"TTTTTTTTTTTTTTTTTTT","AAAAAAAAAAAAAAAAAAA"};
    public PolyAFinder(String fileName, int threads, int maxEditDistance)
            throws IOException {
        this.fqParser = new FastqParser(fileName);
        this.threads = threads;
        this.searchSeqs = new boolean[searchSeqsStr.length][];
        for (int i = 0; i < searchSeqs.length; i++) {
            this.searchSeqs[i] = Encoder.encode(searchSeqsStr[i]);
        }
        this.maxEditDistance = maxEditDistance;

        this.output = new BufferedWriter(new FileWriter(fileName + ".polyA.found.txt"));

        this.negOutput = new BufferedWriter(new FileWriter(fileName + ".polyA.notFound.txt"));
        for (int i = 0; i < searchSeqsStr.length; i++) {
            this.output.write("# queryID " + i + " " + searchSeqsStr[i] + '\n');
        }
        this.output.write("# ReadID QueryID PosFound Dist"+'\n');
    }

    public void find(int startIndex, int endIndex) throws InterruptedException, ExecutionException, IOException {
        int readsSoFar = 0;
        while (true) {
            this.executor = Executors.newFixedThreadPool(threads);
            boolean foundNull = false;
            String[][] seqs = fqParser.next(threads);
            ArrayList<Future<ArrayList<Integer[]>>> resultList = new ArrayList<Future<ArrayList<Integer[]>>>();
            for (int i = 0; i < seqs.length; i++) {
                if (seqs[i] == null) {
                    foundNull = true;
                } else {
                    readsSoFar++;
                    // this.outStream.println(""+readsSoFar+" Reads so far: ");
                    // this.outStream.println(seqs[i][2]);
                    boolean[] encodedSeq = Encoder.encode(seqs[i][2]);
                    SeqFindCallable callable = new SeqFindCallable(searchSeqs, encodedSeq, maxEditDistance, startIndex,
                            endIndex, true, true);
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
            int iSeq = 0;
            for (Future<ArrayList<Integer[]>> fut : resultList) {
                // this.outStream.println(Arrays.toString(seqs[iSeq]));
                // this.outStream.println(seqs[iSeq][seqs[iSeq].length-1]);
                
                if (fut.get().size() == 0) {
                    this.negOutput.write(seqs[iSeq][0] + '\n');
                } else {
                    for (Integer[] i : fut.get()) {
                        String formattedIntString = Arrays.toString(i).toString()
                                .replace(",", "")
                                .replace("[", "")
                                .replace("]", "")
                                .trim();
                        this.output.write(seqs[iSeq][0]+" " + formattedIntString+'\n');
                    }
                }
                iSeq++;
            }

            // if(foundNull || readsSoFar >= maxReads){
            if (foundNull) {
                break;
            }
        }
    }

    public void close() throws IOException {
        fqParser.close();
        output.close();
        negOutput.close();
    }

    public static void main(String [] args) throws InterruptedException, ExecutionException, IOException{
        String inFile = args[0];
        int threads = Integer.parseInt(args[1]);
        int minEditDist = Integer.parseInt(args[2]);
        int startIndex = Integer.parseInt(args[3]);
        int endIndex = Integer.parseInt(args[4]);
        PolyAFinder seqFinder=new PolyAFinder(inFile, threads, minEditDist);
        seqFinder.find(startIndex,endIndex);
    }

}