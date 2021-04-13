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
public class Action {
    public int time;
    public int[] action4PMs;
    public int[] action4Apps;

    public Action(int time, int[] action4PMs, int[] action4Apps) {
        this.time = time;
        this.action4PMs = action4PMs;
        this.action4Apps = action4Apps;
    }
    public Action(Action a){
        this.time = a.time;
        this.action4Apps = a.action4Apps.clone();
        this.action4PMs = a.action4PMs.clone();
    }
    
            
}
