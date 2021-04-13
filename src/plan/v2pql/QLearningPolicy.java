/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

import data.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import monitor.PM;
import monitor.VM;
import plan.ProblemSet;
import plan.mraco.Ant;

/**
 *
 * @author Khiet
 */
public class QLearningPolicy {
    /*
    
    ArrayList<PM> safePM;
    ArrayList<PM> buSafePM;
    ArrayList<PM> pms;
    ArrayList<VM> migrateVM;
    ArrayList<VM> buMigrateVM;
    ArrayList<VM> vms;
    int numVM = 3;
    int numPM = 4;
    private double[][] Q;    // Q learning
    
    
    List<Double> saveLB = new ArrayList<>();

    public QLearningPolicy() {
    }
    
    
    
     public void initSystem() {
        safePM = new ArrayList<>();
        migrateVM = new ArrayList<>();

        readPM();
        numPM = safePM.size();

        readVM();
        numVM = migrateVM.size();

        System.out.println("numPM: " + numPM);
        System.out.println("numVM: " + numVM);
        Q = new double[State.STATE_COUNT][numPM];
    }
    
    public void run() {
        initSystem();
        //training phase
        runPolicy();
        //migrate VM to Pm based on optimal policy
        System.out.println("LoadBalancing........................................");
        for (double a : saveLB) {
            System.out.println(a);
        }

    }
    void runPolicy() {
        buSafePM = (ArrayList<PM>) safePM.clone();
        buMigrateVM = (ArrayList<VM>) migrateVM.clone();
        double loadBalancing;
        Random rand = new Random();
        State crtState;
        State nextState;
        PM pm;
        VM vm = null;
        double epsilon = 0.8;
        int x = 0, y = 0;

        //initialize state
        safePM = (ArrayList<PM>) buSafePM.clone();
        migrateVM = (ArrayList<VM>) buMigrateVM.clone();
        

        //Read Q - start
        try {
            BufferedReader bd = new BufferedReader(new FileReader("./data/dataql/epsilon_04.txt"));
            String line;
            while ((line = bd.readLine()) != null){
                String[] values = line.split(",");
                for (String str : values){
                    double strD = Double.parseDouble(str);
                    Q[x][y] = strD;
                    System.out.print(Q[x][y] + " ");
                    y=y+1;
                }
                x=x+1;
                y=0;
                System.out.println();
            }
            bd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Read Q - end

        for (VM item : migrateVM) {
            System.out.println("##########################################################");
            //1. init
            int indexVMType = item.type;
            loadBalancing = PM.CalculatePMsLoadBalancing(safePM);
            crtState = new State((int) (Math.floor(loadBalancing / 0.2)), indexVMType);
            System.out.println(" Before action: LB:"+loadBalancing+" State: "+crtState.getIndex());
            

            //3. take action from Q
            Action a = choosePMFromQ(crtState);
            assert a != null;
            safePM.get(a.getToPM()).addVM2PM(item);

            loadBalancing = PM.CalculatePMsLoadBalancing(safePM);
            System.out.println(" After action: LB:"+loadBalancing+" State: "+crtState.getIndex());
            
            saveLB.add(loadBalancing);

        }
    }
    private Action choosePMFromQ(State nextState) {

        System.out.println(" #Choose action from Q");
        double maxValue = -10;
        int index = -1;
        for (int j = 0; j < numPM; j++) {
            double value = Q[nextState.getIndex()][j];
            if (value > maxValue) {
                maxValue = value;
                index = j;
            }
        }
        PM pm = safePM.get(index);
        VM vm = migrateVM.get(nextState.getTypeVM());
        if (pm.addableVM(vm)) {
            System.out.println("  VM:" + vm.type + " ==>" + "PM" + index);
            return new Action(vm.type, index);
        }
        return null;
    }
    
    private void readPM() {
        if (safePM == null) {
            return;
        }

        try {
            String fileName = "./data/dataql/PM.csv";
            PM pm;
            Stream<String> lines = Files.lines(Paths.get(fileName));

            List<String> content = lines.collect(Collectors.toList());

            for (String s : content) {
                System.out.println(s);
                String[] ss = s.split(";");
                pm = new PM();
                pm.idPM = Integer.parseInt(ss[0]);
                pm.setCcpu(Double.parseDouble(ss[1]));
                pm.setCram(Double.parseDouble(ss[2]));
                pm.setCdisk(Double.parseDouble(ss[3]));
                pm.setECpu(Double.parseDouble(ss[4]));
                pm.setERam(Double.parseDouble(ss[5]));
                pm.setEDisk(Double.parseDouble(ss[6]));
                safePM.add(pm);
            }
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readVM() {
        if (migrateVM == null || safePM == null) {
            return;
        }

        try {
            String fileName = "./data/dataql/VM.csv";
            VM vm;
            Stream<String> lines = Files.lines(Paths.get(fileName));

            List<String> content = lines.collect(Collectors.toList());

            for (String s : content) {
                System.out.println(s);
                String[] ss = s.split(";");
                vm = new VM();
                vm.column = Integer.parseInt(ss[0]);
                vm.setCcpu(Double.parseDouble(ss[1]));
                vm.setCram(Double.parseDouble(ss[2]));
                vm.setCdisk(Double.parseDouble(ss[3]));
                vm.type = Integer.parseInt(ss[4]) - 1;
                migrateVM.add(vm);
            }
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   */


}
