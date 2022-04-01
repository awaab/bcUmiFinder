package com.bc_umi_finder;

import java.util.Random;

public class RandDNA {
    public static String randomDNAString(int dnaLength) {
        Random rand = new Random();
        StringBuilder dna = new StringBuilder(dnaLength);
    
        for (int i = 0; i < dnaLength; i++) {
            dna.append("ACGT".charAt(rand.nextInt(4)));
        }
    
        return dna.toString();
    }
    
}
