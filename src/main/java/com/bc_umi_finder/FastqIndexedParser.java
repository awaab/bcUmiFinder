package com.bc_umi_finder;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class FastqIndexedParser {
    private FileReader fastqIndexReader;
    private BufferedReader fastqIndexBufferReader;
    RandomAccessFile fastqRandomAccess;
    private Map<String, fastqIndexEntry> fastqIndexMap;
    private String fastqIndexFile;


    public FastqIndexedParser(String fastqFile, String fastqIndexFile) throws Exception {
        this.fastqIndexFile = fastqIndexFile;
        fastqRandomAccess = new RandomAccessFile(fastqFile, "r");
        fastqIndexMap = new HashMap<String, fastqIndexEntry>();
        readIndex();
    }

    public void readIndex() throws Exception {
        fastqIndexReader = new FileReader(this.fastqIndexFile);
        fastqIndexBufferReader = new BufferedReader(fastqIndexReader);
        while (true) {
            String line = fastqIndexBufferReader.readLine();
            if (line == null)
                return;
            String[] record = line.split("\t");
            String transcriptID = record[0];
            int baseCount = Integer.parseInt(record[1]);
            long start = Long.parseLong(record[2]);
            long qualStart = Long.parseLong(record[5]);
            int basePerLine = Integer.parseInt(record[3]);
            int charPerLine = Integer.parseInt(record[4]);
            
            fastqIndexMap.put(transcriptID, new fastqIndexEntry(baseCount, start, charPerLine, basePerLine,qualStart));
        }
    }

    public fastqIndexEntry getfastqEntry(String transcriptID) {
        return fastqIndexMap.get(transcriptID);
    }

    public char[] readfastqRandomAccess(long start, int charCount) throws IOException {
        fastqRandomAccess.seek(start);
        byte[] bytes = new byte[charCount];
        fastqRandomAccess.read(bytes);
        char[] charArray = new char[charCount];
        for (int i = 0; i < charCount; i++) {
            charArray[i] = (char) bytes[i];
        }
        return charArray;
    }

    public void closeRandomAccessfastq() throws IOException {
        fastqRandomAccess.close();
    }

    public String getString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result = result + (char) bytes[i];
        }
        return result;
    }

    public char[] getTranscriptSequence(fastqIndexEntry fastqEntry) throws IOException {
        int linesCount = (int) Math.ceil(fastqEntry.baseCount / (float) fastqEntry.basePerLine);
        int byteTotal = linesCount * fastqEntry.charPerLine + fastqEntry.basePerLine
                - fastqEntry.baseCount % fastqEntry.basePerLine;
        char[] fastqRead = readfastqRandomAccess(fastqEntry.start, byteTotal);
        char[] baseSequence = new char[fastqEntry.baseCount];
        int ifastqRead = 0;
        for (int i = 0; i < fastqEntry.baseCount; i++) {
            try {
                if (fastqRead[ifastqRead] == '\n')
                    ifastqRead++;

                baseSequence[i] = fastqRead[ifastqRead];
            } catch (Exception e) {

            }

            ifastqRead++;
        }
        return baseSequence;
    }

    public int[] getTranscriptQual(fastqIndexEntry fastqEntry) throws IOException {
        int linesCount = (int) Math.ceil(fastqEntry.baseCount / (float) fastqEntry.basePerLine);
        int byteTotal = linesCount * fastqEntry.charPerLine + fastqEntry.basePerLine
                - fastqEntry.baseCount % fastqEntry.basePerLine;
        char[] fastqRead = readfastqRandomAccess(fastqEntry.qualStart, byteTotal);
        int[] qualSequence = new int[fastqEntry.baseCount];
        int ifastqRead = 0;
        for (int i = 0; i < fastqEntry.baseCount; i++) {
            try {
                if (fastqRead[ifastqRead] == '\n')
                    ifastqRead++;

                    qualSequence[i] = fastqRead[ifastqRead];
            } catch (Exception e) {

            }

            ifastqRead++;
        }
        return qualSequence;
    }


    
    public class fastqIndexEntry {
        public long start;
        public long qualStart;
        public int charPerLine;
        public int basePerLine;
        public int baseCount;

        public fastqIndexEntry(int baseCount, long start, int charPerLine, int basePerLine, long qualStart) {
            this.start = start;
            this.charPerLine = charPerLine;
            this.basePerLine = basePerLine;
            this.baseCount = baseCount;
            this.qualStart = qualStart;
        }

        public String toString() {
            String space = ", ";
            return "[" + baseCount + space + start + space + charPerLine + space + basePerLine  + "]";
        }
    }


    public void close() {
        //close all buffer
        try {
            fastqIndexBufferReader.close();
            fastqIndexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
