package com.bc_umi_finder;

import java.util.Arrays;

public class Utils {
    public static String arrToStr(String[] str){
        return Arrays.toString(str).toString()
                                .replace(",", "")
                                .replace("[", "")
                                .replace("]", "")
                                .trim();

    }
    public static String arrToStr(Integer[] intArr){
        return Arrays.toString(intArr).toString()
                                .replace(",", "")
                                .replace("[", "")
                                .replace("]", "")
                                .trim();

    }
}
