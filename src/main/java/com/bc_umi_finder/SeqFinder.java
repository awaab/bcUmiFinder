package com.bc_umi_finder;

import java.util.ArrayList;

public class SeqFinder {
    public static ArrayList<Integer[]> findSeq(boolean[] longSeq, boolean[] shortSeq, int maxEditDistance,
            int startIndex, int endIndex, boolean findOnlyOne) {

        ArrayList foundSeq = new ArrayList<Integer[]>();
        if (startIndex < 0) {
            return foundSeq;
        }
        int windowLen = shortSeq.length;
        endIndex = endIndex < longSeq.length ? endIndex : longSeq.length;
        for (int i = startIndex; i + windowLen < endIndex; i += 2) {
            int longSeqStart = i;

            int distance = DistanceFinder.findDistance(longSeq, shortSeq, longSeqStart, windowLen, maxEditDistance);
            if (distance <= maxEditDistance) {
                foundSeq.add(new Integer[] { i / 2, distance });
                if (findOnlyOne || distance == 0) {
                    break;
                }
                // i += shortSeq.length;
            }
            // else{
            // i++;
            // }
        }
        return foundSeq;
    }

    public static ArrayList<Integer[]> findSeqCheckStart(boolean[] longSeq, boolean[] shortSeq, int maxEditDistance,
            int startIndex, int endIndex, boolean findOnlyOne) {
                
        boolean matchStart = shortSeq[0] == longSeq[startIndex] && shortSeq[1] == longSeq[startIndex + 1];
        if (!matchStart)
            return new ArrayList<Integer[]>();
        return findSeq(longSeq, shortSeq, maxEditDistance, startIndex, endIndex, findOnlyOne);

    }
}
