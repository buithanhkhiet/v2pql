/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyze;

/**
 *
 * @author Administrator
 */
public class APPs {
    public int action;
    public int statesystem;
    public double workload;
    public double responsetime;
    public String sworkload;
    public String sresponsetime;

    public APPs(int action, int statesystem, double workload, double responsetime, String sworkload, String sresponsetime) {
        this.action = action;
        this.statesystem = statesystem;
        this.workload = workload;
        this.responsetime = responsetime;
        this.sworkload = sworkload;
        this.sresponsetime = sresponsetime;
    }
    
    
}
