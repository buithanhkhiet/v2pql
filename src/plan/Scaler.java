/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan;

import analyze.Resources;
import config.ConfigSystem;
import helper.RandomData;
import static helper.RandomData.randomInt;
import java.util.ArrayList;
import monitor.PM;
import monitor.VM;



/**
 *
 * @author Mr. Khiet
 */
public class Scaler {
    public static void scaleDown(Solution s){
        //find max availble resourece VM
        //int index = RandomData.randomInt(0,s.vms.size()-1);
        //VM vm = s.vms.get(index);//
        //Constraints.findMaxAvailableVM(s.vms);
        VM vm = Constraints.findMaxAvailableVM(s.vms);
        //int index = Constraints.getIndexVM(vm, s.vms);
        // exsit a VM for a Tier contraint
        boolean numVMTier = Constraints.checkExistVMinTier(s.vms, vm);
        //remove VM
        if(ConfigSystem.numMinVM4App[s.idApp] < Resources.calculateNumVM(s.phi) && vm != null && numVMTier  && s.phi[vm.row][vm.column]>0 ){
            //update phi
            s.phi[vm.row][vm.column] -= 1;
            //update vm
            s.vms.remove(vm.index);
        }
    }
    
    public static boolean checkFaultPM(int[] action4PMs){
        boolean check = false;
        for(int i = 0; i < action4PMs.length; i++){
            if(action4PMs[i] ==1){
                return true;
            }
        }
        return check;
    }
    public static void migarte(Solution s, double[][] trails,  ArrayList<PM> pms,double[] probabilities, int[] action4PMs){
        
        //migrate
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for( int j = 0; j < ConfigSystem.numPM; j++){
                int numVM = s.phi[i][j];
                if(numVM>0 && action4PMs[j]==1){
                    for(int k = 0; k < numVM; k++){
                        //find safe PM
                        int index =0;
                        do{
                            //index = Trail.selectNextHost(j,trails, pms,probabilities, action4PMs);//RandomData.randomInt(0,ConfigSystem.numPM-1);
                            if(index == -1){
                                return;
                            }
                        }
                        while(index == j || action4PMs[index] ==1 );
                        //update phi
                        s.phi[i][index] += 1;
                        s.phi[i][j] -= 1;
                        //update vms
                        for(VM v: s.vms){
                            if(v.row == i && v.column == j){
                                v.column = index;
                                break;
                            }
                        }

                    }
                }
            }
        }
        
    }
    
    public static void migarte(int[] action4PMs,  Solution s){
        
        //migrate
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for( int j = 0; j < ConfigSystem.numPM; j++){
                int numVM = s.phi[i][j];
                if(numVM>0 && action4PMs[j]==1){
                    for(int k = 0; k < numVM; k++){
                        //find safe PM
                        int index =0;
                        do{
                            index = RandomData.randomInt(0,ConfigSystem.numPM-1);
                        }
                        while(index == j || action4PMs[index] ==1 );
                        //update phi
                        s.phi[i][index] += 1;
                        s.phi[i][j] -= 1;
                        //update vms
                        for(VM v: s.vms){
                            if(v.row == i && v.column == j){
                                v.column = index;
                                break;
                            }
                        }

                    }
                }
            }
        }
        
    }
    

     public static void migarte(int idPM, int[] action4PMs, int[][] newPhi, int[][] s, ArrayList<VM> vms, Phi newVelPhi) {
        //find safe PM
        int index =0;
        do{
            index = randomInt(0,ConfigSystem.numPM-1);
        }
        while(index == idPM || action4PMs[index] ==1 );
        //migrate
        for(int j = 0; j < ConfigSystem.numTier; j++){
            //update vel
            newVelPhi.value[j][index] = s[j][idPM];
            newVelPhi.value[j][idPM] = -s[j][idPM];
            //update phi
            newPhi[j][index] += newVelPhi.value[j][index];
            newPhi[j][idPM] += newVelPhi.value[j][idPM];
            //update vms
            for(VM v: vms){
                if(v.row == j && v.column == idPM){
                    v.column = index;
                }
            }
        }
    }
     public static void migarte(int[] action4PMs, Solution s,ArrayList<Solution> pls ,ArrayList<Solution> gls, ArrayList<PM> pms, double w){
        
        //migrate
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for( int j = 0; j < ConfigSystem.numPM; j++){
                int numVM = s.phi[i][j];
                if(numVM>0 && action4PMs[j]==1){
                    for(int k = 0; k < numVM; k++){
                        //find safe PM
                        int index =0;
                        do{
                            //index = PSOUtility.selectNextHost(j,pls,gls,pms, s, w);
                            index =  RandomData.randomInt(0,ConfigSystem.numPM-1);
                        }
                        while(index == j || action4PMs[index] ==1 );
                        //update phi
                        s.phi[i][index] += 1;
                        s.phi[i][j] -= 1;
                        //update vms
                        for(VM v: s.vms){
                            if(v.row == i && v.column == j){
                                v.column = index;
                                break;
                            }
                        }

                    }
                }
            }
        }
        
    }
}
