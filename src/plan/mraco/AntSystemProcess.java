/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.mraco;


import config.ConfigSystem;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import monitor.PM;
import monitor.VM;
import plan.ProblemSet;




/**
 *
 * @author Mr. Khiet
 */
public class AntSystemProcess {
    
    
    private Random random = new Random();
    ArrayList<Ant> ants;
    private double[][] trails;
    private double probabilities[];
    private Ant bestAnt;
    private ArrayList<Ant> listBestAnt;
   
    ArrayList<PM> safePM;
    ArrayList<PM> pms;
    ArrayList<VM> migrateVM;
    ArrayList<VM> vms;
    
    private Vector<Double> fs;
    
    private int numVM =0;
    private int numPM = 0;
    
    
    //parameters 
    double c = 0.1;
    double epsilon = 0;
    double theta =0;
    int numberOfAnts = 0; 
    
    public AntSystemProcess(ArrayList<PM> pms,  ArrayList<VM> vms, 
            ArrayList<VM> migrateVM, ArrayList<PM> safePM,
            double c, double epsilon, double theta, int numberOfAnts
            ){
        
        this.numVM = migrateVM.size();
        this.numPM= safePM.size();
        
        trails = new double[numVM][numPM];
        probabilities = new double[numPM];
        
        this.safePM = safePM;
        this.pms =pms;
        this.migrateVM = migrateVM;
        this.vms = vms;
        fs = new  Vector<>();
        fs.add(0,0.0);
        listBestAnt = new ArrayList<>();
        
        this.c = c;
        this.epsilon = epsilon;
        this.theta =theta;
        this.numberOfAnts = numberOfAnts; 
    }
    public void runDemo(){
        
        //System.out.println("reset trails: "+c);
        Trail.clearTrails(numVM, numPM,trails);
        int iteration = 0;
        
        while(!stopCondition(iteration)){
            //System.out.println("#Iteration: "+iteration);
            //System.out.println("Set up number of ant: "+numberOfAnts);
            setupAnts();
            moveAnts();
            Trail.updateTrails(ants, trails,numVM,numPM);
            /*
            System.out.println("#Trails");
            for(int x = 0; x < numVM; x++){
                for(int y = 0; y < numPM; y++){
                    System.out.print("\t"+MyRound.getRound(trails[x][y]));
                }
                System.out.println();
            }*/
            updateBest();
            fs.add(iteration+1, ProblemSet.evaluate(bestAnt));   
            iteration++;
        }
        double[] r = ProblemSet.getMetrics(bestAnt);
        //result simulation 1
        System.out.println(iteration+"\t"+r[0]+"\t"+r[1]+"\t"+r[2]);
        
        //result simulation 2
        //System.out.println(theta+"\t"+iteration+"\t"+r[0]+"\t"+r[1]+"\t"+r[2]);
        
        //result simulation 3
        //System.out.println(numberOfAnts+"\t"+iteration+"\t"+r[0]+"\t"+r[1]+"\t"+r[2]);
        
        //result simulation 4
        //System.out.println(ConfigSystem.nguy+"\t"+iteration+"\t"+r[0]+"\t"+r[1]+"\t"+r[2]);
        
        //result simulation 5
        //System.out.println(r[0]+","+r[1]+","+r[2]);
        
        
       
    }
    private void setupAnts() {
       ants = new ArrayList<>();
       for(int i = 0; i < numberOfAnts; i++){
           int[] solution = new int[numVM];
           for(int j = 0; j < numVM; j++){
               solution[j] = 0;
           }
           Ant ant = new Ant(migrateVM ,safePM ,solution);
           ants.add(ant);
       }
    }
   
    private void moveAnts() {
        for(int i = 0; i < numberOfAnts; i++){
            int[] newSolutions = new int[numVM];
            //ArrayList<PM> pmsafes = this.ants.get(i).safePM;
            Ant ant = this.ants.get(i);
            //System.out.println("##Ant: "+i);
            for(int indexAnt = 0; indexAnt < numVM; indexAnt++){
                //1. calcualte p_k_ij
                probabilities = Trail.calculateProbabilities(indexAnt,numPM, ant,  
                        trails);
                //2. choose PM for VM
                int indexPM= Trail.selectNextHost(numPM,
                        trails,probabilities,migrateVM.get(indexAnt),ant);
                if(indexPM != -1){
                    //3. update PM info after migrate VM to it
                    ant.safePM.get(indexPM).addVM2PM(migrateVM.get(indexAnt));
                    newSolutions[indexAnt] = indexPM;
                    //System.out.println("VM#"+ indexAnt+"==>PM#"+newSolutions[indexAnt]);
                    /*
                    System.out.println("i\tid\tcpu\tram\tdisk\th");
                    for(int j =0; j< numPM; j++){
                        ArrayList<PM> pmsafes = this.ants.get(i).safePM;
                        System.out.println(j+"\t"+pmsafes.get(j).idPM+
                                "\t"+MyRound.getRound(pmsafes.get(j).getCpu())+
                                "\t"+MyRound.getRound(pmsafes.get(j).getRam())+
                                "\t"+MyRound.getRound(pmsafes.get(j).getDisk())+
                                "\t"+MyRound.getRound(pmsafes.get(j).getUtilizationLoad())
                        );
                    }*/
                    
                }else{
                    //System.out.println("VM#"+indexAnt+"==>X");
                }
                
            }
           
            ants.get(i).setSolution(newSolutions);
            double[] r = ProblemSet.getMetrics(ants.get(i));
            //System.out.println("##LoadBalancing:"+r[0]+"##Waste: "+r[1]+"##F:"+ProblemSet.evaluate(ants.get(i)));
        }
    }
   
    private void updateBest() {   
        Ant bestLocalAnt =  ants.get(0);
        
        //local best
        for(int i =0; i < numberOfAnts; i++){
            if(ProblemSet.evaluate(ants.get(i)) <= ProblemSet.evaluate(bestLocalAnt)){
                bestLocalAnt = new Ant(ants.get(i));  
            }
        }
       // add local best
       listBestAnt.add(bestLocalAnt);
       //global best
       bestAnt = listBestAnt.get(0);
       int size = listBestAnt.size();
       for(int i = 0; i <size; i++){
           if(ProblemSet.evaluate(listBestAnt.get(i)) <= ProblemSet.evaluate(bestAnt)){
                bestAnt = new Ant(listBestAnt.get(i));  
            }
       }
        
    }
    private boolean stopCondition(int t){
        boolean condition = true;
        double err = 9999;
        if(t >2){
            err = 0;
            for(int x = 2; x < fs.size(); x++){
                err += (Math.sqrt(Math.pow(fs.get(x)- fs.get(x-1), 2)/t+1)); // minimizing the functions means it's getting closer to 0
            }
            err =1/err;
        }

        if(err > epsilon){
            condition = false;
        }
        return condition;
    }
    
    
}
