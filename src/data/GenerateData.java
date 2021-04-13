/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author NguyenLinh
 */
public class GenerateData {

    private static final String SAMPLE_CSV_FILE = "./data/data/workloads.csv";
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            NormalDistribution standarRandom;
            Random generator = new Random();
            for (int i = 0; i < 1; i++){
                for (int j = 0; j < 3; j++){
                    standarRandom = new NormalDistribution(generator.nextInt(9 + 1 - 1) + 1,0.1,0.9);
                    csvWriter.printRecord(i, j, standarRandom.getNumericalMean()/10.0);
                    System.out.println("time: " + i + " idApp: " + j + " idWorkLoad: " + standarRandom.getNumericalMean()/10.0);
                }
            }
        }
    }
    
}
