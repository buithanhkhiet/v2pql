/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;


import java.util.ArrayList;
import config.ConfigSystem;


/**
 *
 * @author Administrator
 */
public class Application {
    
    public int time;
    public int idApp;
    public double workload;
    public ArrayList<VM>  vms;
    

    public Application(int time, int idApp, double workload, ArrayList<VM> vms) {
        this.time = time;
        this.idApp = idApp;
        this.workload = workload;
        this.vms = vms;
    }
    public int[][] getPhi(){
        int[][] phi = new int[ConfigSystem.numTier][ConfigSystem.numPM];
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for(int j = 0; j < ConfigSystem.numPM; j++){
                int size = vms.size();
                for(int k = 0; k < size; k++){
                    VM vm = vms.get(k);
                    if(vm.row == i && vm.column == j){
                        phi[i][j] += 1;
                        break;
                    }
                    else{
                        phi[i][j] = 0;
                    }
                }
            }
        }
        return phi;
    }
    
    public double getWorkLoad(){
        return this.workload;
    }
    
    
    
}
