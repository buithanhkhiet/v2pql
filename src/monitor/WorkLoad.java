/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

/**
 *
 * @author Administrator
 */
public class WorkLoad {
    public int time;
    public int idApp;
    public double workoad;

    public WorkLoad(int time, int idApp, double workoad) {
        this.time = time;
        this.idApp = idApp;
        this.workoad = workoad;
    }
    
}
