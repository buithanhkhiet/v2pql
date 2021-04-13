/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author Administrator
 */
public class MyRound {
    public static double getRound(double a){
        return (double) Math.round(a*100)/100;
    }
    public static double getRound(double a, int b){
        return (double) Math.round(a*b)/b;
    }
}
