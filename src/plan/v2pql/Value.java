/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

/**
 *
 * @author Khiet
 */
public class Value {
    private int iteration;
    private double value;

    public Value(int iteration, double value) {
        this.iteration = iteration;
        this.value = value;
    }

    public int getInteration() {
        return iteration;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
}
