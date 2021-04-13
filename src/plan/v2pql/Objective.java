/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import java.util.ArrayList;
import monitor.PM;

/**
 *
 * @author Khiet
 */
public class Objective {
    private ArrayList<PM> buSafePM;
    private PM pm;
    private double loadbalance;
    private double utilization;
    private double payoff;
    

    private int anpha;
    public Objective(ArrayList<PM> buSafePM, PM pm, int anpha) {
        this.buSafePM = buSafePM;
        this.pm = pm;
        this.anpha = anpha;
        loadbalance = PM.CalculatePMsLoadBalancing(buSafePM);
        utilization = pm.getH();
        //payoff = -(1-anpha)*utilization-loadbalance;
        // payoff = Math.sqrt(Math.pow(utilization-loadbalance, 2));
        payoff = 1/utilization+1/loadbalance;
    }

    public double getLoadbalance() {
        return loadbalance;
    }

    public double getUtilization() {
        return utilization;
    }

    public double getPayoff() {
        return payoff;
    }
    public static double calculateReward(ArrayList<PM> safePM, Action action, int anpha){
        double reward = 0;
        double l= 1/PM.CalculatePMsLoadBalancing(safePM);
        PM pm =safePM.get(action.getToPM());
        double h=1/pm.getH();
        //reward = Math.sqrt(Math.pow(l-h, 2));
        //reward = ((1-anpha)*h-l);
        reward = h+l;
        return reward;
    }
    
    
    
    
}
