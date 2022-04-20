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
    // get mean of int array
    public static double getMean(double[] arr) {
        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum / arr.length;
    }
    
    // convert phred array to probability
    public static double[] phredToProb(int[] arr) {
        double[] prob = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            prob[i] = Math.pow(10, -arr[i] / 10.0);
        }
        return prob;
    }

    // convert probability value to phred
    public static double probToPhred(double prob) {
        return (-10 * Math.log10(prob));
    }



    public static void main(String[] args) throws Exception {
        String read0 = "TTGTACTTCGTTCAAGTTACGTATTGCTCTACACGACGCTCTTCCGATCTCCGTGAAGTCCCATGCATTAGCTTTATTTTTTTTTTTTTTTTTTTTTTTTTTTGGGATTGACTCACGGCTGTCTCTTATACACATCTGACCCCATGTACTCTGCGTTGACCACCACTGCTTCCCATGTACTCTGCGTTGATACCACTGCTTCCCATGTACTCTGCGTTGA";
        String read2 = "TTGTACTTCGTTCAGTTACGTATTGCTCTACACGACGCTCTTCCGATCTTACCGCAGCAAGAGTGCATGTTATTAAGATTTTTTTTTTTTTTTTTTTTTTTTTTTTTTAGAGTTTCATTTTTACAGAGAAGACACAAGGTAAAGAAAGACAACCACTTTGGAGAGGGATCATGGTGAGGGCAATGTCACTGTCATGGCCATAGTACAAGCCTTCCAAGAAAAGGGACCTGCGTTATTCCTCACAAGCTGCCCCTGTCCCACCCCATGTACTCTGCGTTGATACCACCCCATGTACTCTGCGTTGATACCACTGCTTCCCATGTACTCTGCGTT";
        String bc0 = read0.substring(72-(12+16),72-(12));
        String bc2 = read2.substring(78-(12+16),78-(12));
        boolean enc0[] = Encoder.encode(bc0);
        boolean enc2[] = Encoder.encode(bc2);
        int dist = DistanceFinder.findDistance(enc0, enc2,0,enc0.length,200); 
        System.out.println(dist);
        System.out.println(bc0);
        System.out.println(bc0.length());
        System.out.println(bc2);
        // FastqIndexedParser fqParser = new FastqIndexedParser("D:/BC_UMI_FIND/small.SC_pass_combined.fastq","D:/BC_UMI_FIND/small.SC_pass_combined.fastq.fai");
        // String readId = "3155ca9f-91ae-4646-9f4b-4a656785c384";
        // fqParser.getfastqEntry(readId);
        // char [] seq = fqParser.getTranscriptSubSequence(fqParser.getfastqEntry(readId),10,10);
        // // print char array
        // System.out.println(Arrays.toString(seq));

        // char [] seqBig = fqParser.getTranscriptSequence(fqParser.getfastqEntry(readId));
        // // print char array
        // System.out.println(Arrays.toString(seqBig));
        



        // int [] qual = fqParser.getTranscriptQual(fqParser.getfastqEntry(readId));
        // // print char array
        // // 13.225380
        // System.out.println(Arrays.toString(seq));
        // // System.out.println(Arrays.toString(qual));
        // // print length of seq and qual
        // System.out.println(seq.length);
        // System.out.println(qual.length);
        // // subtract 31 from all qual values
        // for (int i = 0; i < qual.length; i++) {
        //     qual[i] -= 33;
        // }
        // System.out.println(Arrays.toString(qual));
        // double [] qualD = phredToProb(qual);
        // System.out.println(Arrays.toString(qualD));
        // System.out.println(probToPhred(getMean(qualD)));
        // get mean of array


        // String seq1 = "GGTGTACTTCGTTCAGTTACGTATTGCTCTACACGGCCTCTTCCGATCTTTAACGTCGAGCTGACTATTACGCTTTTTTTTTTTTTTTTTTTTTTTAGTAAAACTGAGATACTTATGAAAAAAGCCAAAATTCTTAAATGGTTGGGGGTTGGGTTTAAACAACTGCCTCAACATACGGAAACACAGGTCTTATGATACTTTACTTACCCCATGTACTCTACGTTGATACCACTGCTTAACCAATACGTAAC";
        // String seq2 = "TTTTTTTTTTTTTTTTTT";
        // boolean[]  enc1 = Encoder.encode(seq1);
        // boolean[] enc2 = Encoder.encode(seq2);
        // // int dist = DistanceFinder.findDistance(enc1, enc2,0,enc1.length,10);
        // ArrayList<Integer[]> findSeq = SeqFinder.findSeqCheckStart(enc1, enc2, 3, 0, 200,true);
        // // print arraylist of integer arrays
        // for (Integer[] i : findSeq) {
        //     System.out.println(Arrays.toString(i));
        //     int start = i[0];
        //     System.out.println(seq1.substring(start));
        // }
        // String inFile = args[0];
        // String queryInFile = args[1];
        // int threads = Integer.parseInt(args[1]);
        // int minEditDist = Integer.parseInt(args[2]);
        // int startIndex = Integer.parseInt(args[3]);
        // int endIndex = Integer.parseInt(args[4]);
        ///
        //BufferedReader in = new BufferedReader(new FileReader(queryInFile));
        //String str;

        //List<String> searchSeqsList = new ArrayList<String>();
        //while((str = in.readLine()) != null){
        //    searchSeqsList.add(str);
        //}
        //in.close();
        //String[] searchSeqs = searchSeqsList.toArray(new String[0]);
        //"D:/BC_UMI_FIND/combined_sc2F2.pass.fastq";
  
        // PolyAFinder ffSeqFinder=new PolyAFinder(inFile, threads, minEditDist);

        // ffSeqFinder.find(startIndex,endIndex);
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