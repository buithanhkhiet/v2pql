/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import helper.MyRandom;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author NguyenLinh
 */
public class RandomPMS {
    
    private static final String SAMPLE_CSV_FILE = "./data/data/pms.csv";
    
    public static void main(String[] args) throws IOException{
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            double generatorCPU = MyRandom.randomDoubleMinMax(0.1, 0.2);//new Random();
            double generatorRAM =  MyRandom.randomDoubleMinMax(0.1, 0.2);//new Random();
            double generatorDISK =  MyRandom.randomDoubleMinMax(0.1, 0.2);//new Random();
            Random generatorTEMP =  new Random();
            Random generatorBandWidth =  new Random();
            Random generatorSpecs = new Random();
            ComputerSpecs spec1 = new ComputerSpecs(128, 1024*32, 2048*2);
            ComputerSpecs spec2 = new ComputerSpecs(256, 2048*32, 4096*2);
            ComputerSpecs spec3 = new ComputerSpecs(512, 4096*32, 8192*2);
            List<ComputerSpecs> specsList = new ArrayList<>();
            specsList.add(0, spec1);
            specsList.add(1, spec2);
            specsList.add(2, spec3);
            for (int i = 0; i < 1; i++){
                for (int j = 0; j < 450; j++){
                    
                    int randSpec = generatorSpecs.nextInt(3);
                    csvWriter.printRecord(
                            i, //time
                            j, //id PM
                            generatorCPU, 
                            generatorRAM,
                            generatorDISK,
                            generatorTEMP.nextDouble(),
                            specsList.get(randSpec).getCcpu(),
                            specsList.get(randSpec).getCram(),
                            specsList.get(randSpec).getCdisk(),
                            generatorBandWidth.nextDouble()
                    );
                    //System.out.println("time: " + i + " idPM: " + j + " cpu: " + normalRandCPU.getNumericalMean()/10.0 + " ram: " + normalRandRAM.getNumericalMean()/10.0 + " disk: " + normalRandDISK.getNumericalMean()/10.0 + " temp: " + normalRandTEMP.getNumericalMean()/10.0 + " ccpu:" + specsList.get(randSpec).getCcpu() + " cram: " + specsList.get(randSpec).getCram() + " cdisk: " + specsList.get(randSpec).getCdisk());
                }
            }
        }
    }
}
