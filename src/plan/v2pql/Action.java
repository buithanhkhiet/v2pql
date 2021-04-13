/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

/**
 *
 * @author User
 */
public class Action {
   
    private int toPM;
    private double q_value;

    public Action() {
    }

    public Action(int toPM) {
        
        
        this.toPM = toPM;
    }

    public Action(int toPM, double q_value) {
        this.toPM = toPM;
        this.q_value = q_value;
    }
    
    public Action(Action a){
        
        this.toPM = a.toPM;
    }

    public double getQ_value() {
        return q_value;
    }

    public void setQ_value(double q_value) {
        this.q_value = q_value;
    }
    
    

    

   
   
    public int getToPM() {
        return toPM;
    }

    public void setToPM(int toPM) {
        this.toPM = toPM;
    }
}
