/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyze;

import helper.MyRound;

/**
 *
 * @author Administrator
 */
public class Analysing {
    public static int getAction4PM(double load, double temp){
        
        //Fuzzy
        double[] memLoad = FuzzySystem4PM.membershipLoad(load);
        double[] memTemperature= FuzzySystem4PM.membershipTemperature(load);
        

        double stateLoad =  FuzzySystem4PM.getStateByMax(memLoad)[0];
        double stateTemperature = FuzzySystem4PM.getStateByMax(memTemperature)[0];
        

        int systemState = FuzzySystem4PM.getStateSystem(stateLoad, stateTemperature);
        //System.out.print(" State:"+systemState
        //                        +" Load: "+FuzzySystem4PM.sLoadSate[(int)stateLoad]
        //                        +" Temperature: "+FuzzySystem4PM.sTemperatureSate[(int)stateTemperature]);


        //Defuzzy
        double tu = 0;
        double mau = 0;
        /*
        RULE
        LOAD    TEMP    FAULT
        LOW     LOW     0
        LOW     MED     0
        LOW     HIG     1
        MED     LOW     0
        MED     MED     0
        MED     HIG     1
        HIG     LOW     0
        HIG     MED     1
        HIG     HIG     1
        */
        int a[] = {0,0,1,0,0,1,0,1,1};
        for(int i = 0; i < FuzzySystem4PM.numRule; i++){
            double anp = FuzzySystem4PM.anpha(i,memLoad,memTemperature);
            tu += anp*a[i];
            mau += anp;
        }
        int action = (int) (Math.round(tu/mau));
        return action;
    }
    public static APPs getAction4App(double workload, double responsetime){
        
        double[] memWorkLoad = FuzzySystem4App.membershipWorkLoad(workload);
        double[] memResponseTime= FuzzySystem4App.membershipResponseTime(responsetime);
        

        double stateWorkLoad =  FuzzySystem4App.getStateByMax(memWorkLoad)[0];
        double stateResponseTime = FuzzySystem4App.getStateByMax(memResponseTime)[0];
        int systemState = FuzzySystem4App.getStateSystem(stateWorkLoad, stateResponseTime);
        String sworkload = FuzzySystem4App.sWorkLoadSate[(int)stateWorkLoad];
        String sresponsetime = FuzzySystem4App.sResponseTimeSate[(int)stateResponseTime];


        //Defuzzy
        double tu = 0;
        double mau = 0;
        
        /*RULE
        WL      RT      ACTION
        LOW     GOOD    -1
        LOW     NORM    0
        LOW     BAB     +1
        MED     GOOD    -1
        MED     NORM    0
        MED     BAB     +1
        HIG     GOOD    -1
        HIG     NORM    0
        HIG     BAB     +1
        */
        int a[] = {-1,0,1,-1,0,1,-1,0,1};
        for(int i = 0; i < FuzzySystem4App.numRule; i++){
            double anp = FuzzySystem4App.anpha(i,memWorkLoad,memResponseTime);
            tu += anp*a[i];
            //System.out.println("i:"+i+" anpha:"+anp+" a:"+a[i]+" tu:"+tu);
            mau += anp;
        }
        double ac = (int) Math.round(tu/mau);
        int action = 0;
        if(ac > 0)
            action =1;
        else if(ac <0)
            action = -1;
        else
            action = 0;
        
        
        return new APPs(action, systemState, workload, responsetime, sworkload, sresponsetime) ;
    }
    public static String getStateApp(double workload, double responsetime){
        double[] memWorkLoad = FuzzySystem4App.membershipWorkLoad(workload);
        double[] memResponseTime= FuzzySystem4App.membershipResponseTime(responsetime);
        double stateWorkLoad =  FuzzySystem4App.getStateByMax(memWorkLoad)[0];
        double stateResponseTime = FuzzySystem4App.getStateByMax(memResponseTime)[0];
        int systemState = FuzzySystem4App.getStateSystem(stateWorkLoad, stateResponseTime);
        String s = "#"+systemState+
                "#"+FuzzySystem4App.sWorkLoadSate[(int)stateWorkLoad]+
                "#"+FuzzySystem4App.sResponseTimeSate[(int)stateResponseTime];
        return s;
    }
}
