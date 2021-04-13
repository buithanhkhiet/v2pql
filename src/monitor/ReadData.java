/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

import helper.CSVFileReader;
import java.io.File;
import java.util.ArrayList;
import config.ConfigSystem;

/**
 *
 * @author Administrator
 */
public class ReadData {
    
    public ArrayList<PM> pms = new ArrayList<>();        
    public ArrayList<WorkLoad> workloads = new ArrayList<>();
    public ArrayList listVM = new ArrayList();
    public ReadData(){
        //read vm data
        readApplicationData();
        //read pm data
        readPMData();
        //read workload data
        //readWorkLoadData();
        System.out.println("completed reading data");
    }
    public void readApplicationData(){
        int numApp = ConfigSystem.numApplications;
        for(int num = 0; num < numApp; num++){
            int time = 0,idVM=0,idApp=num,row=0,column=0;
            double cpu=0, ram=0, disk=0, c=0,s=0,z=0,lambda=0,r=0;
            double ccpu, cram, cdisk;
            int type;
            File f = new File("");
            String sVM = f.getAbsolutePath()+ConfigSystem.sdata+"app"+num+".csv";
            CSVFileReader vmdata = new CSVFileReader(sVM);
            vmdata.ReadFile();
          
            int numrow = vmdata.getRowsNum();
            ArrayList<VM> vms = new ArrayList<>();
            for(int i = 0; i < numrow; i++){
                time = (int) vmdata.getValue(i, 0);
                idVM = (int) vmdata.getValue(i, 1);
                row = (int) vmdata.getValue(i, 2);
                column = (int) vmdata.getValue(i, 3);
                cpu = vmdata.getValue(i, 4);
                ram = vmdata.getValue(i, 5);
                disk = vmdata.getValue(i, 6);
                c = vmdata.getValue(i, 7);
                s = vmdata.getValue(i, 8);
                z = vmdata.getValue(i, 9);
                lambda = vmdata.getValue(i, 10);
                r = vmdata.getValue(i, 11);
                ccpu = vmdata.getValue(i, 12);
                cram = vmdata.getValue(i, 13);
                cdisk = vmdata.getValue(i, 14);
                type = (int) vmdata.getValue(i, 15);
                VM vm = new VM(time, idVM, idApp, row, column, cpu, ram, disk, c, s, z, lambda, r, ccpu, cram, cdisk,type);
                vms.add(vm);
            }
            listVM.add(num,vms);
           
        }
        
    }
    public void readPMData(){
        int time = 0, idPM = 0;
        double cpu = 0, ram = 0, disk = 0, temp = 0, bandwidth=0;
        double ccpu, cram, cdisk;
        File f = new File("");
        String sPM = f.getAbsolutePath()+ConfigSystem.sdata+"pms.csv";
        CSVFileReader pmdata = new CSVFileReader(sPM);
        pmdata.ReadFile();
        int numrow = pmdata.getRowsNum();
        for(int i = 0; i < numrow; i++){
            time = (int) pmdata.getValue(i, 0);
            idPM = (int) pmdata.getValue(i, 1);
            cpu = pmdata.getValue(i, 2);
            ram = pmdata.getValue(i, 3);
            disk = pmdata.getValue(i, 4);
            temp = pmdata.getValue(i, 5);
            ccpu = pmdata.getValue(i, 6);
            cram = pmdata.getValue(i, 7);
            cdisk = pmdata.getValue(i, 8);
            bandwidth = pmdata.getValue(i, 9);
            PM pm = new PM(time, idPM, cpu, ram, disk, temp, ccpu, cram, cdisk,bandwidth);
            pms.add(pm);
        }
    }
    public void readWorkLoadData(){
        int time = 0,idApp=0;
        double workoad = 0;
        File f = new File("");
        String sWL = f.getAbsolutePath()+ConfigSystem.sdata+"workloads.csv";
        CSVFileReader wl = new CSVFileReader(sWL);
        wl.ReadFile();
        int row = wl.getRowsNum();
        for(int  i = 0; i < row; i++){
            time = (int) wl.getValue(i,0);
            idApp =(int) wl.getValue(i,1);
            workoad = wl.getValue(i,2);
            WorkLoad w = new WorkLoad(time, idApp, workoad);
            workloads.add(w);
        }
        
    }
    public double getWorkLoad(int time, int idApp){
        int size = this.workloads.size();
        double workload = 0;
        for(int i =0; i < size; i++ ){
            WorkLoad wl = this.workloads.get(i);
            if(wl.time == time && wl.idApp == idApp){
                return wl.workoad;
            }
        }
        return workload;
    }
    public ArrayList<VM> getVM(int time, int idApp){
        ArrayList<VM> VMs = new ArrayList<>();
        ArrayList<VM> vms = (ArrayList<VM>) listVM.get(idApp);
        int size = vms.size();
        for(int i = 0; i < size; i++){
            VM vm = vms.get(i);
            if(vm.time == time){
                VM vmNew = new VM(vm);
                VMs.add(vmNew);
            }
        }
        return VMs;
    }
     public PM getPM(int time, int idPM){
        int size = this.pms.size();
        PM pm = null;
        for(int i =0; i < size; i++ ){
            pm = this.pms.get(i);
            if(pm.time == time && pm.idPM == idPM){
                return pm;
            }
        }
        return pm;
    }
}