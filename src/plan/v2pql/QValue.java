/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import config.ConfigSystem;
import java.util.ArrayList;

/**
 *
 * @author Khiet
 */
public class QValue {
    private int id;
    private State state;
    private Action action;
    private ArrayList<Value> value;

    public QValue(int id,State state, Action action, int iteration, double value) {
        this.id = id;
        this.state = state;
        this.action = action;
        this.value = new ArrayList<>();
        this.value.add(new Value(iteration,value));
    }

    public ArrayList<Value> getValue() {
        return value;
    }

    public void setValue(ArrayList<Value> value) {
        this.value = value;
    }
    public void addValue(int iteration, double value){
        for(int i = 0; i < this.value.size(); i++){
            if(this.value.get(i).getInteration() == iteration){
                this.value.get(i).setValue(value);
                return;
            }
        }
        this.value.add(new Value(iteration,value));
    }


    public State getState() {
        return state;
    }

   

    public Action getAction() {
        return action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    
    

  
    
}
