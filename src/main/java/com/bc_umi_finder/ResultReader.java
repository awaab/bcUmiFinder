package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ResultReader {
    private BufferedReader resReader;
    public ResultReader(String resFile) throws FileNotFoundException{
        this.resReader = new BufferedReader(new FileReader(resFile));
    }
    public String[][] next(int n) throws IOException {
        String[][] res = new String[n][];
        for (int i = 0; i < n; i++) {
            res[i] = next();
        }
        return res;
    }

    public String[] next() throws IOException {
        String line;

        while (true) {
            // System.out.println("=");
            line = resReader.readLine();
            if (line == null)
                return null;
            if (line.charAt(0) == '#') {
                continue;
            }

            return line.split("\\s+");

        }
    }
}
