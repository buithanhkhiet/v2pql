/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import monitor.PM;
import monitor.VM;

/**
 *
 * @author Khiet
 */
public class QTableUtility {
    public static void printQ(ArrayList<QValue> QTable) {
   
        System.out.println("Q matrix");
        for (int i = 0; i < QTable.size(); i++) {
            System.out.print("State " + i + ":  LoadBalance:"
                 +QTable.get(i).getState().getStateLoadBalance());
            int[] statePMs = QTable.get(i).getState().getStatePMs();
            for(int j = 0; j<statePMs.length;j++ ){
                System.out.print(" PM"+j+":"+statePMs[j]+" ");
            }
            System.out.print(" TypeVM:"+QTable.get(i).getState().getTypeVM()
                            +" Action:"+QTable.get(i).getAction().getToPM()
                            +" Value:"+QTable.get(i).getValue().get(QTable.get(i).getValue().size()-1).getValue());
            System.out.println();
        }

    }
    public static QValue maxQValue(ArrayList<QValue> QTable) {
        QValue qvaluemax=null;
        double maxValue = -99999;
        int size = QTable.size();
        for (int i = 0; i < size; i++) {
            QValue qvalue = QTable.get(i);
            int index = qvalue.getValue().size();
            double value = qvalue.getValue().get(index-1).getValue();
            if (maxValue <=value ){
                maxValue = value;
                qvaluemax=qvalue;
            }
        }
        
        return qvaluemax;
    }
    public static double getQValueFromQTable(State state, Action action, ArrayList<QValue> QTable) {
        int size = QTable.size();
        double result = -1;
        if(action !=null && action.getToPM() != -1 && state != null){
            for(int i = 0; i < size; i++){
                QValue q_value = QTable.get(i);
                //System.out.println("$$$$$$$ "+i+"%"+q_value.getAction().getToPM()+"%"+action.getToPM());
                if(q_value.getAction().getToPM() == action.getToPM()){
                    State s = q_value.getState();
                    if(s.getStateLoadBalance() == state.getStateLoadBalance() && s.getTypeVM() == state.getTypeVM()){
                        int[] statePMs = s.getStatePMs();
                        int count = 0;
                        while(count < statePMs.length){
                            if(statePMs[count] == state.getStatePMs()[count])
                                return q_value.getValue().get(q_value.getValue().size()-1).getValue();
                            count++;
                        }
                    }
                }
            }
        }
        return result;
    }
    public static QValue minQValue(ArrayList<QValue> QTable) {
        QValue qvaluemin=null;
        double minValue = 9999;
        int size = QTable.size();
        for (int i = 0; i < size; i++) {
            QValue qvalue = QTable.get(i);
            int index = qvalue.getValue().size();
            double value = qvalue.getValue().get(index-1).getValue();
            if (minValue >= value ){
                minValue = value;
                qvaluemin=qvalue;
            }
        }
        
        return qvaluemin;
    }
    public static void printState(State state){
        System.out.print("State Syste:  LoadBalance:"
                 +state.getStateLoadBalance());
        int[] statePMs = state.getStatePMs();
        for(int j = 0; j<statePMs.length;j++ ){
            System.out.print(" PM"+j+":"+statePMs[j]+" ");
        }
        System.out.println();
    }
    public static Action choosePMRandom(Random rand, VM vm, int numPM, ArrayList<PM> buSafePM) {
        //System.out.println(" #Choose randomly");
        int i = 0;
        while (i < numPM) {
            int j = rand.nextInt(numPM);            
            PM pm = buSafePM.get(j);
            if (pm.addableVM(vm)) {
                //System.out.println("  ID:" + vm.idVM + " ,VM Type: " + vm.type + " ==>" + "PM" + j);
                Action action = new Action(j);
                return action;
            }
            i++;
        }
        return null;
    }
    public static Action choosePMFromQ(State state,VM vm,ArrayList<PM> buSafePM,ArrayList<Action> actions, ArrayList<QValue> QTable) {
        //System.out.println(" #Choose Q-Value");
        double maxValue = -99999;
       
        ArrayList<Action> atcs =new ArrayList<>();
        int numPM = buSafePM.size();
        for (int j = 0; j < numPM; j++) {
            PM pm = buSafePM.get(j);
            if (pm.addableVM(vm)) {
               double q_value = getQValueFromQTable(state,actions.get(j),QTable);
               atcs.add(new Action(j,q_value));
            }
        }
        Action act = null;
        for(int i = 0; i < atcs.size();i++){
            if(maxValue <= atcs.get(i).getQ_value()){
                maxValue = atcs.get(i).getQ_value();
                act = atcs.get(i);
            }
        }   
        return act;
    }
    public static double maxQ(State nextState, int numPM,ArrayList<QValue> QTable ) {
        double maxValue = 0.1;
        for (int j = 0; j < numPM; j++) {
            if(nextState != null){
                Action action = new Action(j);
                double q_value = QTableUtility.getQValueFromQTable(nextState, action, QTable);// getQValueFromQTable(nextState,actions.get(j));
                if (q_value >= maxValue) {
                    maxValue = q_value;
                }
            }
        }
        return maxValue;
    }
   
    
   
    
}
