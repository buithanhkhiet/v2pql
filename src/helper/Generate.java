/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author Mr. Khiet
 */
public class Generate {
     public static int[][] generatePhi(int j, int m, int min, int max){
        int[][] phi = new int[j][m];
        for(int x = 0; x < j; x++){
            for(int y = 0; y <m; y++){
                phi[x][y] = RandomData.randInt(min,max);
            }
        }
        return phi;
    }
    public static double[][] generateServiceTime(int j, int li, double min, double max){
        double[][]s = new double[j][li];
        for(int x = 0; x < j; x++){
            for(int y = 0; y <li; y++){
                s[x][y] = RandomData.randDouble(min,max);
            }
        }
        return s;
    }
    public static double[][] generateWorkLoad(int j, int li, double min, double max){
        double[][]w = new double[j][li];
        for(int x = 0; x < j; x++){
            for(int y = 0; y <li; y++){
                w[x][y] = RandomData.randDouble(min,max);
            }
        }
        return w;
    }

    public static int[][] generatePhi(int j, int m, int minPhi, int maxPhi, int maxTotalVM4App, int minTotalVM4App) {
        int[][] phi = new int[j][m];
        for(int x = 0; x < j; x++){
            //int maxVM = RandomData.randInt(minTotalVM4App/3,maxTotalVM4App/3);
            //int count = 0;
            //int index = RandomData.randInt(0,m);
            //for(int y = index; y <m; y++){
            for(int y = 0; y <m; y++){
                int tempVM = RandomData.randInt(0,5);
                //if(count<maxVM){
                    phi[x][y] = tempVM;
                    //count += tempVM;
                    
                //}
                
            }
        }
        return phi;
    }
}
