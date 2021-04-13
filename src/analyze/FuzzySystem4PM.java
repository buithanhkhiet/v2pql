/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyze;



/**
 *
 * @author Administrator
 */
public class FuzzySystem4PM {
 
 public static int numSateLoad = 3;
 public static int numSateTemperature = 3; 
 public static int numAction = 2;
 public static int numRule = 9;
 
 public static String[] Data = {"Load","Temperature"};
 public static String[] sLoadSate = {"Low","Medium","High"};
 public static String[] sTemperatureSate = {"Low","Medium","High"};
 
 
 
 public static int[] LoadSate = {0,1,2};
 public static int[] TemperatureSate = {0,1,2};
 public static int[] Action = {0,1};
 
 
 
  
 public static double[] membershipLoad (double inputValue){
    double[] fuzzyValues = new double[numSateLoad ];    
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
    return fuzzyValues;
 }
  public static double[] membershipTemperature(double inputValue){
    double[] fuzzyValues = new double[numSateTemperature];    
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
    
    return fuzzyValues;
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
 public static int getStateSystem(double s1, double s2){
     
     int index = 0;
     for(int i = 0; i < FuzzySystem4PM.numSateLoad; i++){
        for(int j = 0; j <FuzzySystem4PM.numSateTemperature; j++ ){		
			if(i ==s1 && j==s2){
				return index;
			}
			index++;
            
            
        }
     }
     return index;
 }
 public static int[] getSateSystemDetail(int state){
     int[] s = {-1,-1,-1};
     int index = 0;
     for(int i = 0; i < numSateLoad; i++){
        for(int j = 0; j< numSateTemperature; j++){
            
                if(index == state){
                    s[0] = i;
                    s[1] = j;
            
                    return  s;       
                }
                index++;
            }
            
        
     }
     return s;
 }
 public static double anpha(int i, double[] memLoad, double[] memTemperature) {
       int[] stateDetail = FuzzySystem4PM.getSateSystemDetail(i);
       double load = memLoad[stateDetail[0]];
       double temp = memTemperature[stateDetail[1]];
       if( temp<=load)
           return temp;
       return load;
    }
 
}
