/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import java.util.ArrayList;
import monitor.PM;
import monitor.VM;

/**
 *
 * @author Khiet
 */
public class SystemState {
     public static State getState(ArrayList<PM> safePM, VM vm, int numPM) {
        
       
       int stateLoadBalancing = (int) FuzzyState.membershipLoadBalance(PM.CalculatePMsLoadBalancing(safePM))[0];
       int[] statePMs = new int[safePM.size()];
       for(int i = 0; i < numPM; i++){
           
           statePMs[i] = (int) FuzzyState.membershipUtilization(safePM.get(i).getH())[0] ;
           //System.out.println("###### PM"+i+":"+safePM.get(i).getUtilizationLoad());
       }
       
       return new State(stateLoadBalancing,statePMs,vm.getType(), numPM);
       
    }
}
