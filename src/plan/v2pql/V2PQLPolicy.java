/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import config.ConfigSystem;
import helper.CSVFileReader;
import helper.MyRound;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import monitor.PM;
import monitor.VM;



/**
 *
 * @author Khiet
 */
public class V2PQLPolicy {
    ArrayList<QValue> QTable = null;
    int numPM;
    double epsilon;
    String fileName;
    int iteration;
    ArrayList<PM> safePM;
    ArrayList<PM> buSafePM;
    ArrayList<VM> migrateVM;
    int anpha;
    private ArrayList<Action> actions = null;
    double[][] results = null;
    ArrayList<Player> players = null;
    public V2PQLPolicy(
            double epsilon,
            int iteration,
            ArrayList<VM> migrateVM,
            ArrayList<PM> safePM,
            int anpha
            ){
        this.epsilon = epsilon;
        fileName= "./data/policies/policy_"+ConfigSystem.sdataset+"_epsilon_"+epsilon+".csv";
        this.numPM = safePM.size();
        this.iteration = iteration;
        this.QTable = new ArrayList<>();
        this.safePM = safePM;
        this.migrateVM = migrateVM;
        this.actions = new ArrayList<>();
        players = new ArrayList<>();
        for(int i  = 0; i < numPM; i++){
            actions.add(new Action(i));
            players.add(new Player(i,this.safePM.get(i).idPM));
        }
        this.anpha = anpha;
        results = new double[this.migrateVM.size()][5]; //indexVM, idPM,utilization,loadbalance,payoff
        
        
        
        
        
    }
    public void run(){
        //read policy file of Q-Learning
        readFilePolicy(fileName,epsilon,numPM,iteration);
        QTableUtility.printQ(QTable);
        //migrate VM to PM based on Q-Table policy
        int i = 0;
        this.buSafePM = initPMInfor();
        ArrayList<Objective> objs = new ArrayList<>();
        for (VM vm : migrateVM) {
            System.out.println("------------VM:" +i+ " Type:" + vm.getType()+"--------");
            results[i][0] = i;
            //get current state
            State crtState  = SystemState.getState(buSafePM,vm,numPM);
            QTableUtility.printState(crtState);
            //choose the optimal PM for migrating VM
            Action action = QTableUtility.choosePMFromQ(crtState, vm, buSafePM, actions, QTable);
            
            PM pm = buSafePM.get(action.getToPM());
            if(!pm.addableVM(vm)){
                results[i][1] = -1;
                results[i][2] = -1;
                results[i][3] = -1;
                results[i][4] = -1;
            }
            else{
                pm.addVM2PM(vm);
                ExportExperiment.updatePlayerInfor(players,pm,this.buSafePM,anpha);
                results[i][1] = action.getToPM();
                System.out.println("  ID:" + i+ " ,VM Type: " + vm.getType() + " ==>" + "PM" + action.getToPM());
                System.out.println("PM\tccpu\tcram\tcdisk\tcpu(%)\tram(%)\tdisk(%)\th");
                System.out.println(action.getToPM()
                            +"\t"+MyRound.getRound(pm.getCcpu())
                            +"\t"+MyRound.getRound(pm.getCram())
                            +"\t"+MyRound.getRound(pm.getCdisk())
                            +"\t"+MyRound.getRound(pm.getCpu())
                            +"\t"+ MyRound.getRound(pm.getRam())
                            +"\t"+ MyRound.getRound(pm.getDisk())
                            +"\t"+ MyRound.getRound(pm.getUtilizationLoad())
                    );

                Objective obj = new Objective(buSafePM, this.buSafePM.get(action.getToPM()),anpha);
                objs.add(obj);
                System.out.println("$$$$ LoadBalance: "+obj.getLoadbalance()
                        +" Utilization:"
                        +obj.getUtilization()
                        +" PayOff:"+obj.getPayoff()+" $$$$");

                results[i][2] = obj.getUtilization();
                results[i][3] = obj.getLoadbalance();
                results[i][4] = obj.getPayoff();
                i++;
            }
            
        }
        try {
            ExportExperiment.writeProblem(epsilon, results, migrateVM.size());
            ExportExperiment.writePlayers(players, "epsilon_"+epsilon+"_");
        } catch (IOException ex) {
            Logger.getLogger(V2PQLPolicy.class.getName()).log(Level.SEVERE, null, ex);
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
    void readFilePolicy(String fileName,double epsilon, int numPM, int iteration){
        
        /*Read Q - start*/

        CSVFileReader policydata = new CSVFileReader(fileName);
        policydata.ReadFile();
        int numrow = policydata.getRowsNum();
        int id;
        int stateLoadBalance;
        int typeVM;
        Action action;
        double value;
        
        for(int i = 1; i < numrow; i++){
            
            id = (int) policydata.getValue(i, 0);
            stateLoadBalance = (int) policydata.getValue(i, 1);
            typeVM = (int) policydata.getValue(i, 2);
            action = new Action((int) policydata.getValue(i, 3));
            value = (double) policydata.getValue(i, 4);
            int[] statePMs = new int[numPM];
            for(int j = 0; j < numPM; j++){
                statePMs[j] = (int) policydata.getValue(i, 5+j);
            }
            State s = new State(stateLoadBalance, statePMs, typeVM, numPM);
            QValue qvalue = new QValue(id, s, action, iteration, value);
            this.QTable.add(qvalue);
        }

                
        /*Read Q - end*/
        
    }

    
    
    
    
}
