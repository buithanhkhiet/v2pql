/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan;



import analyze.Resources;
import config.ConfigSystem;
import java.util.ArrayList;

import monitor.PM;
import monitor.VM;
import plan.mraco.Ant;




public class ProblemSet{
	
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
       
        
        public static double evaluate(ArrayList<VM> vms,ArrayList<PM> pms){ 
          
           double[] r = Resources.getObjective(vms, pms);
           return ConfigSystem.nguy *r[0]
                   +(1-ConfigSystem.nguy)*r[1];
           
        }
        public static double evaluate(double a, double b){
            return ConfigSystem.nguy*a+(1-ConfigSystem.nguy)*b;
        }
        public static double evaluate(Ant ant){
           
           double[] r = Resources.getObjective(ant.vms, ant.safePM);
           return ConfigSystem.nguy *r[0]
                   +(1-ConfigSystem.nguy)*r[1];
          
        }
        public static double[] getMetrics(Ant ant){
             double[] r = new double[3];
             double[] obj = Resources.getObjective(ant.vms, ant.safePM);
             r[0] = obj[0];
             r[1] = obj[1];
             r[2] = ConfigSystem.nguy *r[0]
                   +(1-ConfigSystem.nguy)*r[1];
           return r;
        }
}