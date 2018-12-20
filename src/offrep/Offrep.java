package offrep;

import com.tolclin.manage.Manage;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.mindrot.jbcrypt.BCrypt;
public class Offrep extends javax.swing.JFrame {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CompanyPanel company = new CompanyPanel();
        public String loggeduserid;
        private int id;
        public static String loggedid_number;
        public static String loggedusername;
        public static int loggedcompanyid;
        public static String loggedcompanyname;
        public static String loggedusertype;
        public static String logopath;
        private String company_phoneno;
        private String email_address;
        private String email_pass;
        private float daysBetween;
        Manage manage = new Manage();
        ImageIcon icon;
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
    public Offrep() {
        initComponents();
        conn = Manage.ConnecrDb();
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        buttonYes.setVisible(false);
        buttonNo.setVisible(false);
        lbl_exception.setText("");
        changeuser.setToolTipText("Log out to change  User");
        days_left();
        icon = new ImageIcon("icon.png");
        setIconImage(icon.getImage());
    }
    private void days_left(){
        try{
            String sql = "SELECT final_date FROM renew_table WHERE id = '1' AND s = '1' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_days.setToolTipText("click to Activate or Add Days. The Expiry date of your application is on "+rs.getString("final_date")+"");
                }
            
        }catch(Exception e){
           lbl_exception.setText(e+" days_left");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        
    }
    private void date_difference(){
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
	 try {
             String sql = "SELECT final_date,today_date FROM renew_table WHERE s = '1' AND id = '1'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    String dateBeforeString = rs.getString("today_date");
                    String dateAfterString = rs.getString("final_date");
                    Date dateBefore = myFormat.parse(dateBeforeString);
                    Date dateAfter = myFormat.parse(dateAfterString);
                    long difference = dateAfter.getTime() - dateBefore.getTime();
                    daysBetween = (difference / (1000*60*60*24));
                    lbl_days.setText(String.format("%.0f", daysBetween)+" Days left");
                }
	 } catch (Exception e) {
	       System.out.println(e+" date_difference");
	 }
    }
    private void resetLogin(){
        txtUsername.setText("");
        txtPassword.setText("");
        comboCompany.removeAllItems();
        comboCompany.addItem("Select Company");
        comboCompany.setSelectedItem("Select Company");
        String sql = "SELECT company_name FROM companytable WHERE s = '1'";
        manage.fillcombo(sql, comboCompany, "company_name");
        txtUsername.requestFocus();
    }
    public void login(){
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
         Date date = new Date();
         String today = (dateformat.format(date));
         
         String username = txtUsername.getText();
         String password = txtPassword.getText();
         
        try{
            String sql = "SELECT * FROM userstable WHERE s = '1' "
                    + "AND userstable.username = ?";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, username);
            
            rs = pst.executeQuery();
            
                if(rs.next()){
                    String hashed = rs.getString("userstable.password");
                    if(BCrypt.checkpw(password, hashed)){
                        InternalFrame internal = new InternalFrame();
                        loggeduserid = rs.getString("userstable.id");
                        loggedid_number = rs.getString("userstable.id_number");
                        loggedusername = rs.getString("userstable.name");
                        loggedusertype = rs.getString("userstable.user_type");
                        
                        lbl_loggedin.setText("("+loggedusertype+") - "+loggedusername+" is logged in...["+loggedcompanyname+"]");
                        internal.users.loggeduserid = loggeduserid;
                        
                        internal.payments.loggeduserid = loggeduserid;
                        internal.payments.loggedcompanyid = loggedcompanyid;
                        
                        internal.expenses.loggeduserid = loggeduserid;
                        internal.expenses.loggedcompanyid = loggedcompanyid;
                        
                        internal.quotation.loggeduserid = loggeduserid;
                        internal.quotation.email = email_address;
                        internal.quotation.email_pass = email_pass;
                        internal.quotation.companyname = loggedcompanyname;
                        internal.quotation.loggedcompanyid = loggedcompanyid;
                        internal.quotation.logopath = logopath;
                        
                        internal.report.email = email_address;
                        internal.report.email_pass = email_pass;
                        internal.report.companyname = loggedcompanyname;
                        internal.report.logopath = logopath;
                        
                        internal.loggedusertype = loggedusertype;
                        internal.payments.loggedusertype = loggedusertype;
                        internal.reminder.loggeduserid = loggeduserid;
                       
                        internal.invoices.email = email_address;
                        internal.invoices.email_pass = email_pass;
                        internal.invoices.loggeduserid = loggeduserid;
                        internal.invoices.companyname = loggedcompanyname;
                        internal.invoices.loggedcompanyid = loggedcompanyid;
                        internal.invoices.logopath = logopath;
                        
                        internal.clients.loggedcompanyid = loggedcompanyid;
                        
                        internal.report.loggedcompanyid = loggedcompanyid;
                        
                     

                        manage.update("UPDATE userstable SET last_login = '"+dateFormat.format(date.getTime())+"' WHERE id = '"+loggeduserid+"'");
                        this.setTitle("Offrep for "+loggedcompanyname);
                        desktop.add(internal);
                        internal.setSize(desktop.getWidth(), desktop.getHeight());
                        internal.setVisible(true);
                        internal.priviledges();
                        LoginDialog.dispose();
                        
                        loadreminder();
                        //-----------------------------------------------------------------------------------------------------------

                        manage.updatedate("UPDATE renew_table SET today_date = '"+today+"' WHERE id = '1'");//update date today
                        //poison();
//                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                        Calendar cal = Calendar.getInstance();
//                        cal.setTime(new Date());
//                        cal.add(Calendar.DATE,0);
//                        String initial_date = df.format(cal.getTime());
//                        String query = "UPDATE backup_table SET today_date = '"+initial_date+"' WHERE id = '1'";
//                        manage.update(query);
                        //------------------------------------------------------------------------------------------------------------__comparing date__
                        compare_date();
                        //------------------------------------------------------------------------------------------------------------
                        date_difference();
                   }else{
                        txtUsername.setText("");
                        txtPassword.setText("");
                        lbl_exception.setText("Password Doesn't match...");
                    }
                }else{
                    txtUsername.setText("");
                    txtPassword.setText("");
                    lbl_exception.setText("Access Denied. Wrong Username or Password...");
                }
        }catch(Exception e){
            lbl_exception.setText(e+" mainswitch.login");
            System.out.println(e+" Offrep Mainswitch login");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void load_table(){
        String sql_table = "SELECT id, date AS 'Recording Date',details AS 'Reminder Details',final_date AS 'Reminder Date' FROM reminder_table WHERE s = '1'"
                + " ORDER BY final_date";
        manage.update_table(sql_table, tableReminder);
    }
    private void loadreminder(){
        buttonReminder.setEnabled(false);
        cal.setTime(new Date());
        cal.add(Calendar.DATE,0);
        String initial_date = df.format(cal.getTime());
        String sql = "SELECT * FROM reminder_table WHERE final_date <= '"+initial_date+"' AND s = '1'";
        if(manage.checking(sql) == 1){
            manage.showdialog(ReminderDialog, desktop, 909, 479);
            load_table();
            lbl_exception.setText("Reminders Available, Please Attend to them...");
            String path = "reminder.wav";
            manage.music(path);
        }else{
            lbl_exception.setText("");
        }
    }
    private void selectedrowreminder(){
        try{
            buttonReminder.setEnabled(true);
            int row = tableReminder.getSelectedRow();
            String table_click = tableReminder.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM reminder_table WHERE s = '1' AND id = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                }
        }catch(Exception e){
            System.out.println(e+" nazi.selectedrowreminder");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void attend(){
      
       String sql = "UPDATE reminder_table SET s = '0' WHERE s = '1' AND id = '"+id+"'";
       manage.update(sql);
       loadreminder();
       load_table();
    }
    
    //    _____________________________renew code_____________________________________________
    
    public void showrenewdialog(){
        manage.showdialog(DialogRenew, jPanel1, 669, 349);
        txtCode.setText("");
        txtCode.requestFocus();
        jLabel6.setVisible(false);
        txtCode.setVisible(false);
        buttonActivate.setVisible(false);
        buttonSendCode.setEnabled(false);
        buttonYes.setEnabled(true);
        buttonNo.setEnabled(true);
        resetPush();
    }
//    _________________________________________________________end renew code_______________
    
    //            ___________________generate time for renew_______________________
     public void renewingtime(){//activate immediately the code has accepted.
            String amount = comboAmount.getSelectedItem().toString();
            amount = amount.trim();
            int renewal_amount = Integer.parseInt(amount);
            int days = renewal_amount/15 ;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, days);
            daysBetween = 0;
                try{
                    String sql = "SELECT * FROM renew_table WHERE s = '1' AND id = '1'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                        if(rs.next()){
                            Date final_d = rs.getDate("final_date");
                            Date initial_d = rs.getDate("today_date");
                            
                                if(initial_d == final_d || initial_d.after(final_d)){
                                    c.add(Calendar.DATE, days);
                                }else{
                                    
                                    date_difference();
                                    int daybtwn = (int)(Math.round(daysBetween));
                                    
                                    c.add(Calendar.DATE, days - (- daybtwn));
                                }
                                String output = sdf.format(c.getTime());
                                manage.updatedate("UPDATE renew_table SET final_date = '"+output+"' WHERE id = '1'");
                        }
                }catch(Exception e){
                    System.out.println(e+" ");
                }finally{
                    try{
                        rs.close();
                        pst.close();
                    }catch(Exception e){

                    }
                }
    } 
    private void compare_date(){
         try{
             String sql = "SELECT final_date,today_date FROM renew_table WHERE id = '1'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    Date final_date = rs.getDate("final_date");
                    Date today_date = rs.getDate("today_date");
                    
                    if(today_date.after(final_date)){
                        showrenewdialog();
                    }else if(today_date.equals(final_date)){
//                        String path = "nazi.wav";
//                        manage.music(path);
                    }else{
                        
                    }
                   
                }
         }catch(Exception e){
             System.out.println("compare_date");
         }finally{
             try{
                rs.close();
                pst.close();
             }catch(Exception e){
                 
             }
         }
     }
//            ___________________________end renew time generation___________________
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoginDialog = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        buttonExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        buttonLogin = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboCompany = new javax.swing.JComboBox<>();
        DialogRenew = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        buttonActivate = new javax.swing.JButton();
        txtCode = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        buttonSendCode = new javax.swing.JButton();
        comboAmount = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        buttonPush = new javax.swing.JButton();
        comboAmount_push = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtPhone_push = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtCode_push = new javax.swing.JTextField();
        buttonActivate1 = new javax.swing.JButton();
        buttonReset = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        ReminderDialog = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        buttonReminder = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableReminder = new javax.swing.JTable();
        buttonexit = new javax.swing.JButton();
        desktop = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lbl_loggedin = new javax.swing.JLabel();
        buttonYes = new javax.swing.JButton();
        buttonNo = new javax.swing.JButton();
        lbl_exception = new javax.swing.JLabel();
        changeuser = new javax.swing.JButton();
        lbl_days = new javax.swing.JLabel();

        LoginDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        LoginDialog.setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(214, 193, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/login.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(214, 193, 240));

        buttonExit.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel1.setText("Username:");

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel2.setText("Password:");

        buttonLogin.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/icons8-login-button-30.png"))); // NOI18N
        buttonLogin.setText("Login");
        buttonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoginActionPerformed(evt);
            }
        });
        buttonLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buttonLoginKeyPressed(evt);
            }
        });

        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel4.setText("Company:");

        comboCompany.setBackground(new java.awt.Color(255, 255, 255));
        comboCompany.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        comboCompany.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboCompanyPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(buttonLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboCompany, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonLogin)
                    .addComponent(buttonExit))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel3)
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout LoginDialogLayout = new javax.swing.GroupLayout(LoginDialog.getContentPane());
        LoginDialog.getContentPane().setLayout(LoginDialogLayout);
        LoginDialogLayout.setHorizontalGroup(
            LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        LoginDialogLayout.setVerticalGroup(
            LoginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        DialogRenew.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogRenew.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                DialogRenewWindowClosing(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        jPanel4.setBackground(java.awt.Color.white);
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jLabel6.setText("Enter Code Here:");

        buttonActivate.setText("Activate");
        buttonActivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActivateActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        jLabel8.setForeground(java.awt.Color.blue);
        jLabel8.setText("Enter Amount and click 'Send Code' button for code to be sent to you after verification.");

        jLabel9.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        jLabel9.setForeground(java.awt.Color.blue);
        jLabel9.setText("Ensure there is internet connection. This will not take a lot of time...");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 204)));

        jLabel7.setText("Enter Amount:");

        buttonSendCode.setText("Send Code");
        buttonSendCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendCodeActionPerformed(evt);
            }
        });

        comboAmount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Amount", "450", "900", "1350", "1800", "2250", "2700", "3150", "3600", "4050", "4500", "4950", "5400" }));
        comboAmount.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboAmountPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel10.setText("Enter Phone #:");

        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPhone))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSendCode, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(buttonSendCode)
                    .addComponent(comboAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 0, 0));
        jLabel11.setText("NOTE: Please use the same phone# you used to do the payment");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonActivate, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCode))
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(buttonActivate)
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("Send Code", jPanel4);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 204)));

        jLabel12.setText("Enter Amount:");

        buttonPush.setText("Push");
        buttonPush.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPushActionPerformed(evt);
            }
        });

        comboAmount_push.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Amount", "450", "900", "1350", "1800", "2250", "2700", "3150", "3600", "4050", "4500", "4950", "5400", "1" }));
        comboAmount_push.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboAmount_pushPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel13.setText("Enter Phone #:");

        txtPhone_push.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhone_pushKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhone_pushKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhone_push))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboAmount_push, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonPush, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(buttonPush)
                    .addComponent(comboAmount_push, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone_push, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        jLabel14.setText("Enter Code Here:");

        txtCode_push.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCode_pushKeyReleased(evt);
            }
        });

        buttonActivate1.setText("Activate");
        buttonActivate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActivate1ActionPerformed(evt);
            }
        });

        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        jLabel15.setForeground(java.awt.Color.blue);
        jLabel15.setText("Paying Using MPESA STK by selecting the amount you want and then push. Observe your ");

        jLabel16.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        jLabel16.setForeground(java.awt.Color.blue);
        jLabel16.setText("phone and enter the pin for the transaction to take place. A code will be sent to you and");

        jLabel17.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        jLabel17.setForeground(java.awt.Color.blue);
        jLabel17.setText("in the field provided. Rememeber to enter you phone no correctly.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonActivate1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCode_push, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtCode_push, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonActivate1)
                    .addComponent(buttonReset))
                .addGap(50, 50, 50))
        );

        jTabbedPane1.addTab("STK Push", jPanel8);

        javax.swing.GroupLayout DialogRenewLayout = new javax.swing.GroupLayout(DialogRenew.getContentPane());
        DialogRenew.getContentPane().setLayout(DialogRenewLayout);
        DialogRenewLayout.setHorizontalGroup(
            DialogRenewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        DialogRenewLayout.setVerticalGroup(
            DialogRenewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        ReminderDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ReminderDialog.setUndecorated(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        buttonReminder.setText("Reminder Attended");
        buttonReminder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReminderActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reminders", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 2, 12), new java.awt.Color(0, 0, 153))); // NOI18N

        tableReminder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableReminder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableReminderMouseClicked(evt);
            }
        });
        tableReminder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableReminderKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableReminder);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );

        buttonexit.setText("Exit");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(buttonReminder, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReminder)
                    .addComponent(buttonexit))
                .addContainerGap())
        );

        javax.swing.GroupLayout ReminderDialogLayout = new javax.swing.GroupLayout(ReminderDialog.getContentPane());
        ReminderDialog.getContentPane().setLayout(ReminderDialogLayout);
        ReminderDialogLayout.setHorizontalGroup(
            ReminderDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReminderDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ReminderDialogLayout.setVerticalGroup(
            ReminderDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReminderDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        desktop.setBackground(new java.awt.Color(214, 193, 240));
        desktop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(214, 193, 240));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbl_loggedin.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        lbl_loggedin.setForeground(new java.awt.Color(0, 153, 0));

        buttonYes.setBackground(new java.awt.Color(204, 0, 0));
        buttonYes.setForeground(new java.awt.Color(255, 255, 255));
        buttonYes.setText("Yes");
        buttonYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonYesActionPerformed(evt);
            }
        });

        buttonNo.setBackground(new java.awt.Color(0, 153, 0));
        buttonNo.setForeground(new java.awt.Color(255, 255, 255));
        buttonNo.setText("No");
        buttonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNoActionPerformed(evt);
            }
        });

        lbl_exception.setFont(new java.awt.Font("SansSerif", 2, 12)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));
        lbl_exception.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_exceptionMouseClicked(evt);
            }
        });

        changeuser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/changeuser.png"))); // NOI18N
        changeuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeuserActionPerformed(evt);
            }
        });

        lbl_days.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_days.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_daysMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(changeuser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_loggedin, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_days, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonYes, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonNo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl_days, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(buttonYes)
                                    .addComponent(buttonNo))))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(changeuser)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbl_loggedin, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        manage.showdialog(LoginDialog, desktop, 647,263);
        this.requestFocus();
        resetLogin();
        
    }//GEN-LAST:event_formWindowOpened

    private void buttonNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNoActionPerformed
        lbl_exception.setText("");
        buttonYes.setVisible(false);
        buttonNo.setVisible(false);
        buttonNo.requestFocus();
        loadreminder();
    }//GEN-LAST:event_buttonNoActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        lbl_exception.setText("Are you sure you want to quit?");
        buttonYes.setVisible(true);
        buttonNo.setVisible(true);
        buttonNo.requestFocus();
    }//GEN-LAST:event_buttonExitActionPerformed

    
    private void buttonYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonYesActionPerformed
       
        if(lbl_exception.getText().equals("Are you SURE you want to Change Users?")){
            try{
                Runtime.getRuntime().exec("java -jar Gracious_Events.jar");                              
                System.exit(0);
            }catch (IOException e) {
                
            }
        }else{
            System.exit(0);
        }
    }//GEN-LAST:event_buttonYesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        lbl_exception.setText("Are you sure you want to quit?");
        buttonYes.setVisible(true);
        buttonNo.setVisible(true);
        buttonNo.requestFocus();
    }//GEN-LAST:event_formWindowClosing

    private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoginActionPerformed
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
              if(txtUsername.getText().equals("")){
                  lbl_exception.setText("Please Enter Username...");
                  txtUsername.requestFocus();
              }else if(txtPassword.getText().equals("")){
                  lbl_exception.setText("Please Enter Password...");
                  txtPassword.requestFocus();
              }else if(comboCompany.getSelectedItem().toString().equals("Select Company")){
                  lbl_exception.setText("Please Enter Company...");
                  comboCompany.requestFocus();
              }else{
                  login();
              }
              
            return null;
           }
           
       };
       worker.execute(); 
        
    }//GEN-LAST:event_buttonLoginActionPerformed

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        
    }//GEN-LAST:event_txtUsernameFocusGained

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        lbl_exception.setText("");
        buttonYes.setVisible(false);
        buttonNo.setVisible(false);
        buttonNo.requestFocus();
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            login();
        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void buttonLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buttonLoginKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            login();
        }
    }//GEN-LAST:event_buttonLoginKeyPressed

    private void changeuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeuserActionPerformed
        lbl_exception.setText("Are you SURE you want to Change Users?");
        buttonYes.setVisible(true);
        buttonNo.setVisible(true);
    }//GEN-LAST:event_changeuserActionPerformed

    private void buttonSendCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendCodeActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
               String code_ = (String)comboAmount.getSelectedItem();
               if(code_.equals("Select Amount") || txtPhone.getText().equals("")){
                    lbl_exception.setText("Please Enter All the details required...");
                    txtPhone.requestFocus();
               }else{
                    buttonSendCode.setEnabled(false);
                    comboAmount.setEnabled(false);
                    txtPhone.setEnabled(false);
                    DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String today = (dateformat.format(date));

                    GeneratedCode generated = new GeneratedCode();
                    String subject_to_me = ""+loggedcompanyname+" Generated Code";
                    String codegen = generated.generation();

                    String query = "UPDATE renew_table SET code = '"+codegen+"' WHERE s = '1' AND id = '1'";
                    manage.update(query);

                    String message_to_me = "Company Name: "+loggedcompanyname+" Phone #: "+company_phoneno+" AND Email Address "+email_address+" Generated Code:"
                            + " "+codegen+" AS "+today+" AMOUNT Kshs."+(String)comboAmount.getSelectedItem()+"";
                    String message = ""+loggedcompanyname+",Phone#:"+txtPhone.getText()+",Code:"+codegen+",Amount:"+(String)comboAmount.getSelectedItem()+"";

                    //manage.sendmessage("0723095840",message);
                    manage.sendnotification_emailtome("josephmwawasi29@gmail.com","tolclin.it@gmail.com","J35u5Christ",subject_to_me,message_to_me,DialogRenew);
                    comboAmount.setEnabled(false);
                    txtPhone.setEnabled(false);
                    jLabel6.setVisible(true);
                    txtCode.setVisible(true);
                    txtCode.requestFocus();
                    buttonActivate.setVisible(true);
                    buttonSendCode.setEnabled(false);
               }
                return null;
           }
           
       };
       worker.execute();    
    }//GEN-LAST:event_buttonSendCodeActionPerformed

    private void comboAmountPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboAmountPopupMenuWillBecomeInvisible
        buttonSendCode.setEnabled(true);
        lbl_exception.setText("");
    }//GEN-LAST:event_comboAmountPopupMenuWillBecomeInvisible

    private void DialogRenewWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogRenewWindowClosing
        DialogRenew.dispose();
        this.setEnabled(true);
        lbl_exception.setText("");
    }//GEN-LAST:event_DialogRenewWindowClosing

   
    private void buttonActivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActivateActionPerformed
        
        try{
            String sql = "SELECT * FROM renew_table WHERE code = ? AND id = '1' AND s = '1'";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtCode.getText());
            rs = pst.executeQuery();
                if(rs.next()){
                    DialogRenew.dispose();
                    renewingtime();
                    this.setEnabled(true);
                    daysBetween = 0;
                    date_difference();
//                    String path = "thankyou.wav";
//                    manage.music(path);
                }
        }catch(Exception e){
            DialogRenew.dispose();
            JOptionPane.showMessageDialog(null, e+" offrep.buttonActivateActionPerformed");
            showrenewdialog();
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_buttonActivateActionPerformed

    private void txtPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
                ||(vchar == KeyEvent.VK_BACK_SPACE)

                || (vchar == KeyEvent.VK_DELETE)){
              evt.consume();
         }
    }//GEN-LAST:event_txtPhoneKeyTyped

    private void txtPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPhoneKeyPressed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        lbl_exception.setText("");
        buttonYes.setVisible(false);
        buttonNo.setVisible(false);
        buttonNo.requestFocus();
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            login();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void lbl_exceptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_exceptionMouseClicked
        loadreminder();
    }//GEN-LAST:event_lbl_exceptionMouseClicked

    private void tableReminderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableReminderMouseClicked
        selectedrowreminder();
    }//GEN-LAST:event_tableReminderMouseClicked

    private void tableReminderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableReminderKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrowreminder();
        }
    }//GEN-LAST:event_tableReminderKeyReleased

    private void buttonReminderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReminderActionPerformed
        
        ReminderDialog.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you have attended this reminder???", "Confirm",JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                manage.showdialog(ReminderDialog, desktop, 909, 479);
            }else if (response == JOptionPane.YES_OPTION){
                attend();
                manage.showdialog(ReminderDialog, desktop, 909, 479);
            }else if(response == JOptionPane.CLOSED_OPTION){
                manage.showdialog(ReminderDialog, desktop, 909, 479);
            }
    }//GEN-LAST:event_buttonReminderActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        ReminderDialog.dispose();
    }//GEN-LAST:event_buttonexitActionPerformed

    private void lbl_daysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_daysMouseClicked
        showrenewdialog();
    }//GEN-LAST:event_lbl_daysMouseClicked

    Mpesa mpesa;
    private void pay(){
        
        String phoneno = "254"+txtPhone_push.getText().substring(txtPhone_push.getText().length() - 9);
        String amount = comboAmount_push.getSelectedItem().toString();
            try {
                String sql = "SELECT * FROM companytable WHERE id = ? ";
                pst = conn.prepareStatement(sql);
                        
                pst.setInt(1, loggedcompanyid);
                rs = pst.executeQuery();
                    if(rs.next()){
                        String accountname = rs.getString("account_name");
                        GeneratedCode generated = new GeneratedCode();
                        String codegen = generated.generation();
                        String account = "Offrep-"+accountname;
//                        mpesa = new Mpesa("mQNxJDUjAOc6iiOlO4tH0p4R1GHzOs1M","zSsJvTTXAPJ0lpME");
//                        mpesa.STKPushSimulation("224343","MjI0MzQzN2IyZGEyZTM2Y2ZiYjc4YjU0ZmRkODliMjVlMDgyZThhZWJmMmE0MDRmNWE4Y2ExM2VkN2I0M2I2Yjk5NjE4YzIwMTgxMTAzMTYwNzMz",
//                            "20181103160733","CustomerPayBillOnline",""+amount+"",""+phoneno+"",""+phoneno+"","224343","http://tolclin.com/mpesa-api/callback_url.php?account="+account+"_"+codegen+"",
//                             "",""+account+"",""+account+"");
                        String query = "UPDATE renew_table SET code = '"+codegen+"' WHERE id = '1'";
                        manage.update(query); 
                        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String today = (dateformat.format(date));
                        String message_to_me = "Company Name: "+loggedcompanyname+" Phone #: "+company_phoneno+" AND Email Address "+email_address+" Generated Code:"
                            + " "+codegen+" AS "+today+" AMOUNT Kshs."+(String)comboAmount_push.getSelectedItem()+" for Account "+account+"";
                        String subject_to_me = ""+loggedcompanyname+" Generated Code";
                        manage.sendnotification_emailtome("josephmwawasi29@gmail.com","tolclin.it@gmail.com","J35u5Christ",subject_to_me,message_to_me,DialogRenew);
                        comboAmount_push.setEnabled(false);
                        buttonPush.setEnabled(false);
                        txtPhone_push.setEnabled(false);
                        jLabel14.setVisible(true);
                        txtCode_push.setVisible(true);
                        txtCode_push.setText("");
                        buttonReset.setVisible(true);
                        buttonActivate1.setVisible(true);
                        txtCode_push.requestFocus();
                    }
            } catch (Exception ex) {
                System.out.println(ex);
        }
            
    }
    private void resetPush(){
        comboAmount_push.setEnabled(true);
        buttonPush.setEnabled(true);
        txtPhone_push.setEnabled(true);
        txtPhone_push.setText("");
        comboAmount_push.setSelectedItem("Select Amount");
        txtCode_push.setText("");
        comboAmount_push.requestFocus();
        jLabel14.setVisible(false);
        txtCode_push.setText("");
        txtCode_push.setVisible(false);
        buttonReset.setVisible(false);
        buttonActivate1.setVisible(false);
    }
    private void buttonPushActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPushActionPerformed
       SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
                if(comboAmount_push.getSelectedItem().toString().equals("Select Amount")){
                    lbl_exception.setText("Please Select Amount...");
                    comboAmount_push.requestFocus();
                }else if(txtPhone_push.getText().equals("")){
                    lbl_exception.setText("Please Enter Phone #...");
                    txtPhone_push.requestFocus();
                }else{
                    comboAmount_push.setEnabled(false);
                    
                    pay();
                }
                return null;
           }
           
       };
       worker.execute();    
    }//GEN-LAST:event_buttonPushActionPerformed

    private void comboAmount_pushPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboAmount_pushPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_comboAmount_pushPopupMenuWillBecomeInvisible

    private void txtPhone_pushKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhone_pushKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhone_pushKeyTyped

    private void txtPhone_pushKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhone_pushKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPhone_pushKeyPressed

    private void buttonActivate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActivate1ActionPerformed
       try{
           String sql = "SELECT * FROM renew_table WHERE id = '1'";
           pst = conn.prepareStatement(sql);
           rs = pst.executeQuery();
            if(rs.next()){
               String serial = txtCode_push.getText();
               String code = rs.getString("code");
                if(code.equals(serial)){
                    DialogRenew.dispose();
                    renewingtime();
                    daysBetween = 0;
                    date_difference();
//                    String path = "thankyou.wav";
//                    manage.music(path);
                    this.setEnabled(true);
                }
            }
       }catch(Exception e){
           
       }finally{
           try{
               rs.close();
               pst.close();
           }catch(Exception e){
               
           }
       }
    }//GEN-LAST:event_buttonActivate1ActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        resetPush();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void comboCompanyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboCompanyPopupMenuWillBecomeInvisible
        lbl_exception.setText("");
        buttonYes.setVisible(false);
        buttonNo.setVisible(false);
        buttonNo.requestFocus();
        try{
            String sql = "SELECT * FROM companytable WHERE s = '1' AND company_name = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)comboCompany.getSelectedItem());
            rs = pst.executeQuery();
                if(rs.next()){
                    loggedcompanyid = rs.getInt("id");
                    loggedcompanyname = rs.getString("company_name");
                    company_phoneno = rs.getString("phone_no");
                    email_address = rs.getString("email");
                    email_pass = rs.getString("email_password");
                    logopath = rs.getString("image");
                }
        }catch(Exception e){
            lbl_exception.setText(e+" comboCompany");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_comboCompanyPopupMenuWillBecomeInvisible

    private void txtCode_pushKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCode_pushKeyReleased
        txtCode_push.setText(txtCode_push.getText().toUpperCase());
    }//GEN-LAST:event_txtCode_pushKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Offrep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Offrep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Offrep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Offrep.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Offrep().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogRenew;
    private javax.swing.JDialog LoginDialog;
    private javax.swing.JDialog ReminderDialog;
    private javax.swing.JButton buttonActivate;
    private javax.swing.JButton buttonActivate1;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonLogin;
    private javax.swing.JButton buttonNo;
    private javax.swing.JButton buttonPush;
    private javax.swing.JButton buttonReminder;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSendCode;
    private javax.swing.JButton buttonYes;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton changeuser;
    private javax.swing.JComboBox<String> comboAmount;
    private javax.swing.JComboBox<String> comboAmount_push;
    private javax.swing.JComboBox<String> comboCompany;
    private javax.swing.JPanel desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_days;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel lbl_loggedin;
    private javax.swing.JTable tableReminder;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtCode_push;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPhone_push;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
