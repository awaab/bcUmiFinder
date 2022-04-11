package com.bc_umi_finder;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SeqFindCallable implements Callable<ArrayList<Integer[]>> {
    private boolean[][] searchSeqs;
    private boolean[] seq;
    private int maxEditDistance;
    private int startIndex;
    private int endIndex;
    private boolean findOnlyOne;
    private boolean checkStart;

    SeqFindCallable(boolean[][] searchSeqs, boolean[] seq, int maxEditDistance, int startIndex, int endIndex,
            boolean findOnlyOne, boolean checkStart) {
        this.maxEditDistance = maxEditDistance;
        this.searchSeqs = searchSeqs;
        this.seq = seq;
        this.endIndex = endIndex * 2;
        this.startIndex = startIndex * 2;
        this.findOnlyOne = findOnlyOne;
        this.checkStart = checkStart;
    }

    @Override
    public ArrayList<Integer[]> call() throws Exception {
        ArrayList<Integer[]> result = new ArrayList<Integer[]>();
        for (int i = 0; i < searchSeqs.length; i++) {
            ArrayList<Integer[]> found;
            if (this.checkStart)
                found = SeqFinder.findSeqCheckStart(this.seq, searchSeqs[i], maxEditDistance, this.startIndex,
                        this.endIndex, this.findOnlyOne);
            else
                found = SeqFinder.findSeq(this.seq, searchSeqs[i], maxEditDistance, this.startIndex, this.endIndex,
                        this.findOnlyOne);
            for (Integer[] found_i : found) {
                Integer[] newFound = { i, found_i[0], found_i[1] };
                result.add(newFound);
            }
        }
        return result;
    }
}