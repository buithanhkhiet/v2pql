/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

import analyze.PMs;
import config.ConfigSystem;
import helper.MyRound;


/**
 *
 * @author Administrator
 * Moi VM doc toan bo du lieu theo thoi gian t
 */
public class VM {
    public int time;
    public int idVM;
    public int idApp;
    public int row;//tier
    public int column;//pm
    private double cpu, ram, disk;
    public double c,s,z,lambda,r;
    private double ccpu, cram, cdisk; //capcity
    private double ucpu, uram, udisk; //utility
    private double acpu, aram, adisk; //avilable
    private int type;
    public int index;

    public VM() {
    }
    
    

    public VM(int time, int idVM, int idApp, int row, int column, double cpu, double ram, double disk, double c, double s, double z, double lambda, double r, double ccpu, double cram, double cdisk, int type) {
        this.time = time;
        this.idVM = idVM;
        this.idApp = idApp;
        this.row = row;
        this.column = column;
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
        this.c = c;
        this.s = s;
        this.z = z;
        this.lambda = lambda;
        this.r = r;
        this.ccpu = ccpu;
        this.cram = cram;
        this.cdisk = cdisk;
        this.type = type;
    }

    
    public VM (VM vm){
        this.idVM = vm.idVM;
        this.idApp = vm.idApp;
        this.time = vm.time;
        this.row = vm.row;
        this.column = vm.column;
        this.cpu = vm.cpu;
        this.ram = vm.ram;
        this.disk = vm.disk;
        this.c = vm.c;
        this.s = vm.s;
        this.z = vm.z;
        this.lambda = vm.lambda;
        this.r = vm.r;
        this.ccpu = vm.ccpu;
        this.cram = vm.cram;
        this.cdisk = vm.cdisk;
        this.type = vm.getType();
    }
    public double getNguy(){
        return s*c/z;
    }    
    public double getAvailableResourceVM(){
        return MyRound.getRound(ConfigSystem.anpha[0]*(1-cpu)+ConfigSystem.anpha[1]*(1-ram)+ConfigSystem.anpha[2]*(1-disk));
    }
    
    public double getRho(){
        return lambda*r/getNguy();
    }
    
    public double getRho(int numVM){
     return MyRound.getRound(lambda/(numVM*getNguy()));
    }
    
    public double getCost(PMs pms){
        
        return this.ccpu*(ConfigSystem.a[0]*pms.ucpu[this.column]+ConfigSystem.a[1]);
        //return MyRound.getRound(ConfigSystem.a[0]*this.c + ConfigSystem.a[1]);
    }

    public double getUcpu() {
        return cpu*ccpu;
    }

    public double getUram() {
        return ram*cram;
    }

    public double getUdisk() {
        return disk*cdisk;
    }

    public double getAcpu() {
        return ccpu-cpu*ccpu;
    }

    public double getAram() {
        return cram-ram*cram;
    }

    public double getAdisk() {
        return cdisk - disk*cdisk;
    }

    public double getCcpu() {
        return ccpu;
    }

    public double getCram() {
        return cram;
    }

    public double getCdisk() {
        return cdisk;
    }

    public void setCcpu(double ccpu) {
        this.ccpu = ccpu;
    }

    public void setCram(double cram) {
        this.cram = cram;
    }

    public void setCdisk(double cdisk) {
        this.cdisk = cdisk;
    }

    public int getType() {
        return type;
    }
    
    

}
