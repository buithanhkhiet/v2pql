/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author Administrator
 */
public class ConfigSystem {
    
    //Config input
    //datatest
    
    
    public static String sdataset = "datasettest";
    public static int time = 1;//4
    public static int numApplications = 3;//100;//3
    public static int numPM = 5;//150;
    public static String sdata = "/data/datatest/";
    
    
    /*
    
    //dataset1
    public static String sdataset = "dataset1";
    public static int time = 1;//4
    public static int numApplications = 30;//100;//3
    public static int numPM = 50;//150;
    public static String sdata = "/data/dataset1/";
    */
    
    
    //dataset2
    
    /*
    public static String sdataset = "dataset2";
    public static int time = 1;//4
    public static int numApplications = 100;//3
    public static int numPM = 150;
    public static String sdata = "/data/dataset2/";
    */
    
    //dataset3
    
    
    /*
    public static String sdataset = "dataset3";
    public static int time = 1;//4
    public static int numApplications = 200;//3
    public static int numPM = 450;
    public static String sdata = "/data/dataset3/";
    */
    
    //General Config 
    public static int numTier = 3;
    public static int[] numMaxVM4App = {15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15};
    public static int[] numMinVM4App = {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
    
    
    //Config IO
    public static String outDesInputLBW = "data/input/InputLBW.csv";
    public static String outDesInputApp = "data/input/InputApp.csv";
    
    //public static double epsilon = 0.1;
    //public static double coolingRate = 10;
    //public static String outdata = "data/output_"+epsilon+".csv";
    
    //public static String outdata = "data/output_epsilon"+epsilon+"_coolingRate"+coolingRate+".csv";
    public static String outdataACO =  "data/output/outputACO.csv";
    public static String outdataPSO =  "data/output/outputPSO.csv";
    public static String outdataSA =  "data/output/outputSA.csv";
    
    public static String outdataMigrateACO =  "data/output/outputMigrateACO.csv";
    public static String outdataMigratePSO =  "data/output/outputMigratePSO.csv";
    public static String outdataMigrateSA =  "data/output/outputMigrateSA.csv";
    

    //Fuzzy Config
    public static double[] gamma = {0.3,0.3,0.4};
    public static double[] anpha = {0.3,0.3,0.4};
    
    //Costing Config
    public static double a[] = {0.5,1};
    
    //Problem Config
    public static double tau =  0.5;
    public static double nguy = 0.5;
    public static double theta1 = 0.5;
    
    
    
    
    
    
}
