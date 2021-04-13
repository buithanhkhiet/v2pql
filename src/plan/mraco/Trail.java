/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.mraco;


import config.ConfigSystem;
import helper.MyRandom;
import helper.RandomData;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import monitor.PM;
import monitor.VM;
import plan.Constraints;
import plan.ProblemSet;

import static plan.mraco.ACOConstants.Q;
import static plan.mraco.ACOConstants.alpha;
import static plan.mraco.ACOConstants.beta;
import static plan.mraco.ACOConstants.c;
import static plan.mraco.ACOConstants.evaporation;


/**
 *
 * @author Mr. Khiet
 */
public class Trail implements ACOConstants {
    
    public static int selectNextHost(int numPM,double[][] trails,
        double[] probabilities,  VM vm,  Ant ant){
        int index = -1;
        double maxProb = 0;
        
        double r =  MyRandom.randomDoubleMinMax(0, 1);
        if(r<=theta){
            int count = 0;
            for(int i = 0; i < numPM; i++){
                index  = MyRandom.randomIntMinMax(0,numPM-1);
                PM pm = ant.safePM.get(index);
                if(count < numPM && Constraints.satisfy(vm,pm)){
                    return index;
                }
            }
        }
        else{
            
            for (int i = 0; i < numPM; i++) {
                PM pm = ant.safePM.get(i);
                if (maxProb <= probabilities[i] && Constraints.satisfy(vm,pm)) {
                    maxProb = probabilities[i] ;
                    index = i;
                   
                }
            }
            
        }
        return index;
    }
    
    public static void clearTrails(int numVM, int numPM, double[][] trails) {
       for(int i = 0; i < numVM; i++){
           for(int j = 0; j < numPM; j++){
               trails[i][j] = c;
           }
       }
    }
     
    public static void updateTrails(ArrayList<Ant> ants, double[][] trails, int numVM, int numPM) {
        int numberOfAnts = ants.size();
        for (int index =0; index < numberOfAnts; index++) {
            Ant a  = ants.get(index);
            double contribution = Q /ProblemSet.evaluate(a);
            for (int i = 0; i < numVM; i++) {
                trails[i][a.getSolution()[i]] =trails[i][a.getSolution()[i]]*evaporation+contribution;
                
            }
        }
    }

    public static double[] calculateProbabilities(int indexAnt,int numPM, Ant ant, double[][] trails) {
        double[] probabilities = new double[numPM];
        double pheromone = pheromone(indexAnt, numPM,trails,ant);
        for(int i = 0; i < numPM; i++){
            double numerator = Math.pow(trails[indexAnt][i], alpha) * Math.pow(1.0 / performance(i,ant),beta);
            probabilities[i] = numerator / pheromone;
        }
        return probabilities;
    }

    public static double pheromone(int indexAnt, int numPM,double[][] trails, Ant ant) {
        double pheromone = 0.0;
        for (int i = 0; i < numPM; i++) {
            pheromone += Math.pow(trails[indexAnt][i], alpha) * Math.pow(1.0 / performance(i,ant), beta);   
        }
        return pheromone;
    }
    public static double performance(int i,Ant ant) {
        return 1/ant.safePM.get(i).getUtilizationLoad();
    }

   
}
