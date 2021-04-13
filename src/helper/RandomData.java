/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Mr. Khiet
 */
public class RandomData {
    public static double randDouble(double min, double max){
        double random = ThreadLocalRandom.current().nextDouble(min, max);
        return random;
    }
    public static int randInt(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static double Rand(){
        Random r = new Random();
        return r.nextInt(1000) / 1000.0;
    }
    public static int randomInt(int min , int max) {
        Random r = new Random();
        double d = min + r.nextDouble() * (max - min);
        return (int)d;
    }
}
