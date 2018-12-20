
package offrep;
import com.tolclin.manage.Manage;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CompanyPanel extends javax.swing.JPanel {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Manage manage  = new Manage();
        public String loggedincompanyid;
        public int companyid;
        public CompanyPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
        update_table();
        addimage.setToolTipText("Image Size should be ");
    }

    private void update_table(){
        String sql = "SELECT id,company_name AS 'Company Name',email AS 'Email',pin AS 'PIN',website AS 'Website' FROM companytable WHERE s = '1'";
        manage.update_table(sql, tablecompany);
    }
    public void reset(){
        txtCompanyName.setText("");
        txtLocation.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtPhoneNo.setText("");
        txtFax.setText("");
        txtEmailAddress.setText("");
        txtWebsite.setText("");
        txtPin.setText("");
        txtEmailPassword.setText("");
        txtDealerIn.setText("");
        txtAccount.setText("");
        txtCompanyName.requestFocus();
        buttonSave.setEnabled(true);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
        buttonAdd.setEnabled(true);
        buttonexit.setEnabled(true);
        update_table();
        companyid = 0;
        lbl_exception.setText("");
    }
    private void selectedrow(){
        buttonSave.setEnabled(false);
        buttonUpdate.setEnabled(true);
        buttonDelete.setEnabled(true);
            try{
                int row = tablecompany.getSelectedRow();
                String table_click = tablecompany.getValueAt(row, 0).toString();
                String sql = "SELECT * FROM companytable WHERE s = '1' AND id = '"+table_click+"'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();    
                    if(rs.next()){
                        companyid = rs.getInt("id");
                        txtCompanyName.setText(rs.getString("company_name"));
                        txtLocation.setText(rs.getString("location"));
                        txtAddress.setText(rs.getString("address"));
                        txtCity.setText(rs.getString("city"));
                        txtPhoneNo.setText(rs.getString("phone_no"));
                        txtFax.setText(rs.getString("fax"));
                        txtEmailAddress.setText(rs.getString("email"));
                        txtWebsite.setText(rs.getString("website"));
                        txtPin.setText(rs.getString("pin"));
                        txtEmailPassword.setText(rs.getString("email_password"));
                        txtAccount.setText(rs.getString("account_name"));
                        txtDealerIn.setText(rs.getString("Dealer_in"));
                        lbl_exception.setText("");
                    }
            }catch(Exception e){
                lbl_exception.setText(e+" selectedrow");
            }finally{
                try{
                    rs.close();
                    pst.close();
                }catch(Exception e){
                    
                }
            }
    }
    
    private void save(){
        try{
            String companyname = txtCompanyName.getText();
            String location = txtLocation.getText();
            String address = txtAddress.getText();
            String city = txtCity.getText();
            String phoneno = txtPhoneNo.getText();
            String fax = txtFax.getText();
            String email = txtEmailAddress.getText();
            String website = txtWebsite.getText();
            String pin = txtPin.getText();
            String email_pass = txtEmailPassword.getText();
            String dealerin = txtDealerIn.getText();
            String imagelocation = path.getText();
            
            if(companyname.equals("")){
                lbl_exception.setText("Please Enter Company Name...");
                txtCompanyName.requestFocus();
            }else if(location.equals("")){
                lbl_exception.setText("Please Enter Location...");
                txtLocation.requestFocus();
            }else if(address.equals("")){
                lbl_exception.setText("Please Enter Address...");
                txtAddress.requestFocus();
            }else if(city.equals("")){
                lbl_exception.setText("Please Enter City...");
                txtCity.requestFocus();
            }else if(phoneno.equals("")){
                lbl_exception.setText("Please Enter Phone #...");
                txtPhoneNo.requestFocus();
            }else if(fax.equals("")){
                lbl_exception.setText("Please Enter Fax...");
                txtFax.requestFocus();
            }else if(email.equals("")){
                lbl_exception.setText("Please Enter Email Address...");
                txtEmailAddress.requestFocus();
            }else if(website.equals("")){
                lbl_exception.setText("Please Enter Website...");
                txtWebsite.requestFocus();
            }else if(pin.equals("")){
                lbl_exception.setText("Please Enter PIN...");
                txtPin.requestFocus();
            }else if(email_pass.equals("")){
                lbl_exception.setText("Please Enter Email Password...");
                txtEmailPassword.requestFocus();
            }else if(txtAccount.getText().equals("")){
                lbl_exception.setText("Please Enter App A/C Name...");
                txtAccount.requestFocus();
            }else if(dealerin.equals("")){
                lbl_exception.setText("Please Enter Dealer In ...");
                txtDealerIn.requestFocus();
            }else if(imagelocation.equals("")){
                lbl_exception.setText("Please Choose Image Location...");
                addimage.requestFocus();
            }else{
                String sql = "INSERT INTO companytable(company_name,location,address,city,phone_no,fax,email,email_password,pin,Dealer_in,website,s,account_name,image)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                
                pst.setString(1, companyname);
                pst.setString(2, location);
                pst.setString(3, address);
                pst.setString(4, city);
                pst.setString(5, phoneno);
                pst.setString(6, fax);
                pst.setString(7, email);
                pst.setString(8, email_pass);
                pst.setString(9, pin);
                pst.setString(10, dealerin);
                pst.setString(11, website);
                pst.setString(12, "1");
                pst.setString(13, txtAccount.getText());
                pst.setString(14, imagelocation);
                
                pst.execute();
                reset();
                lbl_exception.setText("Company "+txtCompanyName.getText()+" Saved Successfully...");
               
            }
        }catch(Exception e){
            lbl_exception.setText(e+" companypanel.save");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    String attachment_path;
    private void filechooser(){
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        //String filename1 = f.getAbsolutePath();
        attachment_path = f.getAbsolutePath();
        path.setText(attachment_path);
        manage.showdialog(DialogCompany, jPanel2, 825, 370);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogCompany = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonUpdate = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonReset = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtFax = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEmailAddress = new javax.swing.JTextField();
        txtPin = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtWebsite = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtEmailPassword = new javax.swing.JPasswordField();
        checkViewCharacters = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        txtAccount = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtAddress = new javax.swing.JTextField();
        txtCompanyName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        txtPhoneNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDealerIn = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        addimage = new javax.swing.JButton();
        path = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablecompany = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        buttonexit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        lbl_exception = new javax.swing.JLabel();

        DialogCompany.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogCompany.setUndecorated(true);

        jPanel7.setBackground(new java.awt.Color(214, 193, 240));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        buttonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonUpdate.setText("Update");
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateActionPerformed(evt);
            }
        });

        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUpdate)
                    .addComponent(buttonExit)
                    .addComponent(buttonSave)
                    .addComponent(buttonReset)
                    .addComponent(buttonDelete))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        txtFax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFaxKeyPressed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("Email Address:");

        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Pin:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Fax:");

        txtEmailAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailAddressKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailAddressKeyReleased(evt);
            }
        });

        txtPin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPinKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPinKeyReleased(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("Website:");

        txtWebsite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWebsiteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWebsiteKeyReleased(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("Email Password:");

        txtEmailPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailPasswordKeyPressed(evt);
            }
        });

        checkViewCharacters.setText("Show Password");
        checkViewCharacters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkViewCharactersActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("App A/C Name:");

        txtAccount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFax, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtWebsite, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(checkViewCharacters)
                            .addComponent(txtEmailPassword))))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtFax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtEmailPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkViewCharacters)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
        });

        txtCompanyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCompanyNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCompanyNameKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Company Name:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Location:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Address:");

        txtLocation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLocationKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLocationKeyReleased(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("City:");

        txtCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCityKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCityKeyReleased(evt);
            }
        });

        txtPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPhoneNoKeyReleased(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Phone #:");

        txtDealerIn.setColumns(20);
        txtDealerIn.setRows(5);
        txtDealerIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDealerInKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtDealerIn);

        jLabel10.setForeground(new java.awt.Color(0, 0, 153));
        jLabel10.setText("Dealer In:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCity, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(txtPhoneNo))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)))
        );

        addimage.setText("Add Letter Head");
        addimage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addimageActionPerformed(evt);
            }
        });

        path.setFont(new java.awt.Font("SansSerif", 1, 10)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(addimage, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addimage)
                    .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout DialogCompanyLayout = new javax.swing.GroupLayout(DialogCompany.getContentPane());
        DialogCompany.getContentPane().setLayout(DialogCompanyLayout);
        DialogCompanyLayout.setHorizontalGroup(
            DialogCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        DialogCompanyLayout.setVerticalGroup(
            DialogCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogCompanyLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(214, 193, 240));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(983, 475));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/company.png"))); // NOI18N
        jLabel1.setText("Company");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tablecompany.setBackground(new java.awt.Color(255, 255, 153));
        tablecompany.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablecompany.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablecompanyMouseClicked(evt);
            }
        });
        tablecompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablecompanyKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablecompany);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.setToolTipText("");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/add.png"))); // NOI18N
        buttonAdd.setText("Add/Edit");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonexit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_exception.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));
        lbl_exception.setText("    ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                        .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogCompany.dispose();
        buttonAdd.setEnabled(true);
        buttonexit.setEnabled(true);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonExitActionPerformed

    private void update(){
         try{
             String password = txtEmailPassword.getText();
             String imagelocation = path.getText();
            if(txtCompanyName.getText().equals("")){
                lbl_exception.setText("Please Enter Company Name...");
                txtCompanyName.requestFocus();
            }else if(txtLocation.getText().equals("")){
                lbl_exception.setText("Please Enter Location...");
                txtLocation.requestFocus();
            }else if(txtAddress.getText().equals("")){
                lbl_exception.setText("Please Enter Address...");
                txtAddress.requestFocus();
            }else if(txtCity.getText().equals("")){
                lbl_exception.setText("Please Enter City...");
                txtCity.requestFocus();
            }else if(txtPhoneNo.getText().equals("")){
                lbl_exception.setText("Please Enter Phone #...");
                txtPhoneNo.requestFocus();
            }else if(txtFax.getText().equals("")){
                lbl_exception.setText("Please Enter Fax...");
                txtFax.requestFocus();
            }else if(txtEmailAddress.getText().equals("")){
                lbl_exception.setText("Please Enter Email Address...");
                txtEmailAddress.requestFocus();
            }else if(txtPin.getText().equals("")){
                lbl_exception.setText("Please Enter Pin #...");
                txtPin.requestFocus();
            }else if(txtAccount.getText().equals("")){
                lbl_exception.setText("Please Enter App A/C Name...");
                txtAccount.requestFocus();
            }else if(txtDealerIn.getText().equals("")){
                lbl_exception.setText("Please Enter Dealer In...");
                txtDealerIn.requestFocus();
            }else if(txtEmailPassword.getText().equals("")){
                lbl_exception.setText("Please Enter the Correct Email Password...");
                txtEmailPassword.requestFocus();
            }else if(imagelocation.equals("")){
                lbl_exception.setText("Please Choose Image Location...");
                addimage.requestFocus();
            }else{
                String sql = "UPDATE companytable SET company_name = '"+txtCompanyName.getText()+"',location = '"+txtLocation.getText()+"',"
                              + "address = '"+txtAddress.getText()+"',city = '"+txtCity.getText()+"',phone_no = '"+txtPhoneNo.getText()+"',"
                              + "fax = '"+txtFax.getText()+"',email = '"+txtEmailAddress.getText()+"',pin = '"+txtPin.getText()+"',account_name = '"+txtAccount.getText()+"'"
                        + ",Dealer_in = '"+txtDealerIn.getText()+"',website = '"+txtWebsite.getText()+"',email_password = '"+password+"',image = '"+imagelocation+"' WHERE id = '"+companyid+"'"
                        + " AND s = '1'";
                manage.update(sql); 
                reset();
                lbl_exception.setText("Company Details for "+txtCompanyName.getText()+" Updated...");
            }
         }catch(Exception e){
             
         }
        
    }
    public void sendemail(String sender,String to,String pass,String companyname){
        final String username = sender;
        final String password = pass;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Date date = new Date();

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
             message.setSubject("ALERT!!! Security-Software INSTALLATION/UPDATE TAKING PLACE at "+companyname+"");
             message.setText("Installation or Update done to Security-System in "+companyname+" located at "+txtLocation.getText()+", Address "+txtAddress.getText()+""
                     + ", in "+txtCity.getText()+", Phone # "+txtPhoneNo.getText()+" and Email Address "+txtEmailAddress.getText()+" AT '"+dateFormat.format(date)+"'"
                             + "... Are you Aware?");
             //update();
             System.out.println("Sending");
             Transport.send(message);
             System.out.println("Done");
             
         } catch (MessagingException e) {
             JOptionPane.showMessageDialog(null, "Cannot Update Company details without Internet Connection.Please Connect to internet...");
         }
    }
    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        update();  
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void txtCompanyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompanyNameKeyReleased
        
    }//GEN-LAST:event_txtCompanyNameKeyReleased

    private void txtLocationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocationKeyReleased
        txtLocation.setText(txtLocation.getText().toUpperCase());
    }//GEN-LAST:event_txtLocationKeyReleased

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        txtCity.setText(txtCity.getText().toUpperCase());
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyTyped
        
    }//GEN-LAST:event_txtPhoneNoKeyTyped

    private void txtPinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinKeyReleased
        txtPin.setText(txtPin.getText().toUpperCase());
    }//GEN-LAST:event_txtPinKeyReleased

    private void txtPhoneNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyReleased
        txtPhoneNo.setText(txtPhoneNo.getText().toUpperCase());
    }//GEN-LAST:event_txtPhoneNoKeyReleased

    private void txtEmailAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyReleased
        txtEmailAddress.setText(txtEmailAddress.getText().toLowerCase());
    }//GEN-LAST:event_txtEmailAddressKeyReleased

    private void txtWebsiteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWebsiteKeyReleased
        txtWebsite.setText(txtWebsite.getText().toLowerCase());
    }//GEN-LAST:event_txtWebsiteKeyReleased

    private void checkViewCharactersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkViewCharactersActionPerformed
        if(checkViewCharacters.isSelected()){
            txtEmailPassword.setEchoChar((char)0);
        }else{
            txtEmailPassword.setEchoChar('#');
        }
    }//GEN-LAST:event_checkViewCharactersActionPerformed

    private void tablecompanyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablecompanyMouseClicked
        selectedrow();
        manage.showdialog(DialogCompany, jPanel2, 825, 370);
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
    }//GEN-LAST:event_tablecompanyMouseClicked

    private void tablecompanyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablecompanyKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tablecompanyKeyReleased

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        manage.showdialog(DialogCompany, jPanel2, 825, 370);
        reset();
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        save();
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        reset();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        DialogCompany.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION){
            manage.showdialog(DialogCompany, jPanel2, 825, 370);
        }else if (response == JOptionPane.YES_OPTION){
            String sql = "UPDATE companytable SET s = '0' WHERE s = '1' AND id = '"+companyid+"' ";
            manage.update(sql);
            reset();
            lbl_exception.setText("Deleted Successfully...");
            
            manage.showdialog(DialogCompany, jPanel2, 825, 370);
        }else if(response == JOptionPane.CLOSED_OPTION){
            manage.showdialog(DialogCompany, jPanel2, 825, 370);    
        }
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void txtCompanyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompanyNameKeyPressed
       lbl_exception.setText("");
    }//GEN-LAST:event_txtCompanyNameKeyPressed

    private void txtLocationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLocationKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtLocationKeyPressed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPhoneNoKeyPressed

    private void txtFaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFaxKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtFaxKeyPressed

    private void txtEmailAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmailAddressKeyPressed

    private void txtWebsiteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWebsiteKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtWebsiteKeyPressed

    private void txtPinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPinKeyPressed

    private void txtEmailPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailPasswordKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmailPasswordKeyPressed

    private void txtDealerInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDealerInKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtDealerInKeyPressed

    private void txtAccountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountKeyReleased
        txtAccount.setText(txtAccount.getText().toUpperCase());
    }//GEN-LAST:event_txtAccountKeyReleased

    private void addimageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addimageActionPerformed
        DialogCompany.dispose();
        filechooser();
    }//GEN-LAST:event_addimageActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogCompany;
    private javax.swing.JButton addimage;
    public javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonUpdate;
    public javax.swing.JButton buttonexit;
    private javax.swing.JCheckBox checkViewCharacters;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel path;
    private javax.swing.JTable tablecompany;
    private javax.swing.JTextField txtAccount;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCompanyName;
    private javax.swing.JTextArea txtDealerIn;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JPasswordField txtEmailPassword;
    private javax.swing.JTextField txtFax;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPin;
    private javax.swing.JTextField txtWebsite;
    // End of variables declaration//GEN-END:variables
}
