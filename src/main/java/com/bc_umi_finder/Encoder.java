package com.bc_umi_finder;


public class Encoder {
    // T is compliment to A
    // C is compliment co G
    static final boolean [] T = {true,false};
    static final boolean [] A = {false,true};
    static final boolean [] C = {true,true};
    static final boolean [] G = {false,false};
    // function that converts neuclotide to binary digits
    public static boolean[] encode(String sequence) {
        boolean[] binary = new boolean[sequence.length() * 2];
        for (int i = 0; i < sequence.length(); i++) {
            boolean [] Neucl = new boolean[2];
            switch (sequence.charAt(i)) {
                case 'A':
                    Neucl = A;
                    break;
                case 'C':
                    Neucl = C;
                    break;
                case 'G':
                    Neucl = G;
                    break;
                case 'T':
                    Neucl = T;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid character in sequence: "+sequence.charAt(i));
            }
            binary[2*i] = Neucl[0];
            binary[2*i+1] = Neucl[1];
        }
        return binary;
    }
    //
    public static boolean[] encode(char [] sequence) {
        boolean[] binary = new boolean[sequence.length * 2];
        for (int i = 0; i < sequence.length; i++) {
            boolean [] Neucl = new boolean[2];
            switch (sequence[i]) {
                case 'A':
                    Neucl = A;
                    break;
                case 'C':
                    Neucl = C;
                    break;
                case 'G':
                    Neucl = G;
                    break;
                case 'T':
                    Neucl = T;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid character in sequence: "+sequence[i]);
            }
            binary[2*i] = Neucl[0];
            binary[2*i+1] = Neucl[1];
        }
        return binary;
    }
    public static boolean[] complement(boolean[] sequence) {
        boolean[] comp = new boolean[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            comp[i] = !sequence[i];
        }
           return comp;
    }


}
