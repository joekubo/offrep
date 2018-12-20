
package offrep;

import com.tolclin.manage.Manage;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ClientsPanel extends javax.swing.JPanel {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String clientid;
    public int loggedcompanyid;
    
    Manage manage = new Manage();
    public ClientsPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
    }

    public void reset(){
        txtclientName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtPhoneNo.setText("");
        txtEmailAddress.setText("");
        txtPin.setText("");
        txtclientName.requestFocus();
        
        buttonSave.setEnabled(true);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
        
        loadtable();
    }
    private void loadtable(){
        String sql = "SELECT clientid AS 'id',name AS 'Client Name',address AS 'Address',city AS 'City',phone_no AS 'Phone #',email AS 'Email Address'"
                + ",pin AS 'PIN #' FROM clientstable WHERE s = '1' AND clientid != '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update_table(sql, tableClients);
    }
    private void selectedrow(){
        try{
            manage.showdialog(DialogClients, jPanel1, 812, 264);
            buttonSave.setEnabled(false);
            buttonUpdate.setEnabled(true);
            buttonDelete.setEnabled(true);
            
            int row = tableClients.getSelectedRow();
            String table_click = tableClients.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM clientstable WHERE s = '1' AND clientid = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    clientid = rs.getString("Clientid");
                    txtclientName.setText(rs.getString("name"));
                    txtAddress.setText(rs.getString("address"));
                    txtCity.setText(rs.getString("city"));
                    txtPhoneNo.setText(rs.getString("phone_no"));
                    txtEmailAddress.setText(rs.getString("email"));
                    txtPin.setText(rs.getString("pin"));
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
            if(txtclientName.getText().equals("")){
                lbl_exception.setText("Please Enter Client Name...");
                txtclientName.requestFocus();
            }else if(txtAddress.getText().equals("")){
                lbl_exception.setText("Please Enter Address...");
                txtAddress.requestFocus();
            }else if(txtCity.getText().equals("")){
                lbl_exception.setText("Please Enter City...");
                txtCity.requestFocus();
            }else if(txtPhoneNo.getText().equals("")){
                lbl_exception.setText("Please Enter Phone #...");
                txtPhoneNo.requestFocus();
            }else if(txtEmailAddress.getText().equals("")){
                lbl_exception.setText("Please Enter Email Address...");
                txtEmailAddress.requestFocus();
            }else if(txtPin.getText().equals("")){
                lbl_exception.setText("Please Enter PIN #...");
                txtPin.requestFocus();
            }else{
                String sql = "INSERT INTO clientstable(name,address,city,phone_no,email,pin,balance,s,company_id)VALUES(?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                
                pst.setString(1, txtclientName.getText());
                pst.setString(2, txtAddress.getText());
                pst.setString(3, txtCity.getText());
                pst.setString(4, txtPhoneNo.getText());
                pst.setString(5, txtEmailAddress.getText());
                pst.setString(6, txtPin.getText());
                pst.setString(7, "0");
                pst.setString(8, "1");
                pst.setInt(9, loggedcompanyid);
                
                pst.execute();
                lbl_exception.setText("Client Saved Successfully...");
                reset();
                loadtable();
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
        String sql = "UPDATE clientstable SET name = '"+txtclientName.getText()+"',address = '"+txtAddress.getText()+"',city = '"+txtCity.getText()+"',phone_no"
                + "= '"+txtPhoneNo.getText()+"',email = '"+txtEmailAddress.getText()+"',pin = '"+txtPin.getText()+"' WHERE clientid = '"+clientid+"' "
                + "AND company_id = '"+loggedcompanyid+"'";
        manage.update(sql);
        String name = "Client "+txtclientName.getText()+"";
        lbl_exception.setText("Client Updated Successfully...");
        reset();
        loadtable();
    }
    private void delete(){
        DialogClients.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION){
            manage.showdialog(DialogClients, jPanel1, 812, 264);
        }else if (response == JOptionPane.YES_OPTION){
            manage.showdialog(DialogClients, jPanel1, 812, 264);
            String sql = "UPDATE clientstable SET s = '0' WHERE s = '1' AND clientid = '"+clientid+"' AND company_id = '"+loggedcompanyid+"'";
            manage.update(sql);
            lbl_exception.setText("Client Deleted Successfully...");
            reset();
            loadtable();
        }else if(response == JOptionPane.CLOSED_OPTION){
           manage.showdialog(DialogClients, jPanel1, 812, 264);
        }      
       
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogClients = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        txtEmailAddress = new javax.swing.JTextField();
        txtPin = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtCity = new javax.swing.JTextField();
        txtclientName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lbl_exception = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        buttonexit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClients = new javax.swing.JTable();

        DialogClients.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogClients.setUndecorated(true);
        DialogClients.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogClientsWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(214, 193, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jPanel4.setBackground(new java.awt.Color(214, 193, 240));

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("PIN #:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Email Address:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Phone #:");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(txtEmailAddress)
                    .addComponent(txtPin))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(214, 193, 240));

        txtCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCityKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCityKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCityKeyReleased(evt);
            }
        });

        txtclientName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtclientNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtclientNameKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client Name:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Address:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("City:");

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
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
                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtclientName, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtclientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonReset.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reset.png"))); // NOI18N
        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonExit.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        buttonSave.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonUpdate.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonUpdate.setText("Update");
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateActionPerformed(evt);
            }
        });

        buttonDelete.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
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

        javax.swing.GroupLayout DialogClientsLayout = new javax.swing.GroupLayout(DialogClients.getContentPane());
        DialogClients.getContentPane().setLayout(DialogClientsLayout);
        DialogClientsLayout.setHorizontalGroup(
            DialogClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogClientsLayout.setVerticalGroup(
            DialogClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(214, 193, 240));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/client.png"))); // NOI18N
        jLabel1.setText("Clients");

        lbl_exception.setFont(new java.awt.Font("SansSerif", 3, 14)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonexit.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.setToolTipText("");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonAdd.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/add.png"))); // NOI18N
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

        jPanel2.setBackground(new java.awt.Color(214, 193, 240));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tableClients.setBackground(new java.awt.Color(255, 255, 153));
        tableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClientsMouseClicked(evt);
            }
        });
        tableClients.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableClientsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableClients);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 941, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(191, 191, 191))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        manage.showdialog(DialogClients, jPanel1, 812, 264);
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
        reset();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void tableClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClientsMouseClicked
        selectedrow();
        //showdialogclients();
        buttonAdd.setEnabled(false);
        buttonexit.setEnabled(false);
    }//GEN-LAST:event_tableClientsMouseClicked

    private void tableClientsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableClientsKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableClientsKeyReleased

    private void txtPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneNoKeyTyped

    private void txtPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPhoneNoKeyPressed

    private void txtPhoneNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyReleased
        txtPhoneNo.setText(txtPhoneNo.getText().toUpperCase());
    }//GEN-LAST:event_txtPhoneNoKeyReleased

    private void txtEmailAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmailAddressKeyPressed

    private void txtEmailAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyReleased
        txtEmailAddress.setText(txtEmailAddress.getText().toLowerCase());
    }//GEN-LAST:event_txtEmailAddressKeyReleased

    private void txtPinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPinKeyPressed

    private void txtPinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinKeyReleased
        txtPin.setText(txtPin.getText().toUpperCase());
    }//GEN-LAST:event_txtPinKeyReleased

    private void txtCityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyTyped
        char vchar = evt.getKeyChar();
        if(((Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtCityKeyTyped

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        txtCity.setText(txtCity.getText().toUpperCase());
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtclientNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtclientNameKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtclientNameKeyPressed

    private void txtclientNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtclientNameKeyReleased
        txtclientName.setText(txtclientName.getText().toUpperCase());
    }//GEN-LAST:event_txtclientNameKeyReleased

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        txtAddress.setText(txtAddress.getText().toUpperCase());
    }//GEN-LAST:event_txtAddressKeyReleased

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        reset();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogClients.dispose();
        jPanel1.setEnabled(false);
        buttonAdd.setEnabled(true);
        buttonexit.setEnabled(true);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        String sql = "SELECT name FROM clientstable WHERE s = '1' AND name = '"+txtclientName.getText()+"'";
            if(manage.checking(sql) == 1){
                lbl_exception.setText("Name already Exists...");
                txtclientName.setText("");
                txtclientName.requestFocus();
            }else{
                save();
            }
        
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed

        if(txtclientName.getText().equals("")){
            lbl_exception.setText("Please Enter Client Name...");
            txtclientName.requestFocus();
        }else if(txtAddress.getText().equals("")){
            lbl_exception.setText("Please Enter Address...");
            txtAddress.requestFocus();
        }else if(txtCity.getText().equals("")){
            lbl_exception.setText("Please Enter City...");
            txtCity.requestFocus();
        }else if(txtPhoneNo.getText().equals("")){
            lbl_exception.setText("Please Enter Phone #...");
            txtPhoneNo.requestFocus();
        }else if(txtEmailAddress.getText().equals("")){
            lbl_exception.setText("Please Enter Email Address...");
            txtEmailAddress.requestFocus();
        }else if(txtPin.getText().equals("")){
            lbl_exception.setText("Please Enter PIN #...");
            txtPin.requestFocus();
        }else{
            update();
        }

    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        delete();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void DialogClientsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogClientsWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_DialogClientsWindowOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogClients;
    public javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonUpdate;
    public javax.swing.JButton buttonexit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JTable tableClients;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPin;
    private javax.swing.JTextField txtclientName;
    // End of variables declaration//GEN-END:variables
}
