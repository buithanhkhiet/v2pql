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
public class Monitoring {
    
    public ArrayList<ArrayList<PM>> pms;  
    public ArrayList<ArrayList<Application>>  apps;
    public ReadData rdata;
    public Monitoring(){
       rdata = new ReadData();
       this.pms = new ArrayList<ArrayList<PM>>();
       this.apps = new ArrayList<ArrayList<Application>>();
       createApplications();
       createPM();
    }
    
    public void createApplications(){
        //init list application
       for(int i =0 ; i < ConfigSystem.time; i++){
           apps.add(new ArrayList<Application>());
           for(int j = 0; j <ConfigSystem.numApplications ; j++){
               int time = i;
               int idApp = j;
               double workload = rdata.getWorkLoad(time, idApp);
               ArrayList<VM> vms = rdata.getVM(time, idApp);
               Application app = new Application(time, idApp, workload, vms);
               apps.get(i).add(app);
               
           }
           
       }
    }
    public void createPM(){
        for(int i =0 ; i < ConfigSystem.time; i++){
           pms.add(new ArrayList<PM>());
           for(int j = 0; j <ConfigSystem.numPM ; j++){
               PM pm = rdata.getPM(i, j);
               pms.get(i).add(new PM(pm));
               
           }
       }
    }
    
}
