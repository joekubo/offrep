
package security;

import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class Manage {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    public static String loggedid_number;
    public static String loggedusername;
    String path = null;
    String filename;
    
    public Manage(){
        conn = Javaconnect.ConnecrDb();
        
    }
    public void update(String sql){
        try{
            pst = conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" update");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void delete(String sql){
        try{
            pst = conn.prepareStatement(sql);
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" delete");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void update_table(String sql, JTable table){
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
            TableColumn idColumn = table.getColumn("id");
            idColumn.setMaxWidth(0);
            idColumn.setMinWidth(0);
            idColumn.setPreferredWidth(0);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" update_table");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    public void fillcombo(String sql, JComboBox combobox,String value){
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                while(rs.next()){
                    combobox.addItem(rs.getString(""+value+""));
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" fillcombo");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedinmessage(){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = ""+loggedid_number+"-"+loggedusername+" is logged in at "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logtable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
            String query = "UPDATE userstable SET logged = '1' WHERE id_number = '"+loggedid_number+"'";
            this.update(query);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedinmessage");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedoutmessage(){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = ""+loggedid_number+"-"+loggedusername+" has logged out at "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logtable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
            String query = "UPDATE userstable SET logged = '0' WHERE id_number = '"+loggedid_number+"'";
            this.update(query);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedinmessage");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedmessageupdate(String name){
        try{
             DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = ""+loggedid_number+"-"+loggedusername+" Updated "+name+" ON "+dateFormat.format(date.getTime())+" AT "
                                + ""+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logtable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedmessageupdate");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void loggedmessagedelete(String name){
        try{
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String operation = ""+loggedid_number+"-"+loggedusername+" Deleted "+name+" details ON "+dateFormat.format(date.getTime())+" "
                                                                                                                + "AT "+timeFormat.format(date.getTime())+"";
            String sql = "INSERT INTO logtable(operation,s,date)VALUES(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, operation);
            pst.setString(2, "1");
            pst.setString(3, dateFormat.format(date.getTime()));
            
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" loggedmessagedelete");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    public void report(String sql,String path,JPanel panel){//panel report
        try {
            JasperDesign jd = JRXmlLoader.load(path);//Reports/invoice.jrxml
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jd.setQuery(newQuery);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.repaint();
            panel.add(new JRViewer(jp));
            panel.revalidate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception e) {

            }
        }
    }                

    //______________________________________________________________________________________________backing up database______________________________________------securitydb-----
    public void setbackuppath(String companyname){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            try{
                path = companyname.replaceAll("\\s+","_")+"_"+ date +".sql";
            }catch(Exception e){ 
                e.printStackTrace();
            }
    }
    public void backupnow(String username,String password,String database){
        Process p = null;
                    try{
                        Runtime runtime = Runtime.getRuntime();
                        p = runtime.exec("C:/xampp/mysql/bin/mysqldump.exe -u"+username+" -p"+password+" --add-drop-database -B "+database+" -r" +path);
                        int processComplete = p.waitFor();
                            if(processComplete == 0){
                                //JOptionPane.showMessageDialog(null, "Backup created successfully...");   // at location "+path+"");
                                System.out.println("Backup created successfully...");
                            }else{
                                //JOptionPane.showMessageDialog(null, "Backup Unsuccesfully...");
                                System.out.println("Backup Unsuccessfully...");
                            }
                    }catch(Exception e){

                    }
    }
  //____________________________________________________________________________________________end backing up database_________________________________________________________  
    
    public void sendemail(String sender,String to,String pass,String companyname){
        final String username = sender;
        final String password = pass;

         Properties props = new Properties();
         props.put("mail.smtp.auth", true);
         props.put("mail.smtp.starttls.enable", true);
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");

         Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                     protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                         return new javax.mail.PasswordAuthentication(username, password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("josephmwawasi29@gmail.com"));
             message.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(to));
             message.setSubject(companyname+" Db Backup");
             message.setText("Security system at "+companyname+" database attached...");

             MimeBodyPart messageBodyPart = new MimeBodyPart();

             Multipart multipart = new MimeMultipart();

             messageBodyPart = new MimeBodyPart();
             String file = path;
             //String fileName = companyname+" Database Backup";
             DataSource source = new FileDataSource(file);
             messageBodyPart.setDataHandler(new DataHandler(source));
             messageBodyPart.setFileName(path);
             multipart.addBodyPart(messageBodyPart);

             message.setContent(multipart);

             System.out.println("Sending");

             Transport.send(message);

             System.out.println("Done");

         } catch (MessagingException e) {
             
         }
    }
}

