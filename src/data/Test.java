/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Random;
//import org.apache.commons.math3.distribution.NormalDistribution;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * @author nguye
 */
public class Test {
    
    private static final String FILENAME = "E:\\JAVA\\Netbeans\\pft\\data\\Test\\data_coolingrate_0.09.txt";
    
    public static void main(String[] args){
        
        BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;
                        double result = 0;
			while ((sCurrentLine = br.readLine()) != null) {
                            if (sCurrentLine.contains("Excution Time")){
                                result += Double.parseDouble(sCurrentLine.substring(13));
                            }
			}
                        
                        System.out.println(result/4);

		} catch (IOException e) {
		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
    }
}
