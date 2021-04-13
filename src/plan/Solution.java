/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan;

import analyze.PMs;
import analyze.QueueingNetwork;
import config.ConfigSystem;
import helper.MyRound;
import java.util.ArrayList;
import monitor.Application;
import monitor.VM;

/**
 *
 * @author Administrator
 */
public class Solution {
    
    public int time;
    public int idApp;
    public int[][] phi;
    public double workload;
    public ArrayList<VM> vms;
    
    public double avgVariantAvailbleLoad;
    public double responsetime;
    public double cost;
    
    public Solution(Application app){
        this.time = app.time;
        this.idApp = app.idApp;
        this.phi = app.getPhi();
        this.workload = app.workload;
        this.vms = (ArrayList<VM>) app.vms.clone();
        updateInfo();
      
    }
    public Solution(Solution s){
        this.time = s.time;
        this.idApp = s.idApp;
        this.phi = s.phi.clone();
        this.workload = s.workload;
        //this.responsetime = s.responsetime;
        //this.cost = s.cost;
        this.vms = (ArrayList<VM>) s.vms.clone();
        updateInfo();
    }

    public Solution(int time, int idApp, int[][] phi, double workload, ArrayList<VM> vms) {
        this.time = time;
        this.idApp = idApp;
        this.phi = phi.clone();
        this.workload = workload;
        this.vms = (ArrayList<VM>) vms.clone();
        //update infor
        updateInfo();
    }
   
    public double getResponseTime(){
        int numVM = this.vms.size();
        double rhos[] = new double[numVM];
        double lambdas[] = new double[numVM];
        //double w[] = new double[numVM];
        for(int i = 0; i < numVM; i++){
            
            rhos[i] = vms.get(i).getRho(numVM);
            lambdas[i] = vms.get(i).lambda;
            //w[j] = vms.get(j).getAvailableResourceVM();
        }
        return MyRound.getRound(QueueingNetwork.responsetime(rhos, lambdas));
         
    }
    public double getCost(PMs pmsr){
        double cost = 0;
        int numVM = this.vms.size();
        for(int j = 0; j < numVM; j++){
            cost += this.vms.get(j).getCost(pmsr);
        }
        return cost;
    }
    public double agrAvailableResourceVMs(){
        int size = this.vms.size();
        double agr = 0;
        for(int i=0; i<size; i++){
            agr+=this.vms.get(i).getAvailableResourceVM();
        }
        return MyRound.getRound(agr/size);
    }
    //variant of available load of App
    public double getVariantAvailbleLoad(){
        double agv = agrAvailableResourceVMs();
        double availableload = 0;
        int size = vms.size();
        for(int i = 0; i < size; i++){
            VM vm = vms.get(i);
            availableload += Math.pow(agv - vm.getAvailableResourceVM(), 2);
        }
        availableload = availableload/size;
        return MyRound.getRound(availableload);
    }
    public final void updateInfo(){
        this.avgVariantAvailbleLoad = getVariantAvailbleLoad();
        this.responsetime = getResponseTime();
        //this.cost = getCost();
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
    public double getAgvUtilizationLoad(){
         return MyRound.getRound(1 - agrAvailableResourceVMs());
    }
    
}
