/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package downloadmanager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rayat
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            URL url = new URL("http://mitpress.mit.edu/books/chapters/0262033844chap27.pdf");
           Download download = new Download(url,"C:"+File.separator);
           
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
