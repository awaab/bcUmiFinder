package com.bc_umi_finder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileSeqFinder {
    private FastqParser fqParser;
    private int threads;
    private boolean [][] searchSeqs;
    private int minEditDistance;
    private PrintStream outStream;
    private PrintStream negOutStream;
    private ExecutorService executor;
    public FileSeqFinder(String fileName, int threads, String [] searchSeqsStr, int minEditDistance) throws FileNotFoundException {
        this.fqParser = new FastqParser(fileName);
        this.threads = threads;
        this.searchSeqs = new boolean[searchSeqsStr.length][];
        for(int i =0; i<searchSeqs.length; i++){
            this.searchSeqs[i] = Encoder.encode(searchSeqsStr[i]);
        }
        this.minEditDistance = minEditDistance;
		this.outStream = new PrintStream(new FileOutputStream(fileName + "found.txt"));
        this.negOutStream = new PrintStream(new FileOutputStream(fileName + "notFound.txt"));
        for(int i=0; i<searchSeqsStr.length; i++){
            this.outStream.println("# query seqID "+i+" " + searchSeqsStr[i]);
        }
    }
    
    public void find(int startIndex,int endIndex) throws InterruptedException, ExecutionException{
        startIndex *= 2;
        endIndex *= 2;
        int readsSoFar = 0;
        while(true){
        this.executor = Executors.newFixedThreadPool(threads);
        boolean foundNull = false;
        String [][] seqs = fqParser.next(threads);
        ArrayList<Future<ArrayList<Integer[]>>> resultList = new ArrayList<Future<ArrayList<Integer[]>>>();
        for(int i = 0; i<seqs.length; i++){
            if(seqs[i] == null){
                foundNull = true;
            } else{
                readsSoFar++;
                //this.outStream.println(""+readsSoFar+" Reads so far: ");
                //this.outStream.println(seqs[i][2]);
            boolean [] encodedSeq = Encoder.encode(seqs[i][2]);
            SeqFindCallable callable = new SeqFindCallable(searchSeqs,encodedSeq,minEditDistance,startIndex,endIndex);
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
        int iSeq=0;
        for(Future<ArrayList<Integer[]>> fut : resultList) {
            //this.outStream.println(Arrays.toString(seqs[iSeq]));
            this.outStream.print("@read:");
            for(int i=0;i <seqs[iSeq].length - 1;i++){
                this.outStream.print(seqs[iSeq][i] + " ");
            };
            this.outStream.println();
            // this.outStream.println(seqs[iSeq][seqs[iSeq].length-1]);
            iSeq++;
            for(Integer[] i: fut.get()){
                String formattedIntString = Arrays.toString(i).toString()
                .replace(",", "") 
                .replace("[", "")  
                .replace("]", "")  
                .trim(); 
                this.outStream.println(formattedIntString);            }
        }
       
        //if(foundNull || readsSoFar >= maxReads){
        if(foundNull){
            break;
            }
        }
    }
    public void close() throws IOException{
        fqParser.close();
        outStream.close();
        negOutStream.close();
    }

}