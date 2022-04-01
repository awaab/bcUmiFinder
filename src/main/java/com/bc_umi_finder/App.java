package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 */
public final class App {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        String inFile = args[0];
        String queryInFile = args[1];
        int threads = Integer.parseInt(args[2]);
        int minEditDist = Integer.parseInt(args[3]);
        int startIndex = Integer.parseInt(args[4]);
        int endIndex = Integer.parseInt(args[5]);
        ///
        BufferedReader in = new BufferedReader(new FileReader(queryInFile));
        String str;

        List<String> searchSeqsList = new ArrayList<String>();
        while((str = in.readLine()) != null){
            searchSeqsList.add(str);
        }
        in.close();
        String[] searchSeqs = searchSeqsList.toArray(new String[0]);
        //"D:/BC_UMI_FIND/combined_sc2F2.pass.fastq";
  
        FileSeqFinder ffSeqFinder=new FileSeqFinder(inFile, threads, searchSeqs, minEditDist);
        ffSeqFinder.find(startIndex,endIndex);
        // FastqParser fq = new FastqParser(in);
        
        //System.out.println(Arrays.toString(fq.next()));
        //fq.close();
        //q.next();
        //fq.next();
        //fq.next();
        //String seq1 = RandDNA.randomDNAString(24);
        //String seq2 = RandDNA.randomDNAString(4);

        // String seq1 = "ATCGAACGATCCGCTTA";
        // String seq2 = "ATCG";
        
        // boolean[]  enc1 = Encoder.encode(seq1);
        // boolean[] enc2 = Encoder.encode(seq2);
        // System.out.println(Arrays.toString(enc1));
        // System.out.println(seq1);
        // // Instant start = Instant.now();
        // int dist = DistanceFinderStr.findDistance(seq1, seq2);
        // Instant end = Instant.now();
        // System.out.println(Duration.between(start, end));
        // System.out.println("The distance is " + dist);
        
        
        // Instant start1 = Instant.now();
        // ArrayList <Integer[]> dist = SeqFinder.findSeq(enc1, enc2, 2);
        // Instant end1 = Instant.now();
        // System.out.println(Duration.between(start1, end1));
        // //System.out.println("The distance is " + dist);
        // System.out.println(seq2);
        // for(Integer[] i: dist){
        //     System.out.println(i[0] + " " + i[1]+ " " + seq1.substring(i[0] / 2, i[0]/2+seq2.length()));
        // };
    }
    

    
}