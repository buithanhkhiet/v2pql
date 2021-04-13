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
public class Player {
    private int id;
    private int idPM;
    private double loadbalance;
    private double utilization;
    private double utility;

    public Player(int id, int idPM) {
        this.id = id;
        this.idPM = idPM;
    }
    

    public Player(int id, int idPM, double loadbalance, double utilization, double utility) {
        this.id = id;
        this.idPM = idPM;
        this.loadbalance = loadbalance;
        this.utilization = utilization;
        this.utility = utility;
    }

    public int getId() {
        return id;
    }

    public int getIdPM() {
        return idPM;
    }

    public double getLoadbalance() {
        return loadbalance;
    }

    public double getUtilization() {
        return utilization;
    }

    public double getUtility() {
        return utility;
    }

    public void setLoadbalance(double loadbalance) {
        this.loadbalance = loadbalance;
    }

    public void setUtilization(double utilization) {
        this.utilization = utilization;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }
    
    
            
}
