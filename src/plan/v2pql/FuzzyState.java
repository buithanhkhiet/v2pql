/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan.v2pql;

/**
 *
 * @author Khiet
 */
public class FuzzyState {
    
    public static double[] membershipUtilization (double inputValue){
        int numUtilization = 3;
        double[] fuzzyValues = new double[numUtilization ];    
        double[] theta = {0.2,0.4,0.6,0.8};
    
        double l = 0,m = 0,h = 0;
    
        //Low
        if (inputValue <= theta[0]) 
        {
            l = 1;            
        }
        else if (inputValue >= theta[0] && inputValue <= theta[1]) 
        {   
            l = (theta[1]-inputValue)/(theta[1]-theta[0]);
        }
        else if(inputValue > theta[1])
        {
            l = 0;
        }
    
        //Medium
        if (inputValue < theta[0] || inputValue >theta[3]) 
        {
            m = 0;            
        }
        else if (inputValue >= theta[0] && inputValue <=theta[1]) 
        {   
            m = (inputValue-theta[0])/(theta[1]-theta[0]);
        }
        if (inputValue > theta[1] && inputValue <theta[2]) 
        {
            m = 1;            
        }
        else if(inputValue >= theta[2]  && inputValue <= theta[3])
        {
            m = (theta[3] - inputValue)/(theta[3] - theta[2]);
        }

        //High
        if (inputValue > theta[3]) 
        {
            h = 1;            
        }
        else if (inputValue >= theta[2] && inputValue <= theta[3]) 
        {   
            h = (inputValue - theta[2])/(theta[3]-theta[2]);
        } 
        else if(inputValue < theta[2])
        {
            h = 0;
        }
    
        fuzzyValues[0]=l;
        fuzzyValues[1]=m;
        fuzzyValues[2]=h;
        return getStateByMax(fuzzyValues);
    }
 
    public static double[] membershipLoadBalance(double inputValue){
      
        int numLoadBalance = 3;
        double[] fuzzyValues = new double[numLoadBalance ];

        double[] phi = {0.25,0.5,0.75};
        double g = 0;
        double n = 0;
        double b = 0;


        //Good
        if (inputValue <= phi[0]) 
        {
            g = 1;            
        }
        else if (inputValue >= phi[0] && inputValue <= phi[1]) 
        {   
            g = (phi[1]-inputValue)/(phi[1]-phi[0]);
        }
        else if(inputValue > phi[1])
        {
            g = 0;
        }

        //Normal
        if (inputValue < phi[0] || inputValue >phi[2]) 
        {
            n = 0;            
        }
        else if (inputValue >= phi[0] && inputValue <=phi[1]) 
        {   
            n = (inputValue-phi[0])/(phi[1]-phi[0]);
        } 
        else if(inputValue >= phi[1] && inputValue <= phi[2])
        {
            n = (phi[2] - inputValue)/(phi[2] - phi[1]);
        }

        //Bad

        if (inputValue > phi[2]) 
        {
            b = 1;            
        }
        else if (inputValue >= phi[1] && inputValue <= phi[2]) 
        {   
            b = (inputValue-phi[1])/(phi[2]-phi[1]);
        } 
        else if(inputValue < phi[1])
        {
            b = 0;
        }

        fuzzyValues[0]=g;
        fuzzyValues[1]=n;
        fuzzyValues[2]=b;
        

        return getStateByMax(fuzzyValues);
    }
    public static double[] getStateByMax(double[] memberships){
        double[] result = {-1,-1};//{state,value}
        double max = memberships[0];
        int size = memberships.length;
        for (int i = 0; i < size; i++) {
            if(max <= memberships[i]){
                max = memberships[i];
                result[0] = i;
                result[1] = max;

            }
        }
        return result;
    }
}
