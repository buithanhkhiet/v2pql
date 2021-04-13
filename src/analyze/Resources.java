/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyze;

import config.ConfigSystem;
import helper.MyRound;
import java.util.ArrayList;
import monitor.Application;
import monitor.PM;
import monitor.VM;
import plan.Solution;

/**
 *
 * @author Administrator
 */
public class Resources {
    
    public static PMs getLoadPMs(ArrayList<VM> vms, ArrayList<PM> pms){
        
        int numPM = pms.size();
        
        //capacity
        double[] ccpu = new double[numPM];
        double[] cram = new double[numPM];
        double[] cdisk = new double[numPM];
        
        //utility
        double[] ucpu = new double[numPM];
        double[] uram = new double[numPM];
        double[] udisk = new double[numPM];
        
        //available
        double[] acpu = new double[numPM];
        double[] aram = new double[numPM];
        double[] adisk = new double[numPM];
        
        //uload
        double[] uload = new double[numPM];
        double[] wload = new double[numPM];
        int[] numVMs = new int[numPM];
        
       
        
        
        //calculcate load for PM
        for(int i = 0; i < numPM; i++){
            PM pm = pms.get(i);
            
            
            double uCpuPM = pm.getCpu();
            double uRamPM = pm.getRam();
            double uDiskPM = pm.getDisk();
            acpu[i] = pm.getAcpu();
            aram[i] = pm.getAram();
            adisk[i] = pm.getAdisk();
            ccpu[i] = pm.getCcpu();
            cram[i] = pm.getCram();
            cdisk[i] = pm.getCdisk();
            
            double uloadPM = ConfigSystem.anpha[0]*uCpuPM + 
                             ConfigSystem.anpha[1]*uRamPM +
                             ConfigSystem.anpha[2]*uDiskPM; 
            uload[i] = MyRound.getRound(uloadPM);
            wload[i] = Math.sqrt(
                                Math.pow(acpu[i]/ccpu[i], 2)+
                                Math.pow(aram[i]/cram[i], 2)+
                                Math.pow(adisk[i]/cdisk[i], 2)
                                );//MyRound.getRound(1-uloadPM);
            
            ucpu[i] = uCpuPM;
            uram[i] = uRamPM;
            udisk[i] = uDiskPM;
            
            ccpu[i] = pm.getCcpu();
            cram[i] = pm.getCram();
            cdisk[i] = pm.getCdisk();
            
            acpu[i] = pm.getAcpu();
            aram[i] = pm.getAram();
            adisk[i] = pm.getAdisk();
            
            numVMs[i] = pm.getVms().size();
        }
        return new PMs(ccpu, cram, cdisk, ucpu, uram, udisk, acpu, aram, adisk, uload, wload, numVMs);
    }
    
    public static double avgrLoadPM(double[] aload){
        double avgrload = 0;
        for(int i =0; i < aload.length; i++){
            avgrload += aload[i];
        }
        return avgrload/ConfigSystem.numPM;
    }
   
     public static double[] getObjective(ArrayList<VM> vms, ArrayList<PM> pms){
        int numPM = pms.size();
        double[] uload = getLoadPMs(vms,pms).uload;
        double[] wload = getLoadPMs(vms,pms).wload;
        double[] s = new double[2];
        double avgrload = avgrLoadPM(uload);
        
        double varLoad = 0;
        double wasteLoad = 0;
        for(int j = 0; j < numPM; j++){
            varLoad += Math.pow(uload[j] - avgrload, 2);
            wasteLoad +=  wload[j];
            
        }
        s[0] =  varLoad/(numPM - 1);
        s[1] = wasteLoad;
        return s;
        
        
    }
    
    public static int calculateNumVM(int[][] phi){
        int num = 0;
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for(int j = 0; j < ConfigSystem.numPM; j++){
                num += phi[i][j];
            }
        }
        return num;
    }
    public static double getCost(ArrayList<VM>vms, PMs pms){
        double cost = 0;
        int size = vms.size();
        for(int i = 0; i < size; i++){
            cost += vms.get(i).getCost(pms);
        }
        return cost;
    }
    public static int getNumVM(int idPM, ArrayList<Application> apps){
        int numVM = 0;
        int size = apps.size();
        for(int i = 0; i < size; i++){
            int[][] phi = apps.get(i).getPhi();
            for(int row = 0; row < ConfigSystem.numTier; row++){
                numVM += phi[row][idPM];
            }
        }
        return numVM;
    }
    public static ArrayList<Solution> convert2Solution(ArrayList<Application> apps){
        ArrayList<Solution> s = new ArrayList<>();
        int size = apps.size();
        for(int i = 0; i < size; i++){
            s.add( new Solution(apps.get(i)));
        }
        return s;
    }
    public static double costApp(ArrayList<Solution> s){
        int size = s.size();
        double c = 0;
        for(int i = 0; i < size; i++){
            c += s.get(i).cost;
        }
        return c;
    }
    public static double varianceData(double[] data){
        
        double var = 0;
        int size = data.length;
        double avgrdata = 0;
        for(int i =0; i < size; i++){
            avgrdata += data[i];
        }
        avgrdata= avgrdata/size;
        for(int j = 0; j < size; j++){
            var += Math.pow(data[j] - avgrdata, 2);
        }
        return var/size;
        
    }
    public static double varianceData(double[] data,Action action){
        
        double var = 0;
        int size = data.length;
        double avgrdata = 0;
        int num = 0;
        for(int i =0; i < size; i++){
            if(action.action4PMs[i] !=1){
                avgrdata += data[i];
                num++;
            }
            
        }
        avgrdata= avgrdata/num;
        for(int j = 0; j < size; j++){
            if(action.action4PMs[j] !=1){
                var += Math.pow(data[j] - avgrdata, 2);
            }
        }
        return var/num;
        
    }
     public static double totalLoadPM(double[] aload){
        double avgrload = 0;
        for(int i =0; i < ConfigSystem.numPM; i++){
            avgrload += aload[i];
        }
        return avgrload;
    }
    public static double totalLoadPM(double[] aload, Action action){
        double avgrload = 0;
        for(int i =0; i < ConfigSystem.numPM; i++){
            if(action.action4PMs[i] != 1){
                avgrload += aload[i];
            }
        }
        return avgrload;
    }
    
}
