package com.bc_umi_finder;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SeqFindCallable implements Callable< ArrayList<Integer[]>> {
    private boolean[][] searchSeqs;
    private boolean [] seq;
    private int minEditDistance;
    private int startIndex;
    private int endIndex;
    SeqFindCallable(boolean[][] searchSeqs, boolean [] seq, int minEditDistance, int startIndex, int endIndex) {
        this.minEditDistance = minEditDistance;
        this.searchSeqs = searchSeqs;
        this.seq = seq;
        this.endIndex = endIndex;
        this.startIndex = startIndex;
    }
    @Override
    public ArrayList<Integer[]> call() throws Exception {
        ArrayList<Integer[]> result = new ArrayList<Integer[]>();
        for (int i = 0; i < searchSeqs.length; i++){
            ArrayList <Integer[]> found = SeqFinder.findSeq(this.seq, searchSeqs[i], minEditDistance, this.startIndex, this.endIndex);
            for (Integer[] found_i: found){
                Integer [] newFound  = {i,found_i[0],found_i[1]};
                result.add(newFound);
            }
        }
        return result;
    }
}