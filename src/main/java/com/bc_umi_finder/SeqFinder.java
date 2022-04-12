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
        for (int longSeqStart = startIndex; longSeqStart + windowLen < endIndex; longSeqStart += 2) {

            int distance = DistanceFinder.findDistance(longSeq, shortSeq, longSeqStart, windowLen, maxEditDistance);
            if (distance <= maxEditDistance) {
                foundSeq.add(new Integer[] { longSeqStart / 2, distance });
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

        ArrayList foundSeq = new ArrayList<Integer[]>();
        if (startIndex < 0) {
            return foundSeq;
        }
        int windowLen = shortSeq.length;
        endIndex = endIndex < longSeq.length ? endIndex : longSeq.length;
        for (int longSeqStart = startIndex; longSeqStart + windowLen < endIndex; longSeqStart += 2) {
            boolean matchStart = shortSeq[0] == longSeq[longSeqStart] && shortSeq[1] == longSeq[longSeqStart + 1];
            int distance;
            if (!matchStart)
                distance =Integer.MAX_VALUE;
            else
                distance = DistanceFinder.findDistance(longSeq, shortSeq, longSeqStart, windowLen, maxEditDistance);
            if (distance <= maxEditDistance) {
                foundSeq.add(new Integer[] { longSeqStart / 2, distance });
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

    
}
