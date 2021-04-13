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
public class PMs {
   
    public double[] ccpu;
    public double[] cram;
    public double[] cdisk;
    public double[] ucpu;
    public double[] uram;
    public double[] udisk;
    public double[] acpu;
    public double[] aram;
    public double[] adisk;
    public double[] wload;
    public double[] uload;
    public int[] numVM; 

    public PMs(double[] ccpu, double[] cram, double[] cdisk, double[] ucpu, double[] uram, double[] udisk, double[] acpu, double[] aram, double[] adisk, double[] uload, double[] wload, int[] numVM) {
        this.ccpu = ccpu;
        this.cram = cram;
        this.cdisk = cdisk;
        this.ucpu = ucpu;
        this.uram = uram;
        this.udisk = udisk;
        this.acpu = acpu;
        this.aram = aram;
        this.adisk = adisk;
        this.wload = wload;
        this.uload = uload;
        this.numVM = numVM;
    }
    public int getMaxAvailablePM(){
        double max = 0;
        int index = -1;
        for(int i =0 ; i < wload.length; i++){
            if(max <= wload[i]){
                max = wload[i];
                index = i;
            }
        }
        return index;
    }
    
}
