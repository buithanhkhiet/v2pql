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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author nguye
 */
public class RandomAPP {
    
    private static final String SAMPLE_CSV_FILE = "./data/data/app0.csv";
    
    public static void main(String[] args) throws IOException{
        
        for (int h = 0; h < 200; h++){
        
            try(
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("./data/data/app" + h + ".csv"));
                CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
                ){
                
                Random generatorCPU = new Random();
                Random generatorRAM = new Random();
                Random generatorDISK = new Random();
                Random generatorSpecs = new Random();
                Random generatorCol = new Random();
                Random generateIdPm = new Random();
                Random generatorLAMBDA = new Random();
                Random generatorVM = new Random();
                ComputerSpecs spec1 = new ComputerSpecs(8, 1024*2, 256);
                ComputerSpecs spec2 = new ComputerSpecs(16, 1024*4, 512);
                ComputerSpecs spec3 = new ComputerSpecs(32, 1004*8, 1024);
                List<ComputerSpecs> specsList = new ArrayList<>();
                specsList.add(0, spec1);
                specsList.add(1, spec2);
                specsList.add(2, spec3);
                int indexCol;
                int countRow;
                int idPM;
                
                
                //int temp_numberVM = numberVM;                
                for (int i = 0; i < 1; i++){
                    int numberVM = generatorVM.nextInt(15-5)+5;
                    countRow = 0;
                    //ramdom matrix
                    ArrayList rowTest = new ArrayList();
                    for (int z = 0; z < 200; z++){
                        rowTest.add(z); 
                    }
                    Collections.shuffle(rowTest);
                    for (int j = 0; j < numberVM; j++){
                        //random specs VM
                        int randSpec = generatorSpecs.nextInt(3);
                        indexCol = generatorCol.nextInt(3);
                        idPM     = generateIdPm.nextInt(449 + 1);
                        switch (j) {
                            case 0:
                                indexCol = 0;
                                break;
                            case 1:
                                indexCol = 1;
                                break;
                            case 2:
                                indexCol = 2;
                                break;
                            default:
                                break;
                        }
                        csvWriter.printRecord(
                                i, //time
                                j, //id VM
                                indexCol, // tier
                                //rowTest.get(countRow), 
                                idPM, // index id PM
                                (generatorCPU.nextGaussian()*0.09 + 0.7),
                                (generatorRAM.nextGaussian()*0.09 + 0.7),
                                (generatorDISK.nextGaussian()*0.09 + 0.7),
                                4,
                                1000,
                                500,
                                generatorLAMBDA.nextInt(20-10)+10,
                                (float)1/numberVM,
                                specsList.get(randSpec).getCcpu(),
                                specsList.get(randSpec).getCram(),
                                specsList.get(randSpec).getCdisk(),
                                0);
                        //System.out.println("time: " + indexCol + " idVM: " + j + " col: " + j + " row: " + rowTest.get(countRow) + " cpu: " + (generatorCPU.nextGaussian()*0.09 + 0.5) + " ram: " + (generatorRAM.nextGaussian()*0.09 + 0.5) + " disk: " + (generatorDISK.nextGaussian()*0.09 + 0.5) + " ccpu:" + specsList.get(randSpec).getCcpu() + " cram: " + specsList.get(randSpec).getCram() + " cdisk: " + specsList.get(randSpec).getCdisk());
                        countRow++;
                    }
                }
            }
        }
    }
}
