/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author btkhiet
 */
public class IOFile {
    
    OutputStream fOut = null;
    File file;
    public IOFile()
    {
        try {
           
            File f = new File("");
            file = new File(f.getAbsoluteFile()+"/data/output.csv");
            fOut = new FileOutputStream(file);
            OutLog.setOutput(fOut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public IOFile(String path)
    {
        try {
           
            file = new File(path);
            fOut = new FileOutputStream(file);
            OutLog.setOutput(fOut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void printLine(String message)
    {
        OutLog.printLine(message);
    }
    public void print(String message)
    {
        OutLog.print(message);
        
    }
    public void close()
    {
       
    }
}
