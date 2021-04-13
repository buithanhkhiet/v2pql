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
public class Reward {
    private int timestep;
    private double value;

    public Reward(int timestep, double value) {
        this.timestep = timestep;
        this.value = value;
    }

    public int getTimestep() {
        return timestep;
    }

    public double getValue() {
        return value;
    }
    
   
    
}
