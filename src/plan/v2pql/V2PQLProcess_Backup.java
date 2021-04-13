/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import config.ConfigSystem;
import data.Test;
import monitor.PM;
import monitor.VM;
import plan.mraco.Ant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Khiet
 * Ý tưởng của thuật toán
 * Ta sẽ duyệt qua tập VM cần di trú, tại một VM trong danh sách cần di trú
 * ta sẽ chọn một máy PM an toàn để di trú. Thuật toán sẽ lặp lại nhiều lần 
 * cho tới khi nào Q đạt hội tụ. Tại mỗi lần chọn thành công một PM cho VM 
 * ta sẽ tính toán Reward và cập nhật giá trị trong Q-Value. Giá trị khởi tạo
 * ban đầu cho giá trụ Q-Value=0.1
 */
public final class V2PQLProcess_Backup {

    ArrayList<PM> safePM;
    ArrayList<PM> buSafePM;
    ArrayList<PM> pms;
    ArrayList<VM> migrateVM;
    ArrayList<VM> buMigrateVM;
    ArrayList<VM> vms;
    int numVM = 0;
    int numPM = 0;
    Ant ant;
    int timestep = 1000;

    //private double[][] R;       // Reward lookup
    private ArrayList<QValue> QTable;    // Q learning
    //private double[][] buQ;
    private int step;
    private double epsilon, gamma, nguy;
    private int iteration = 0;
    private ArrayList<Action> actions = null;
    /*Export experiment*/
    private ArrayList<Reward> rewards = new ArrayList<>();
    private Map<Double, List<Double>> epReward = new HashMap<>();
    double preReward = 0.0;
    List<Double> saveLB = new ArrayList<>();
    //results
    HashMap<Integer, Integer> migrateVM2PM = new HashMap<>();
    double[] avgrewards;
    
    public V2PQLProcess_Backup (ArrayList<PM> pms, 
                        ArrayList<VM> vms,
                        ArrayList<VM> migrateVM,
                        ArrayList<PM> safePM,
                        double epsilon,
                        double gamma,
                        double nguy
                         ) {
        this.migrateVM = migrateVM;
        this.pms = pms;
        this.safePM = safePM;
        this.vms = vms;
        this.numVM = migrateVM.size();
        this.numPM = safePM.size();
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.nguy = nguy;
        this.QTable = new ArrayList<>();
        this.actions = new ArrayList<>();
        for(int i  = 0; i < numPM; i++){
            actions.add(new Action(i));
        }
        avgrewards = new double[timestep];
        for(int i=0; i < timestep; i++){
            avgrewards[i] = 0;
        }
        
    }

   

    public void run() {
        calculateQ();
        QTableUtility.printQ(QTable);
        /*
        System.out.println("Reward...........................................");
        for (double lb : saveLB){
            System.out.println(lb);
        }*/

    }

    //Training phase
    void calculateQ() {
        buSafePM = (ArrayList<PM>) safePM.clone();
        buMigrateVM = (ArrayList<VM>) migrateVM.clone();
        
        Random rand = new Random();
        State crtState = null;
        State nextState = null;
        PM pm = null;
        VM vm = null;
       

        /*initialize state*/
        
        step = 0;
        int idVM = -1;
        
        migrateVM = (ArrayList<VM>) buMigrateVM.clone();
        iteration = 0;
        safePM = (ArrayList<PM>) buSafePM.clone();
        //System.out.println("#Iteration: " + iteration);
        for (int i = 0; i < migrateVM.size(); i++) {
            vm =migrateVM.get(i);
            System.out.println("------------VM:" + i + " Type:" + vm.getType()+"--------");
            //1. init
            crtState = SystemState.getState(safePM,vm,numPM);                
            //2. Choose action a for current state i 
            //using policy derived from Q by Epsilon-Greedy
            iteration = 0;
            int t = 0;
            while (t<timestep) {
                Action action = null;
                if ((1 - rand.nextDouble()) < epsilon) {
                    action = choosePMRandom(rand,vm);
                    if(action != null)
                        initQTable(crtState,action);
                } else {
                    action = choosePMFromQ(crtState,vm);
                }
                //3. Action taken a and let system goes to next state i^'
                if(action != null){
                    pm = safePM.get(action.getToPM());
                    pm.addVM2PM(vm);
                    System.out.println("  ID:" + vm.idVM + " ,VM Type: " + vm.getType() + " ==>" + "PM" + action.getToPM());
                    //Observer r, nextState
                    nextState = SystemState.getState(safePM,vm,numPM);  
                    //printState(nextState);
                    //4. Calculate the reinforcement signal
                    //Q(state,action)= Q(state,action) + nguy * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))

                    double q_value = getQValueFromQTable(nextState,action);
                    if(q_value==-1){
                        q_value = 0.1;
                        QTable.add(new QValue(QTable.size()-1,nextState, action,iteration,q_value));
                    }
                    double maxQ = maxQ(nextState);
                    double reward = Objective.calculateReward(safePM,  action, 2);
                    double value = q_value + nguy*(reward + gamma * maxQ - q_value);
                    updateQTable(crtState, action, value);
                    //System.out.println("Q-Value:"+value);
                    //saveLB.add(reward);
                    avgrewards[t] += reward;
                    pm.removeRecentVM(vm);
                    System.out.println("TIMESTEP:"+t+" REWARD:"+reward);

                }
                iteration++;
                t++;
            }
            //choose action from Q-Table
            Action action = choosePMFromQ(crtState,vm);
            if(action != null){
                pm = safePM.get(action.getToPM());
                pm.addVM2PM(vm);
                double q_value = getQValueFromQTable(nextState,action);
                double maxQ = maxQ(nextState);
                double reward = Objective.calculateReward(safePM, action, 2);
                double value = q_value + nguy*(reward + gamma * maxQ - q_value);
                updateQTable(crtState, action, value);
            }
           
            
            
        }
           
            
            
        
        
        try {
            ExportExperiment.writeAverageReward(epsilon, avgrewards,  timestep);
        } catch (IOException ex) {
            Logger.getLogger(V2PQLProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
        
    }
   
    private Action choosePMRandom(Random rand, VM vm) {
        //System.out.println(" #Choose randomly");
        int i = 0;
        while (i < numPM) {
            int j = rand.nextInt(numPM);            
            PM pm = safePM.get(j);
            if (pm.addableVM(vm)) {
                //System.out.println("  ID:" + vm.idVM + " ,VM Type: " + vm.type + " ==>" + "PM" + j);
                Action action = new Action(j);
                
                return action;
            }
            i++;
        }
        return null;
    }
    private void initQTable(State state, Action action) {
        double q_value = getQValueFromQTable(state,action);
        if(q_value==-1){
            q_value = 0.1;
            QTable.add(new QValue(QTable.size()-1,state, action,iteration,q_value));
        } 
    }
    private void updateQTable(State state, Action action,double value){
        double q_value = getQValueFromQTable(state,action);
        if(q_value==-1){
            q_value = 0.1;
            QTable.add(new QValue(QTable.size()-1,state, action,iteration,q_value));
        }else{
            int size = QTable.size();
            for(int i = 0; i < size; i++){
                QValue qvalue = QTable.get(i);
                if(qvalue.getAction().getToPM() == action.getToPM()){
                    State s = qvalue.getState();
                    if(s.getStateLoadBalance() == state.getStateLoadBalance() && s.getTypeVM() == state.getTypeVM()){
                        int[] statePMs = s.getStatePMs();
                        int count = 0;
                        while(count < statePMs.length){
                            if(statePMs[count] == state.getStatePMs()[count]){
                                qvalue.addValue(iteration, value);
                                return;
                            }
                                
                            count++;
                        }
                    }
                }
            }
             
        }
            
    }

    
    private Action choosePMFromQ(State state,VM vm) {
        //System.out.println(" #Choose Q-Value");
        double maxValue = -10;
       
        ArrayList<Action> atcs =new ArrayList<>();
        for (int j = 0; j < numPM; j++) {
            PM pm = safePM.get(j);
            if (pm.addableVM(vm)) {
                double q_value = getQValueFromQTable(state,actions.get(j));
                if(q_value==-1){
                    q_value = 0.1;
                    QTable.add(new QValue(QTable.size()-1,state, actions.get(j),iteration,q_value));
                }
                atcs.add(new Action(j,q_value));
            }
        }
        Action act = null;
        for(int i = 0; i < atcs.size();i++){
            if(maxValue < atcs.get(i).getQ_value()){
                maxValue = atcs.get(i).getQ_value();
                act = atcs.get(i);
            }
        }   
        return null;
    }
    
    
    private double getQValueFromQTable(State state, Action action) {
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
   
    void printState(State state){
        System.out.print("State Syste:  LoadBalance:"
                 +state.getStateLoadBalance());
        int[] statePMs = state.getStatePMs();
        for(int j = 0; j<statePMs.length;j++ ){
            System.out.print(" PM"+j+":"+statePMs[j]+" ");
        }
        System.out.println();
    }
    
    double maxQ(State nextState) {
        double maxValue = 0.1;
        for (int j = 0; j < numPM; j++) {
            if(nextState != null){
                double q_value = getQValueFromQTable(nextState,actions.get(j));
                if (q_value >= maxValue) {
                    maxValue = q_value;
                }
            }
        }
        return maxValue;
    }
    
/*

    

   
    

    private double calR(int index, int toPM) {
        System.out.println(" #Calculate Reward");
        VM vm = migrateVM.get(index);
        PM pm = safePM.get(toPM);
        System.out.println("  pre load balancing: " + PM.CalculatePMsLoadBalancing(safePM));
        pm.addVM2PM(vm);
        double loadBalancing = PM.CalculatePMsLoadBalancing(safePM);
        if (loadBalancing > 0.99)
        loadBalancing = 0.99;
        System.out.println("  load balancing: " + loadBalancing);
        System.out.println("  state: " + (((int) Math.floor(loadBalancing / 0.2)) * State.TYPE_VM_QTY + vm.type) + " system:" + (int) Math.floor(loadBalancing / 0.2) + " vmtype:" + vm.type + " idPM: " + pm.idPM);
        pm.removeRecentVM(vm);
        return 1 / loadBalancing;
    }

    private void calBuQ() {
        for (int i = 0; i < State.STATE_COUNT; i++) {
            System.arraycopy(Q[i], 0, buQ[i], 0, safePM.size());
        }
    }

    // Used for debug
    void printR(int[][] matrix) {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i <= 8; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < State.STATE_COUNT; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < numPM; j++) {
                System.out.printf("%4s", matrix[i][j]);
            }
            System.out.println("]");
        }
    }

    

    void printR() {
        System.out.println("R matrix");
        for (int i = 0; i < R.length; i++) {
            System.out.print("From state " + i + ":  ");
            for (int j = 0; j < R[i].length; j++) {
                System.out.printf("%6.2f ", (R[i][j]));
            }
            System.out.println();
        }
    }


    
*/

    
   
    
    
    
}
