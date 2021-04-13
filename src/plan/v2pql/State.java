/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import config.ConfigSystem;
import java.util.ArrayList;
import monitor.PM;

/**
 *
 * @author Khiet
 */
public class State {
    
    private int stateLoadBalance;
    private int typeVM;
    private int[] statePMs;

    public State(int stateLoadBalance,  int[] statePMs,int typeVM, int  numPMs) {
        this.stateLoadBalance = stateLoadBalance;
        this.statePMs = new int[numPMs];
        for(int i = 0; i < numPMs; i++){
            this.statePMs[i] = statePMs[i];
        }
        this.typeVM = typeVM;
    }

    public int getStateLoadBalance() {
        return stateLoadBalance;
    }

    public int getTypeVM() {
        return typeVM;
    }

    public int[] getStatePMs() {
        return statePMs;
    }
    

    
  
}
