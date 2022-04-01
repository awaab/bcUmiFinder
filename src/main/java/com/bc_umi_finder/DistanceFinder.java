package com.bc_umi_finder;
import java.util.Arrays;

public class DistanceFinder {
    public static int findDistance(boolean []seq1, boolean []seq2, int seq1Start, int seq1Len) {
        int len1 = seq1Len / 2;
        int len2 = seq2.length / 2;
    
        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];
    
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
    
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
    
        //iterate though, and check last char
        boolean currentCompare [] = new boolean[4];
        for (int i = 0; i < len1; i++) {
            currentCompare[0] = seq1[seq1Start+i*2];
            currentCompare[1] = seq1[seq1Start+i*2+1];
            for (int j = 0; j < len2; j++) {
                currentCompare[2] = seq2[j*2];
                currentCompare[3] = seq2[j*2+1];
    
                //if last two chars equal
                if (currentCompare[0] == currentCompare[2] && currentCompare[1] == currentCompare[3]) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;
    
                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }

            }
        }
    
        return dp[len1][len2];
    }
}
