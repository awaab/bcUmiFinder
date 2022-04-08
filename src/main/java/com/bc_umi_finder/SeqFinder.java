package com.bc_umi_finder;

import java.util.ArrayList;

public class SeqFinder {
    public static ArrayList<Integer[]> findSeq(boolean []longSeq, boolean[] shortSeq, int maxEditDistance, int startIndex, int endIndex, boolean findOnlyOne) {
        if(startIndex<0){
            return null;
        }
        ArrayList FoundSeq = new ArrayList<Integer[]>();
        int windowLen = shortSeq.length;
        endIndex = endIndex < longSeq.length ?  endIndex:longSeq.length ;
        for(int i =startIndex; i + windowLen< endIndex;i+=2){
            int longSeqStart = i;
            
            int distance = DistanceFinder.findDistance(longSeq, shortSeq, longSeqStart, windowLen);
            if(distance <= maxEditDistance){
                FoundSeq.add(new Integer[]{i / 2, distance});
                if(findOnlyOne || distance == 0){
                    break;
                }
            //    i += shortSeq.length;
            }
            //else{
            //    i++;
            //}
        }
        return FoundSeq;
    }
}
