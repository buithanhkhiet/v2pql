/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.ArrayList;


/**
 *
 * @author Administrator
 */
public class Normalization {
    public double a = 0.01;
    public double b = 0.99;
    public double max = 0;
    public double min=0;
    public int size = 0;
    ArrayList<Double> data;
    /*
    public Normalization(double[] data){
        max = data[0];
        min = data[0];
        int size = data.length;
        for(int i = 0; i < size; i++){
            if(max<=data[i]){
                max=data[i];
            }
            if(data[i]<=min){
                min=data[i];
            }
        }         
    }*/
    public Normalization(ArrayList<Double> data){
       //this.data = data;
        this.size = data.size();
        max = data.get(0);
        min = data.get(0);
        this.size = data.size();
        this.data = data;
        for(int i = 0; i < size; i++){
            if(max<=data.get(i)){
                max=data.get(i);
            }
            if(data.get(i)<=min){
                min=data.get(i);
            }
        }    
    }
    public ArrayList<Double> getNormalData(){
        ArrayList result = new ArrayList();
        for(int i = 0; i < size; i++){
            result.add(i, getNormaValue(data.get(i)));
        }
        return result;
    }
    public double getNormaValue(double value){
        return a + (value - min)*(b-a)/(max - min);
    }
    
}
