/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.roundrobin;

import config.ConfigSystem;
import helper.MyRound;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import monitor.PM;
import monitor.VM;
import plan.Constraints;
import plan.ProblemSet;
import plan.mraco.Ant;
import plan.v2pql.Action;
import plan.v2pql.ExportExperiment;
import plan.v2pql.Objective;
import plan.v2pql.Player;
import plan.v2pql.QValue;

import plan.v2pql.V2PQLPolicy;

/**
 *
 * @author Mr. Khiet
 */
public class RoundRobinProcess {
  
    int numPM;
   
    String fileName;
   
    ArrayList<PM> safePM;
    ArrayList<PM> buSafePM;
    ArrayList<VM> migrateVM;
    int anpha;
    private ArrayList<Action> actions = null;
    double[][] results = null;
    ArrayList<Player> players = null;
    public RoundRobinProcess(
            
            ArrayList<VM> migrateVM,
            ArrayList<PM> safePM,
            int anpha
            ){
       

        this.numPM = safePM.size();
       
        this.anpha = anpha;
        this.safePM = safePM;
        this.migrateVM = migrateVM;
        this.actions = new ArrayList<>();
        this.players = new ArrayList<>();
        for(int i  = 0; i < numPM; i++){
            actions.add(new Action(i));
            players.add(new Player(i,this.safePM.get(i).idPM));
        }
 
        results = new double[this.migrateVM.size()][5]; //indexVM, idPM,utilization,loadbalance,payoff
        
    }
     public void run(){
        this.buSafePM = initPMInfor();
        ArrayList<Objective> objs = new ArrayList<>();
        //int[] newSolutions = new int[numVM];
        for(int i = 0; i <  this.migrateVM.size(); i++){
            VM vm = migrateVM.get(i);
            Action action = selectNextHost(i,vm,this.buSafePM);
            results[i][0] = i;
            
            if(action==null){
                results[i][1] = 0;
                results[i][2] = 0;
                results[i][3] = 0;
                results[i][4] = 0;
            }
            else{
                PM pm = buSafePM.get(action.getToPM());
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
               
            }
            
        }
        try {
            ExportExperiment.writeRoundRobinProblem(results, migrateVM.size());
            ExportExperiment.writePlayers(players, "roundrobin");
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

    private Action selectNextHost(int index, VM vm, ArrayList<PM> buSafePM) {
        
        
        ArrayList<Action> atcs =new ArrayList<>();
        for (int j = 0; j < numPM; j++) {
            PM pm = buSafePM.get(j);
            if (pm.addableVM(vm)) {               
                atcs.add(new Action(j));
            }
        }
        int indexPM = index%atcs.size();
        
        return atcs.get(indexPM);
        
    }
}
