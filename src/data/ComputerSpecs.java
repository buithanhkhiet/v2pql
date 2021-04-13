/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author NguyenLinh
 */
public class ComputerSpecs {
    private int ccpu;
    private int cram;
    private int cdisk;
    private int temp;

    public ComputerSpecs(int ccpu, int cram, int cdisk) {
        this.ccpu = ccpu;
        this.cram = cram;
        this.cdisk = cdisk;
    }

    /**
     * @return the ccpu
     */
    public int getCcpu() {
        return ccpu;
    }

    /**
     * @param ccpu the ccpu to set
     */
    public void setCcpu(int ccpu) {
        this.ccpu = ccpu;
    }

    /**
     * @return the cram
     */
    public int getCram() {
        return cram;
    }

    /**
     * @param cram the cram to set
     */
    public void setCram(int cram) {
        this.cram = cram;
    }

    /**
     * @return the cdisk
     */
    public int getCdisk() {
        return cdisk;
    }

    /**
     * @param cdisk the cdisk to set
     */
    public void setCdisk(int cdisk) {
        this.cdisk = cdisk;
    }

    /**
     * @return the temp
     */
    public int getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(int temp) {
        this.temp = temp;
    }
    
    
}
