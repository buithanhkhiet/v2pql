/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyze;

import helper.MyRound;
import java.util.ArrayList;
import plan.Solution;

/**
 *
 * @author Administrator
 */
public class QueueingNetwork {
    
    public static double nguy(double s, double c, double z){
        return s*c/z;
    }
    
    public static double lambda(double l,double r){
        return l*r;
    }
    public static double rho(double l, double r, double nguy){
        return lambda(l,r)/nguy;
    }
    public static double L(double[] rhos){
        int size = rhos.length;
        double l =0;
        for(int i = 0; i <size; i++){
            l+=rhos[i]/(1-rhos[i]);
        }
        return l;
    }
    public static double beta(double[] lambdas){
        int size = lambdas.length;
        double b =0;
        for(int i = 0; i <size; i++){
            b+=lambdas[i];
        }
        return b;
    }
    public static double responsetime(double[] rhos, double[] lambdas){
        
        return MyRound.getRound(L(rhos)/beta(lambdas));
    }
    public static double omega(double[] cpus){
        int size = cpus.length;
        double omg =0;
        for(int i = 0; i < size; i++){
            omg += cpus[i];
        }
        return omg;
    }
    public static double totalResponseTime(ArrayList<Solution> s){
        double responsetime = 0;
        int size =s.size();
        for(int i = 0; i < size; i++){
            responsetime += s.get(i).responsetime;
        }
        //return responsetime/ConfigSystem.numApplications;
        return responsetime;
    }
    
    
}
