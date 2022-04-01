package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ResultFileSeqFinder {
    private FastqIndexedParser fqp;
    private BufferedReader resReader;
    private int threads;

    public ResultFileSeqFinder(String fastqFile, String resFile, int threads) throws Exception {
        this.fqp = fqp;
        this.resReader = resReader;
        this.threads = threads;
        String fastqIndexFile = fastqFile + ".fai";
        fqp = new FastqIndexedParser(fastqFile, fastqIndexFile);
        resReader = new BufferedReader(new FileReader(resFile));
    }

    public String nextPassRes() {
        String idLine;
        String resLine;
        while (true) {
            try {
                idLine = resReader.readLine();
                if (idLine.charAt(0) == '#') {
                    continue;
                }
                resLine = resReader.readLine();
                if (idLine.charAt(0) == '@' && Character.isDigit(resLine.charAt(0)))
                    return resLine + "------" + idLine;
            } catch (IOException e) {
                return null;
            }
        }
    }
}
