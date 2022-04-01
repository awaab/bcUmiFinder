package com.bc_umi_finder;

import java.util.ArrayList;

public class SeqFinder {
    public static ArrayList<Integer[]> findSeq(boolean []longSeq, boolean[] shortSeq, int minEditDistance, int startIndex, int endIndex) {
        ArrayList FoundSeq = new ArrayList<Integer[]>();
        int windowLen = shortSeq.length;
        endIndex = endIndex < longSeq.length ?  endIndex:longSeq.length ;
        for(int i =startIndex; i + windowLen< endIndex;i+=2){
            int longSeqStart = i;
            
            int distance = DistanceFinder.findDistance(longSeq, shortSeq, longSeqStart, windowLen);
            if(distance <= minEditDistance){
                FoundSeq.add(new Integer[]{i / 2, distance});
                break;
            //    i += shortSeq.length;
            }
            //else{
            //    i++;
            //}
        }
        return FoundSeq;
    }
}
