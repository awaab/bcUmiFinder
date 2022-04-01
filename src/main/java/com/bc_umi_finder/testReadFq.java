package com.bc_umi_finder;

public class testReadFq {
    public static void main(String [] args) throws Exception{
        System.out.println("Working");
        String fqFile = "D:/BC_UMI_FIND/SC_pass_combined.fastq";
        String indFile = "D:/BC_UMI_FIND/SC_pass_combined.fastq.fai";

        FastqIndexedParser fq = new FastqIndexedParser(fqFile,indFile);
        System.out.println(fq.getTranscriptSequence(fq.getfastqEntry("280b2cdf-4996-4e8f-9eaa-b842c4e79360")));
    }
    
}
