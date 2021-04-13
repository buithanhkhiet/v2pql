/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import analyze.Analysing;
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
public final class V2PQLProcess {

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
    double[] discountreward;
    double[][] discountrewards;
    
    public V2PQLProcess (ArrayList<PM> pms, 
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
        discountreward = new double[timestep];
        for(int i=0; i < timestep; i++){
            avgrewards[i] = 0;
            discountreward[i]=0;
        }
        discountrewards = new double[timestep][numVM];
        for(int i = 0; i < timestep; i++){
            for(int j = 0; j < numVM; j++){
                discountrewards[i][j]=0;
            }
        }
        
    }

   

    public void run() {
        calculateQ();
        //QTableUtility.printQ(QTable, migrateVM2PM);
        /*
        System.out.println("Reward...........................................");
        for (double lb : saveLB){
            System.out.println(lb);
        }*/

    }

    //Training phase
    void calculateQ() {
        
        buMigrateVM = (ArrayList<VM>) migrateVM.clone();
        this.QTable = new ArrayList<>();
        
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
        double[] timestepdiscreward = new double[timestep];
        while (iteration<timestep) {
            //safePM = (ArrayList<PM>) buSafePM.clone();
            buSafePM = initPMInfor();//(ArrayList<PM>) safePM.clone();
            //System.out.println("#Iteration: " + iteration);
            double timestepreward = 0;
            double timestepdiscountedreward=0;
            
            for (int i = 0; i < migrateVM.size(); i++) {
                vm =migrateVM.get(i);
                //System.out.println("------------VM:" + i + " Type:" + vm.getType()+"--------");
                //1. init
                crtState = SystemState.getState(buSafePM,vm,numPM);                
                //2. Choose action a for current state i 
                //using policy derived from Q by Epsilon-Greedy
                Action action = null;
                int flag1=0;
                int flag2=0;
                if ((1 - rand.nextDouble()) < epsilon) {
                    action =  QTableUtility.choosePMRandom(rand, vm, numPM, buSafePM);// choosePMRandom(rand,vm);
                    if(action != null){
                        initQTable(crtState,action);
                        flag1=1;
                    }
                    else{
                        action = QTableUtility.choosePMFromQ(crtState, vm, buSafePM, actions, QTable);
                    }  
                   
                } else {
                    action = QTableUtility.choosePMFromQ(crtState, vm, buSafePM, actions, QTable);
                    flag2=1;
                }
                //3. Action taken a and let system goes to next state i^'
                if(action !=null){
                                
                    pm = buSafePM.get(action.getToPM());
                    pm.addVM2PM(vm);
                    
                    //System.out.println("  ID:" + vm.idVM + " ,VM Type: " + vm.getType() + " ==>" + "PM" + action.getToPM());
                    //Observer r, nextState
                    nextState = SystemState.getState(buSafePM,vm,numPM);  
                    //printState(nextState);
                    //4. Calculate the reinforcement signal
                    //Q(state,action)= Q(state,action) + nguy * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))

                    double q_value = QTableUtility.getQValueFromQTable(crtState, action, QTable);//getQValueFromQTable(nextState,action);
                    if(q_value==-1){
                        q_value = -0.1;
                        QTable.add(new QValue(QTable.size()-1,nextState, action,iteration,q_value));
                    }
                    double maxQ = QTableUtility.maxQ(nextState, numPM, QTable);// maxQ(nextState);
                    double reward = Objective.calculateReward(buSafePM, action, 2);
                    double value = q_value + nguy*(reward + gamma * maxQ - q_value);
                    updateQTable(crtState, action, value);
                    //System.out.println("Q-Value:"+value);
                    //saveLB.add(reward);
                    timestepreward += value;
                    timestepdiscountedreward += reward;
                    //timestepdiscountedreward += Math.pow(nguy,i)*reward;
                    //pm.removeRecentVM(vm);
                }
                else
                {
                    
                    System.out.println("############## ACTION NULL ##############"+flag1+"$"+flag2);
                    System.out.println("iteration:"+iteration);
                    
                    System.out.println("  ID:" + vm.idVM + " ,VM Type: " 
                            + vm.getType()
                            +" cpu:"+vm.getCcpu()
                            +" ram:"+vm.getCram()
                            +" disk:"+vm.getCdisk()
                            );
                    System.out.println("PM\tAcpu\tAram\tAdisk");
                    for(int j = 0; j < numPM; j++){
                        PM pm1 = safePM.get(j);
                        System.out.println(j+"\t"+pm1.getAcpu()+"\t"+pm1.getAram()+"\t"+pm1.getAdisk());
                    }                    
                }
            }
            avgrewards[iteration] = timestepreward/numVM;
            /*
            for(int j = 0; j < numVM; j++){
                discountrewards[iteration][j]=timestepdiscreward[j]/numVM;
            }*/
            timestepdiscreward[iteration] = timestepdiscountedreward;
            iteration++;
        }     
        
        try {
            ExportExperiment.writeAverageReward(epsilon, avgrewards, timestep);
            ExportExperiment.writeDiscountedCumulateReward(epsilon, timestepdiscreward,nguy, timestep, numVM);
            ExportExperiment.writeQValue(epsilon, QTable);
            ExportExperiment.writePolicy(epsilon, QTable,numPM);
            
            
        } catch (IOException ex) {
            Logger.getLogger(V2PQLProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private ArrayList<PM> initPMInfor(){
        ArrayList<PM> pms = new ArrayList<>();
        for(int i = 0; i < safePM.size(); i++){
            PM pm = new PM(safePM.get(i));
            pms.add(pm);
        }
        return pms;                      
    }
   
    
    private void initQTable(State state, Action action) {
        double q_value = QTableUtility.getQValueFromQTable(state, action, QTable);//getQValueFromQTable(state,action);
        if(q_value==-1){
            q_value = 0.1;
            QTable.add(new QValue(QTable.size()-1,state, action,iteration,q_value));
        } 
    }
    private void updateQTable(State state, Action action,double value){
        double q_value = QTableUtility.getQValueFromQTable(state, action, QTable);//getQValueFromQTable(state,action);
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
   
   
    
    
}
