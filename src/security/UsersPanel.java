
package security;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UsersPanel extends javax.swing.JPanel {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        public String loggeduserid;
        private int userid;
        
    public UsersPanel() {
        initComponents();
        conn = Javaconnect.ConnecrDb();
        loadtable();
    }
    private void loadtable(){
       Manage manage = new Manage();
        String sql = "SELECT id AS 'id',id_number AS 'ID Number',name AS 'Name of User',phone_no AS 'Phone #',last_login AS 'Last Login'FROM userstable WHERE s = '1'";
        manage.update_table(sql, tableUsers);
    }
    private void selectedrow(){
        try{
            buttonSave.setEnabled(false);
            buttonUpdate.setEnabled(true);
            buttonDelete.setEnabled(true);
            lbl_exception.setText("");
        
            int row = tableUsers.getSelectedRow();
            String table_click = tableUsers.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM userstable WHERE id = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    userid = rs.getInt("id");
                    txtIdNumber.setText(rs.getString("id_number"));
                    txtName.setText(rs.getString("name"));
                    txtPhoneNo.setText(rs.getString("phone_no"));
                    comboUsertype.setSelectedItem(rs.getString("user_type"));
                    
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" selectedrow");
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
            if(txtIdNumber.getText().equals("")){
                lbl_exception.setText("Please Enter ID Number...");
                txtIdNumber.requestFocus();
            }else if(txtName.getText().equals("")){
                lbl_exception.setText("Please Enter Name...");
                txtName.requestFocus();
            }else if(txtPhoneNo.getText().equals("")){
                lbl_exception.setText("Please Enter Phone #...");
                txtPhoneNo.requestFocus();
            }else if(comboUsertype.getSelectedItem().toString().equals("Select Usertype")){
                lbl_exception.setText("Please Select Usertype...");
            }else if(txtUsername.getText().equals("")){
                lbl_exception.setText("Please Enter Username...");
                txtUsername.requestFocus();
            }else if(txtPassword.getText().equals("")){
                lbl_exception.setText("Please Enter Password...");
                txtPassword.requestFocus();
            }else if(txtConfirmPassword.getText().equals("")){
                lbl_exception.setText("Please Confirm Password...");
                txtConfirmPassword.requestFocus();
            }else if(!txtPassword.getText().equals(txtConfirmPassword.getText())){
                lbl_exception.setText("Password doesn't match. Please Enter Password and Confirm...");
                txtConfirmPassword.setText("");
                txtConfirmPassword.setText("");
                txtConfirmPassword.requestFocus();
            }else{

                String sql = "INSERT INTO userstable(id_number,name,phone_no,user_type,username,password,last_login,company_id,s,logged) VALUES"
                        + "(?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);

                pst.setString(1, txtIdNumber.getText());
                pst.setString(2, txtName.getText());
                pst.setString(3, txtPhoneNo.getText());
                pst.setString(4, (String)comboUsertype.getSelectedItem());
                pst.setString(5, txtUsername.getText());
                pst.setString(6, txtConfirmPassword.getText());
                pst.setString(7, "0000-00-00 00:00:00");
                pst.setString(8, "1");
                pst.setString(9, "1");
                pst.setString(10, "0");

                pst.execute();
                reset();
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" save");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void update(){
        if(txtIdNumber.getText().equals("")){
                lbl_exception.setText("Please Enter ID Number...");
                txtIdNumber.requestFocus();
            }else if(txtName.getText().equals("")){
                lbl_exception.setText("Please Enter Name...");
                txtName.requestFocus();
            }else if(txtPhoneNo.getText().equals("")){
                lbl_exception.setText("Please Enter Phone #...");
                txtPhoneNo.requestFocus();
            }else if(comboUsertype.getSelectedItem().toString().equals("Select Usertype")){
                lbl_exception.setText("Please Select Usertype...");
            }else if(txtUsername.getText().equals("")){
                lbl_exception.setText("Please Enter Username...");
                txtUsername.requestFocus();
            }else if(txtPassword.getText().equals("")){
                lbl_exception.setText("Please Enter Password...");
                txtPassword.requestFocus();
            }else if(txtConfirmPassword.getText().equals("")){
                lbl_exception.setText("Please Confirm Password...");
                txtConfirmPassword.requestFocus();
            }else if(!txtPassword.getText().equals(txtConfirmPassword.getText())){
                lbl_exception.setText("Password doesn't match. Please Enter Password and Confirm...");
                txtConfirmPassword.setText("");
                txtConfirmPassword.setText("");
                txtConfirmPassword.requestFocus();
            }else{
                Manage manage = new Manage();
                String sql = "UPDATE userstable SET id_number = '"+txtIdNumber.getText()+"',name = '"+txtName.getText()+"',phone_no = '"+txtPhoneNo.getText()+"',"
                        + "user_type = '"+comboUsertype.getSelectedItem().toString()+"',username = '"+txtUsername.getText()+"',password = '"+txtConfirmPassword.getText()+"'"
                        + "WHERE id = '"+userid+"'";
                manage.update(sql);
                String name = "User "+txtName.getText()+"";
                manage.loggedmessageupdate(name);
                loadtable();
            }
    }
   
    public void showdialogusers(){
        DialogUsers.setVisible(true);
        DialogUsers.setSize(812, 279);
        DialogUsers.setAlwaysOnTop(true);
        DialogUsers.setLocationRelativeTo(this);
        DialogUsers.setResizable(false);
    }
    public void reset(){
        txtIdNumber.setText("");
        txtName.setText("");
        txtPhoneNo.setText("");
        comboUsertype.setSelectedItem("Select Usertype");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtIdNumber.requestFocus();
        
        buttonSave.setEnabled(true);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
        loadtable();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogUsers = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboUsertype = new javax.swing.JComboBox<>();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtPhoneNo = new javax.swing.JTextField();
        txtIdNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        buttonexit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        lbl_exception = new javax.swing.JLabel();

        DialogUsers.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogUsers.setUndecorated(true);
        DialogUsers.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogUsersWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Confirm Password:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Password:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Usertype:");

        comboUsertype.setBackground(new java.awt.Color(255, 255, 255));
        comboUsertype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Usertype", "Admin" }));
        comboUsertype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        comboUsertype.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboUsertypePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        txtConfirmPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtConfirmPasswordKeyPressed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("Username:");

        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtConfirmPassword)
                    .addComponent(txtPassword)
                    .addComponent(comboUsertype, 0, 195, Short.MAX_VALUE)
                    .addComponent(txtUsername))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(comboUsertype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        txtPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneNoKeyPressed(evt);
            }
        });

        txtIdNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdNumberKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdNumberKeyPressed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("ID Number:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Name:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Phone #:");

        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNameKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNameKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/update.png"))); // NOI18N
        buttonUpdate.setText("Update");
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateActionPerformed(evt);
            }
        });

        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
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
                .addGap(54, 54, 54)
                .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReset)
                    .addComponent(buttonExit)
                    .addComponent(buttonSave)
                    .addComponent(buttonUpdate)
                    .addComponent(buttonDelete))
                .addContainerGap())
        );

        javax.swing.GroupLayout DialogUsersLayout = new javax.swing.GroupLayout(DialogUsers.getContentPane());
        DialogUsers.getContentPane().setLayout(DialogUsersLayout);
        DialogUsersLayout.setHorizontalGroup(
            DialogUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogUsersLayout.setVerticalGroup(
            DialogUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogUsersLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(1055, 553));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/users.png"))); // NOI18N
        jLabel1.setText("Users");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tableUsers.setBackground(new java.awt.Color(255, 255, 153));
        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUsersMouseClicked(evt);
            }
        });
        tableUsers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableUsersKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableUsers);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.setToolTipText("");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/edit.png"))); // NOI18N
        buttonAdd.setText("Add/Edit");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonexit))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lbl_exception.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));
        lbl_exception.setText("    ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(287, 287, 287)
                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogUsers.dispose();
        buttonAdd.setEnabled(true);
        buttonexit.setEnabled(true);
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        reset();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void tableUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUsersMouseClicked
        selectedrow();
        showdialogusers();
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
    }//GEN-LAST:event_tableUsersMouseClicked

    private void tableUsersKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableUsersKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableUsersKeyReleased

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        
        try{
            String idnumber = txtIdNumber.getText();
            String name = txtName.getText();
            String phone = txtPhoneNo.getText();
            String usertype = comboUsertype.getSelectedItem().toString();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String confirm = txtConfirmPassword.getText();
                if(idnumber.equals("")){
                    lbl_exception.setText("Please Enter ID Number...");
                    txtIdNumber.requestFocus();
                }else if(name.equals("")){
                    lbl_exception.setText("Please Enter Name...");
                    txtName.requestFocus();
                }else if(phone.equals("")){
                    lbl_exception.setText("Please Enter Phone #...");
                    txtPhoneNo.requestFocus();
                }else if(usertype.equals("Select Usertype")){
                    lbl_exception.setText("Please Select Usertype...");
                }else if(username.equals("")){
                    lbl_exception.setText("Please Enter Username...");
                    txtUsername.requestFocus();
                }else if(password.equals("")){
                    lbl_exception.setText("Please Enter Password...");
                    txtPassword.requestFocus();
                }else if(confirm.equals("")){
                    lbl_exception.setText("Please Confirm Password...");
                    txtConfirmPassword.requestFocus();
                }else if(!password.equals(confirm)){
                    lbl_exception.setText("Password Doesn't match. Please Re-enter and Confirm Password...");
                    txtPassword.setText("");
                    txtConfirmPassword.setText("");
                    txtPassword.requestFocus();
                    
                }else{
                    String sql = "SELECT * FROM userstable WHERE s = '1' AND name = ?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, name);
                    rs = pst.executeQuery();
                        if(rs.next()){
                            DialogUsers.dispose();
                            JOptionPane.showMessageDialog(null,"User Exists...Please Enter another user");
                            showdialogusers();
                            reset();
                        }else{
                            save();
                        }
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, e+" ");
            }finally{
                try{
                    rs.close();
                    pst.close();
                }catch(Exception e){

                }
            }
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        showdialogusers();
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
        reset();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void txtIdNumberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdNumberKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtIdNumberKeyPressed

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPhoneNoKeyPressed

    private void comboUsertypePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboUsertypePopupMenuWillBecomeInvisible
        lbl_exception.setText("");
    }//GEN-LAST:event_comboUsertypePopupMenuWillBecomeInvisible

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtConfirmPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConfirmPasswordKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtConfirmPasswordKeyPressed

    private void txtIdNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdNumberKeyTyped
         char vchar = evt.getKeyChar();
            if((!(Character.isDigit(vchar)))
                ||(vchar == KeyEvent.VK_BACK_SPACE)

                || (vchar == KeyEvent.VK_DELETE)){
              evt.consume();
          }
    }//GEN-LAST:event_txtIdNumberKeyTyped

    private void txtNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyTyped
         
    }//GEN-LAST:event_txtNameKeyTyped

    private void txtPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyTyped
        char vchar = evt.getKeyChar();
            if((!(Character.isDigit(vchar)))
                ||(vchar == KeyEvent.VK_BACK_SPACE)

                || (vchar == KeyEvent.VK_DELETE)){
              evt.consume();
        }
    }//GEN-LAST:event_txtPhoneNoKeyTyped

    private void txtNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyReleased
        txtName.setText(txtName.getText().toUpperCase());
    }//GEN-LAST:event_txtNameKeyReleased

    private void DialogUsersWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogUsersWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_DialogUsersWindowOpened

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        try{
            Manage manage = new Manage();
            
            String sql = "SELECT * FROM userstable WHERE logged = '1' AND id = '"+userid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_exception.setText("Cannot Update User while Logged on. Log Him/Her out first...");
                }else{
                    update();
                    String name = ""+txtName.getText()+"";
                    manage.loggedmessageupdate(name);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+ "button update");
        }finally{
            try{
               rs.close();
               pst.close();
            }catch(Exception e){
                
            }
        }
        
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
             Manage manage = new Manage();
        try{
            String sql = "SELECT * FROM userstable WHERE logged = '1' AND id = '"+userid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_exception.setText("Cannot Delete User while Logged on. Log Him/Her out first...");
                }else{
                    DialogUsers.dispose();
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION){
                        showdialogusers();
                    }else if (response == JOptionPane.YES_OPTION){
                      String query = "UPDATE userstable SET s = '0' WHERE s = '1' AND id = '"+userid+"'";
                      manage.update(query);
                      String name = ""+txtName.getText()+"";
                      manage.loggedmessagedelete(name);
                        
                      lbl_exception.setText("User Deleted Successfully...");
                      showdialogusers();
                      reset();
                    }else if(response == JOptionPane.CLOSED_OPTION){
                        showdialogusers();
                    }       
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+ "button update");
        }finally{
            try{
               rs.close();
               pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_buttonDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogUsers;
    public javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonUpdate;
    public javax.swing.JButton buttonexit;
    private javax.swing.JComboBox<String> comboUsertype;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JTable tableUsers;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtIdNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
