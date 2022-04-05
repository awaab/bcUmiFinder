package com.bc_umi_finder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class FastqParser {
    private BufferedReader reader;
    // function to parse sequence from fastq file
    public FastqParser(String fileName) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(fileName));
 
    }
    public String [][] next(int n){
        String [][] records = new String[n][3];
        for(int i =0; i<n; i++){
            records[i] = next();
        }
        return records;
    }
    public String [] next(){
        try{
            String line = reader.readLine();
            // System.out.println(line);
            assert line.charAt(0) == '@';
            StringTokenizer st = new StringTokenizer(line);
            String id = st.nextToken().substring(1);
            String runID = st.nextToken().substring(6);
            String sampleID = st.nextToken();
            String read = st.nextToken();
            String chLenStr = st.nextToken().substring(3);
            int chLen = Integer.parseInt(chLenStr);

            String seq = reader.readLine();
            // System.out.println(seq.length());

            String plus = reader.readLine();
            String qual = reader.readLine();


            String record [] = {
                id,
                runID,
                seq,
                // sampleID,
                //read,
                //chLenStr
            };
                return record;
            }   
                catch (IOException e) {
                e.printStackTrace();
                return null;
            } 

     }
           
    public void close() throws IOException {
        reader.close();
    }

}
