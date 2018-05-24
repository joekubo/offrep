
package security;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;


public class Javaconnect {
    Connection conn = null;    
     public static Connection ConnecrDb(){
         try{ 
             Class.forName("com.mysql.jdbc.Driver");
             //Connection conn = DriverManager.getConnection("jdbc:mysql://joe-PC:3306/securitydb","root","JesusChrist1");  
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/securitydb","root","JesusChrist1");
             return conn;     
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+("Please Check on the Network Connection."
                     + " This Computer must be in the same Network with the Server..."));
             System.exit(0);
             return null;
         }   
     }
}
