/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan;

import analyze.PMs;
import analyze.Resources;
import config.ConfigSystem;
import helper.RandomData;
import java.util.ArrayList;
import monitor.PM;
import monitor.VM;
import plan.mraco.Ant;

/**
 *
 * @author Administrator
 */
public class Constraints {
    public static boolean checkExistVMinTier(ArrayList<VM> vms, VM vm){
         // exsit a VM for a Tier contraint
        int numVMTier = 0;
        int size = vms.size();
        for(int i = 0; i < size; i++){
            if(vms.get(i).row  == vm.row){
                numVMTier += 1;
            }
        }
        if(numVMTier > 1)
            return true;
        return false;
    }
    public static VM findMaxAvailableVM(ArrayList<VM> vms){
        VM vm = null;
        
        double max = 0;
        int size = vms.size();
        for(int i = 0; i < size; i++){
            if(max <= vms.get(i).getAvailableResourceVM()){
                max = vms.get(i).getAvailableResourceVM();
                vm = vms.get(i);
                vm.index = i;
            }
        }
        /*
        int index = RandomData.randomInt(0,vms.size()-1);
        //System.out.println("INDEX TURN OFF: "+index);
        vm = vms.get(index);
        */
        return vm;
    }
    
    public static int getAvailablePM4VM(ArrayList<VM> vms, ArrayList<PM> pms, int[] action4PMs, VM prevm){
        int idPM = -1;
        PMs pm = Resources.getLoadPMs(vms,pms);
        /*
        double max =  pm.aload[0];
        for(int i = 0; i < ConfigSystem.numPM; i++){
            double ucpu = pm.acpu[i] - prevm.ccpu;
            double uram = pm.aram[i] - prevm.cram ;
            double udisk = pm.adisk[i] -  prevm.cdisk;
            if(max <= pm.aload[i] &&  ucpu > 0  && uram > 0  && udisk > 0 && action4PMs[i] !=1){
                max = pm.aload[i];
                idPM = i;
            }
        }*/
        double ucpu,uram,udisk;
        do{
            idPM = RandomData.randomInt(0,pms.size()-1);
            ucpu = pm.acpu[idPM] - prevm.getCcpu();
            uram = pm.aram[idPM] - prevm.getCram() ;
            udisk = pm.adisk[idPM] -  prevm.getCdisk();
            
        }while(ucpu < 0  && uram < 0  && udisk < 0 && action4PMs[idPM] ==1);
        //System.out.println("INDEX PM: "+idPM);
        return idPM;
    }
    public static int getIndexVM(VM vm, ArrayList<VM> vms){
        int i = -1;
        for(int index = 0; index < vms.size(); index++){
            if(vm.idVM == vm.idVM){
                return index;
            }
        }
        return i;
    }
    public static boolean satisfy(VM vm,PM pm){
        if(pm.getAcpu() - vm.getCcpu() > 0 && pm.getAram() - vm.getCram() > 0 && pm.getAdisk() - vm.getCdisk()>0)
            return true;
        return false;
    }
            
}
