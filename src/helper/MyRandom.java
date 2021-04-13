/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author btkhiet
 */
public class MyRandom {
    public static int randomIntMinMax(int min, int max)
    {
        int result = 0;
        for(int l = 0; l<10;l ++)
            result = ThreadLocalRandom.current().nextInt(((max - min) + 1)) + min;
        return result;
        
    }
    public static double randomDoubleMinMax(double min, double max)
    {
        double random=0;
        for(int l = 0; l<10;l ++)
            random  = ThreadLocalRandom.current().nextDouble(min, max);
        return random;
    }
}
