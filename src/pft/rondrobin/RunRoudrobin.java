/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pft.rondrobin;

import analyze.Analysing;
import analyze.PMs;
import analyze.Resources;
import config.ConfigSystem;
import helper.MyRound;
import java.util.ArrayList;
import monitor.Application;
import monitor.Monitoring;
import monitor.PM;
import monitor.VM;

import plan.ProblemSet;
import plan.roundrobin.RoundRobinProcess;

/**
 *
 * @author Mr. Khiet
 */
public class RunRoudrobin {
     public static void main(String[] args) {
        // TODO code application logic here
        
        //init data
        int time = ConfigSystem.time;
        int numApplications = ConfigSystem.numApplications;
        int numTier = ConfigSystem.numTier;
        int numPM = ConfigSystem.numPM;
        Monitoring monitor = new Monitoring();
       
        for(int t = 0; t < time; t++){
            if(t==0){
            //1. Faulty PM  prediction
            ArrayList<PM> faultyPM = new ArrayList<>();
            ArrayList<PM> safePM = new ArrayList<>();
            ArrayList<VM> vms = new ArrayList<>();
            ArrayList<PM> pms = monitor.pms.get(t);
            ArrayList<VM> migrateVM = new ArrayList<>();
            ArrayList<VM> safeVM = new ArrayList<>();
            ArrayList<Application>  apps = monitor.apps.get(t);
             for(int i = 0; i < apps.size(); i ++){
                ArrayList<VM> vmList = apps.get(i).vms;
                for(int j = 0; j < vmList.size(); j++){
                    VM vm =  vmList.get(j);
                    int idpm =vm.column;
                    pms.get(idpm).addVM2PM(vm);
                    vms.add(vm);
                }
                
                
            }
            
            System.out.println("Time:" + t);
            System.out.println("##INPUT##");
            int[] action4PMs = new int[ConfigSystem.numPM];
            
            
            
            PMs pmsr = Resources.getLoadPMs(vms, pms); 
            
            
            System.out.println("PM\tnumVM\tfault\ttemp\tccpu\tcram\tcdisk");
            for(int i = 0; i < ConfigSystem.numPM; i++){
                PM pm = pms.get(i);
                double uload = pmsr.uload[i];
                int actionPM = Analysing.getAction4PM(uload,pm.getTemp());
                int numVM =  pmsr.numVM[i];
                System.out.println(i+"\t"+numVM+"\t"+actionPM+"\t"+pm.getTemp()
                        +"\t"+pm.getCcpu()+"\t"+pm.getCram()+"\t"+pm.getCdisk());
                
                action4PMs[i] = actionPM;
                if(actionPM == 1){
                    faultyPM.add(pm);
                }
                else{
                    safePM.add(pm);
                }
            }
            //2. List of VMs were deloying the faulty PM
            if(faultyPM.size()>0){
                
                for(int i = 0; i < numApplications; i++){
                    Application app = monitor.apps.get(t).get(i);
                    vms = monitor.apps.get(t).get(i).vms;
                    for(int j = 0; j < vms.size(); j++){
                        if(checkVM(vms.get(j).column, faultyPM)) {
                            migrateVM.add(vms.get(j));
                        }else{
                            safeVM.add(vms.get(j));
                        }
                        
                    }
                }
                
               
                System.out.println("VM need to migrate");
                System.out.println("index\tccpu\tcram\tcdisk\ttype");
                for(int i = 0; i < migrateVM.size(); i++){
                    System.out.println(i
                            //+"\t"+migrateVM.get(i).idApp+"_"
                            //+migrateVM.get(i).idVM
                            +"\t"+migrateVM.get(i).getCcpu()
                            +"\t"+migrateVM.get(i).getCram()
                            +"\t"+migrateVM.get(i).getCdisk()
                            +"\t"+migrateVM.get(i).getType()
                    );
                }
                System.out.println("PM is safe");
                System.out.println("i\tccpu\tcram\tcdisk\tcpu\tram\tdisk\th");
                for(int i = 0; i < safePM.size(); i++){
                    System.out.println(i
                            //+"\t"+safePM.get(i).idPM
                            
                            +"\t"+MyRound.getRound(safePM.get(i).getCcpu())
                            +"\t"+MyRound.getRound(safePM.get(i).getCram())
                            +"\t"+MyRound.getRound(safePM.get(i).getCdisk())
                            +"\t"+MyRound.getRound(safePM.get(i).getCpu())
                            +"\t"+ MyRound.getRound(safePM.get(i).getRam())
                            +"\t"+ MyRound.getRound(safePM.get(i).getDisk())
                            +"\t"+ MyRound.getRound(safePM.get(i).getUtilizationLoad())
                    );
                }
                
               
                System.out.println("LB\tWL\tF");
                double[] r = Resources.getObjective(safeVM, safePM);
                System.out.println(MyRound.getRound(r[0])
                        +"\t"+MyRound.getRound(r[1])
                        +"\t"+MyRound.getRound(ProblemSet.evaluate(r[0],r[1])));
                System.out.println("Run RoundRobin for migrating VM to safe PM ...");
                int anpha =2;
                
                RoundRobinProcess rr = new RoundRobinProcess(migrateVM, safePM,anpha);
               
                rr.run();
                
               
                    
                
            }

        }
            
        }
       
    
   }
   public static boolean checkVM(int idPM, ArrayList<PM> faultyPM){
            
        boolean s = false;
        for(int i = 0; i < faultyPM.size(); i++)
            if(faultyPM.get(i).idPM == idPM)
                return true;
        return s;
    }
}
