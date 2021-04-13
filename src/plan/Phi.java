/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plan;

import config.ConfigSystem;

/**
 *
 * @author Administrator
 */
public class Phi {
    public int[][] value;
    public Phi(){
        this.value = new int[ConfigSystem.numTier][ConfigSystem.numPM];
        for(int i = 0; i < ConfigSystem.numTier; i++){
            for(int j = 0; j < ConfigSystem.numPM; j++){
                this.value[i][j] = 0;
            }
        }
    }
    public Phi(int[][] value){
        this.value = value;
    }
}
