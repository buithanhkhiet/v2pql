/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import config.ConfigSystem;
import helper.MyRound;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import monitor.PM;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author Khiet
 */
public class ExportExperiment {
    public static void writeAverageReward(double epsilon, 
                                        double[] avgrewards,
                                       
                                        int timestep
                                        
                                           ) throws IOException{
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "averagereward__"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
            System.out.println("timestep\treward_eps"+epsilon);
            String s ="reward_eps"+epsilon;
            csvWriter.printRecord("timestep",s);
            //double max = 0;
            for(int i =1; i < timestep; i++){
                //if(avgrewards[i-1]<avgrewards[i]){
                    System.out.println(i+"\t"+MyRound.getRound(avgrewards[i]));
                    //max=avgrewards[i];
                    csvWriter.printRecord(i,MyRound.getRound(avgrewards[i],100));
                //}
                    
            }
            csvWriter.close();
              
        } 
         
    }
    public static void writeDiscountedCumulateReward(double epsilon, 
                                        double[] timestepdiscreward,
                                        double nguy,
                                        int timestep,
                                        int numVM
                                        
                                           ) throws IOException{
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "discountreward_"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
            System.out.println("timestep\tdiscountreward_eps"+epsilon);
            String s ="discountreward_eps"+epsilon;
            csvWriter.printRecord("timestep",s);
            //double max = 0;
            for(int i =0; i < timestep; i++){
                /*
                double discreward = 0;
                for(int j = 0; j < numVM; j++){
                    discreward = discountrewards[i][j];
                    for(int k = j+1; k < numVM-1;k++){
                        discreward += Math.pow(nguy,k)*discountrewards[i][k];
                    }
                }*/
                double discreward = timestepdiscreward[i];
                for(int k = i+1; k < timestep-2;k++){
                    //System.out.println("&&&"+k);
                    discreward += Math.pow(nguy,k)*timestepdiscreward[k];
                }
                System.out.println(i+"\t"+discreward);
                csvWriter.printRecord(i,discreward);                    
            }
            csvWriter.close();
        } 
    }
    public static void writeQValue(double epsilon, 
                                        ArrayList<QValue> QTable
                                           ) throws IOException{
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "qvaluemax__"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            System.out.println("timestep\tqvaluemmax_eps"+epsilon);
            
            String s ="qvaluemax_eps"+epsilon;
            csvWriter.printRecord("timestep",s);
            QValue qvalue = QTableUtility.maxQValue(QTable);
            
            System.out.print("State " + qvalue.getId() + ":  LoadBalance:"
                 +qvalue.getState().getStateLoadBalance());
            int[] statePMs = qvalue.getState().getStatePMs();
            for(int j = 0; j<statePMs.length;j++ ){
                System.out.print(" PM"+j+":"+statePMs[j]+" ");
            }
            System.out.println(" TypeVM:"+qvalue.getState().getTypeVM()
                            +" Action:"+qvalue.getAction().getToPM()
                            +" Value:"+qvalue.getValue().get(qvalue.getValue().size()-1).getValue());                       
            int size = qvalue.getValue().size();
            for(int i = 0; i < size; i++){
                //System.out.println(qvalue.getValue().get(i).getInteration()+"\t"+qvalue.getValue().get(i).getValue());
                csvWriter.printRecord(qvalue.getValue().get(i).getInteration(),qvalue.getValue().get(i).getValue());
            }
            csvWriter.close();
        }
        
        RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "qvaluemin__"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            System.out.println("timestep\tqvaluemin_eps"+epsilon);
            
            String s ="qvaluemin_eps"+epsilon;
            csvWriter.printRecord("timestep",s);
            QValue qvalue = QTableUtility.minQValue(QTable);
            
            System.out.print("State " + qvalue.getId() + ":  LoadBalance:"
                 +qvalue.getState().getStateLoadBalance());
            int[] statePMs = qvalue.getState().getStatePMs();
            for(int j = 0; j<statePMs.length;j++ ){
                System.out.print(" PM"+j+":"+statePMs[j]+" ");
            }
            System.out.println(" TypeVM:"+qvalue.getState().getTypeVM()
                            +" Action:"+qvalue.getAction().getToPM()
                            +" Value:"+qvalue.getValue().get(qvalue.getValue().size()-1).getValue());
            
            
            int size = qvalue.getValue().size();
            for(int i = 0; i < size; i++){
                //System.out.println(qvalue.getValue().get(i).getInteration()+"\t"+qvalue.getValue().get(i).getValue());
                csvWriter.printRecord(qvalue.getValue().get(i).getInteration(),qvalue.getValue().get(i).getValue());
            }
            csvWriter.close();
        }
        
    }
    public static void writePolicy(double epsilon, 
                                        ArrayList<QValue> QTable,
                                        int numPM
                                           ) throws IOException{
        String RESULT_CSV_FILE = "./data/policies/policy_"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
            //Header file
            csvWriter.print("Id");
            csvWriter.print("LoadBalance");
            csvWriter.print("TypeVM");
            csvWriter.print("Action");
             csvWriter.print("Value");
            for(int i = 0; i < numPM; i++){
                csvWriter.print("PM"+i);                 
            }
            csvWriter.println();
            //content file
            
            int size = QTable.size();
            System.out.println("Q matrix");
            for (int i = 0; i < size; i++) {
                System.out.print("State " + i + ":  LoadBalance:"
                     +QTable.get(i).getState().getStateLoadBalance());
                System.out.print(" TypeVM:"+QTable.get(i).getState().getTypeVM()
                                +" Action:"+QTable.get(i).getAction().getToPM()
                                +" Value:"+QTable.get(i).getValue().get(QTable.get(i).getValue().size()-1).getValue());
                csvWriter.print(i);
                csvWriter.print(QTable.get(i).getState().getStateLoadBalance());
                csvWriter.print(QTable.get(i).getState().getTypeVM());
                csvWriter.print(QTable.get(i).getAction().getToPM());
                csvWriter.print(QTable.get(i).getValue().get(QTable.get(i).getValue().size()-1).getValue());
                                                         
                int[] statePMs = QTable.get(i).getState().getStatePMs();
                for(int j = 0; j<statePMs.length;j++ ){
                    System.out.print(" PM"+j+":"+statePMs[j]+" ");
                    csvWriter.print(statePMs[j]);
                }
                System.out.println();
                csvWriter.println();
            }
            
        }
    }
    public static void writeProblem(double epsilon, 
                                        double[][] results,
                                        int numVM
                                           ) throws IOException{
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "result_policy_"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        try(
        
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
        
            csvWriter.printRecord("indexVM","idPM","utilization","loadbalance","payoff");
            for(int i = 0; i < numVM; i++){
                csvWriter.printRecord(results[i][0],
                        results[i][1],
                        results[i][2],
                        results[i][3],
                        results[i][4]);
            }
        }
    }

    public static void writeRoundRobinProblem(double[][] results, int numVM) throws IOException {
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "result_roundrobin_"+ConfigSystem.sdataset+".csv";
        try(
        
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
        
            csvWriter.printRecord("indexVM","idPM","utilization","loadbalance","payoff");
            for(int i = 0; i < numVM; i++){
                csvWriter.printRecord(
                        results[i][0],
                        results[i][1],
                        results[i][2],
                        results[i][3],
                        results[i][4]);
            }
        }
    }
    public static void writePlayers(ArrayList<Player> players, String sAlgorithm) throws IOException {
        String RESULT_CSV_FILE = "G:/My Drive/LATS/3_Thesis/Publication/"
                + "11.VM2PM.QLearning/Experiment/Analysis/Analysis/data/"
                + "players_"+sAlgorithm+"_"+ConfigSystem.sdataset+".csv";
        try(
        
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_CSV_FILE));
            CSVPrinter csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ){
            
        
            csvWriter.printRecord("id","idPM","loadbalance","utilization","utility");
            int size = players.size();
            for(int i = 0; i < size; i++){
                Player player = players.get(i);
                csvWriter.printRecord(
                        player.getId(),
                        player.getIdPM(),
                        player.getLoadbalance(),
                        player.getUtilization(),
                        player.getUtility()
                );
            }
        }
    }
    public static void updatePlayerInfor(ArrayList<Player> players, PM pm, ArrayList<PM> buSafePM, int anpha) {
        int size = players.size();
        for(int i = 0 ; i < size; i++){
            Player player = players.get(i);
            if(player.getIdPM() == pm.idPM){
                Objective obj = new Objective(buSafePM, pm, anpha);
                player.setLoadbalance(obj.getLoadbalance());
                player.setUtilization(obj.getUtilization());
                player.setUtility(obj.getPayoff());
                
            }
        }
    }

    
     
    
}
