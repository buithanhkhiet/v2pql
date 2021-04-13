/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.mraco;


import java.util.ArrayList;
import monitor.PM;
import monitor.VM;
import plan.ProblemSet;
;

/**
 *
 * @author Mr. Khiet
 */
public class Ant {
   
    private double fitnessValue;
    private int[] solution;
    public ArrayList<PM> safePM = new ArrayList<>();
    public ArrayList<VM> vms = new ArrayList<>();
    private int numVM;
    
    public Ant( ArrayList<VM> vms, ArrayList<PM> safePM, int[] solution){
        this.numVM = vms.size();
       
        for(int i = 0 ; i < vms.size(); i++){
            this.vms.add(new VM(vms.get(i)));
        }
        for(int i = 0; i < safePM.size(); i++){
            this.safePM.add(new PM(safePM.get(i)));
        }
        this.solution = new int[this.numVM]; // index, value conresponse index of VM, id PM
        
        this.solution = solution;
    }
    public Ant(Ant ant){
        this.numVM = ant.vms.size();
       
        for(int i = 0 ; i < ant.vms.size(); i++){
            this.vms.add(new VM(ant.vms.get(i)));
        }
        for(int i = 0; i < ant.safePM.size(); i++){
            this.safePM.add(new PM(ant.safePM.get(i)));
        }
        this.solution = new int[this.numVM]; // index, value conresponse index of VM, id PM
        
        this.solution = ant.solution;
    }

    

    public double getFitnessValue() {
        fitnessValue = ProblemSet.evaluate(this);
        return fitnessValue;
    }
    public double[] getMetrics(){
        return ProblemSet.getMetrics(this);
    }

    public int[] getSolution() {
        return solution;
    }

    public void setSolution(int[] solution) {
        this.solution = solution;
    }
    
   
    
    
}
