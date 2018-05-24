
package security;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class PaymentsPanel extends javax.swing.JPanel {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        private int clientid;
        public String loggeduserid;
        
        Calendar c = Calendar.getInstance();
    public PaymentsPanel() {
        initComponents();
        conn = Javaconnect.ConnecrDb();
    }
    
    private void generatePaymentno(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        txtPaymentNo.setText(String.format("%s","PYMT-"+dateFormat.format(date.getTime())));
    }
    
    public void showdialogpayments(){
        DialogPayments.setVisible(true);
        DialogPayments.setSize(812, 259);
        DialogPayments.setAlwaysOnTop(true);
        DialogPayments.setLocationRelativeTo(this);
        DialogPayments.setResizable(false);
    }
    
    public void reset(){
        lblClient.setText("");
        lbl_exception.setText("");
        txtSearching.setText("");
        fillcombo();
        c.add(Calendar.YEAR, 0);
        chooserDate.getDateEditor().setDate(c.getTime());
        comboClient.setSelectedItem("Select Client");
        txtAmount.setText("");
        clientid = 0;
        generatePaymentno();
        loadtable();
        
        buttonSave.setEnabled(true);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
    }
    
    private void save(){
         try{
             if(comboClient.getSelectedItem().toString().equals("Select Client")){
                 lbl_exception.setText("Please Select Client...");
             }else if(txtAmount.getText().equals("")){
                 lbl_exception.setText("Please Enter Amount...");
                 txtAmount.requestFocus();
             }else{
                String sql = "INSERT INTO clients_paytable(payment_id,client_id,amount,s,user_id,payment_date)VALUES(?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                
                pst.setString(1, txtPaymentNo.getText());
                pst.setInt(2, clientid);
                pst.setString(3, txtAmount.getText());
                pst.setString(4, "1");
                pst.setString(5, loggeduserid);
                pst.setString(6, ((JTextField)chooserDate.getDateEditor().getUiComponent()).getText());
                
                pst.execute();
                save_payments();
                setbalance_clients();
                lbl_exception.setText("Payment Saved Successfully...");
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
    public void setbalance_clients(){
        
        try{
            Manage manage = new Manage();
            double balance;
            
            String sql = "SELECT COALESCE(SUM(credit), 0),COALESCE(SUM(debit), 0) FROM paymentstable WHERE s = '1' AND client_id = '"+clientid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    balance = rs.getDouble("COALESCE(SUM(credit), 0)") - rs.getDouble("COALESCE(SUM(debit), 0)");
                    String query = "UPDATE clientstable SET balance = '"+balance+"' WHERE s = '1' AND clientid = '"+clientid+"'";
                    manage.update(query);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" setbalance_clients");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void save_payments(){
        try{
            String sql = "INSERT INTO paymentstable(details,payment_date,debit,credit,expenses_amount,s,user_id,client_id)VALUES(?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtPaymentNo.getText());
            pst.setString(2, ((JTextField)chooserDate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, txtAmount.getText());
            pst.setString(4, "0");
            pst.setString(5, "0");
            pst.setString(6, "1");
            pst.setString(7, loggeduserid);
            pst.setInt(8, clientid);
            
            pst.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" save_payments");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    public ArrayList<SearchPayment> Listpayments(String ValToSearch){
        ArrayList<SearchPayment>viewPayments = new ArrayList<SearchPayment>();
       
        try{
            String searchQuery = "SELECT * FROM clients_paytable,clientstable WHERE clients_paytable.client_id = clientstable.clientid AND "
                    + "CONCAT(clients_paytable.payment_id,'',clients_paytable.payment_date,'',clientstable.name,'',clients_paytable.amount)"
                    + " LIKE'%"+ValToSearch+"%' AND clients_paytable.s = '1'";
            pst = conn.prepareStatement(searchQuery);
            rs = pst.executeQuery();
            SearchPayment search;
            while(rs.next()){
                search = new SearchPayment(
                                    rs.getString("clients_paytable.payment_id"),
                                    rs.getString("clients_paytable.payment_date"),
                                    rs.getString("clientstable.name"),
                                    rs.getDouble("clients_paytable.amount")
                                            );
                viewPayments.add(search);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null ,e+" loadtable");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return viewPayments;
    }
    
    public void loadtable(){
        ArrayList<SearchPayment> pay= Listpayments(txtSearching.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Payment #","Payment Date","Client's Name","Amount Paid"});
        
        Object[] row = new Object[4];
        
            for(int i = 0; i < pay.size(); i ++){
                
             row[0] = pay.get(i).getPaymentid();
             row[1] = pay.get(i).getPay_date();
             row[2] = pay.get(i).getClientname();
             row[3] = pay.get(i).getAmount();
             
             model.addRow(row);
            }
            tablePayments.setModel(model);
    }
    
    private void selectedrow(){
        try{
            showdialogpayments();
                    
            buttonSave.setEnabled(false);
            buttonUpdate.setEnabled(true);
            buttonDelete.setEnabled(true);
            
            int row = tablePayments.getSelectedRow();
            String table_click = tablePayments.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM clients_paytable,clientstable WHERE clients_paytable.client_id = clientstable.clientid AND "
                    + "clients_paytable.payment_id = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    txtPaymentNo.setText(rs.getString("clients_paytable.payment_id"));
                    ((JTextField)chooserDate.getDateEditor().getUiComponent()).setText(rs.getString("clients_paytable.payment_date"));
                    comboClient.setSelectedItem(rs.getString("clientstable.email"));
                    clientid = rs.getInt("clients_paytable.client_id");
                    lblClient.setText(rs.getString("clientstable.name"));
                    txtAmount.setText(rs.getString("amount"));
                    
                    
                }
        }catch(Exception e){
            
        }
    }
    private void update(){
        Manage manage = new Manage();
        String amount = txtAmount.getText();
        if(comboClient.getSelectedItem().toString().equals("Select Client")){
            lbl_exception.setText("Please Select Client...");
        }else if(txtAmount.getText().equals("")){
            lbl_exception.setText("Please Enter Amount...");
                txtAmount.requestFocus();
        }else{
            String sql = "UPDATE clients_paytable SET payment_date = '"+((JTextField)chooserDate.getDateEditor().getUiComponent()).getText()+"',client_id = '"+clientid+"'"
                    + ",amount = '"+amount+"' WHERE payment_id = '"+txtPaymentNo.getText()+"'";
            manage.update(sql);
            String query = "UPDATE paymentstable SET payment_date = '"+((JTextField)chooserDate.getDateEditor().getUiComponent()).getText()+"',debit = '"+amount+"' "
                    + "WHERE details = '"+txtPaymentNo.getText()+"'";
            manage.update(query);
            setbalance_clients();
            String name = "Payment # "+txtPaymentNo.getText()+"";
            manage.loggedmessageupdate(name);
            reset();
            loadtable();
        }
    }
    private void delete(){
        Manage manage = new Manage();
        
        DialogPayments.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION){
            showdialogpayments();
            reset();
        }else if (response == JOptionPane.YES_OPTION){
            String sql = "UPDATE clients_paytable SET s = '0' WHERE payment_id = '"+txtPaymentNo.getText()+"' AND s = '1'";
            manage.update(sql);
            String query = "UPDATE paymentstable SET s = '0' WHERE details = '"+txtPaymentNo.getText()+"' AND s = '1'";
            manage.update(query);
            setbalance_clients();
            String name = "Payment # "+txtPaymentNo.getText()+"";
            manage.loggedmessagedelete(name);
            reset();
            loadtable();
        }else if(response == JOptionPane.CLOSED_OPTION){
            showdialogpayments();
            reset();       
        }      
        
    }
    private void fillcombo(){
        comboClient.removeAllItems();
        comboClient.addItem("Select Client");
        Manage manage = new Manage();
        String sql = "SELECT name FROM clientstable WHERE s = '1' AND clientid != '1'";
        String value = "name";
        manage.fillcombo(sql, comboClient,value);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogPayments = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtAmount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        chooserDate = new com.toedter.calendar.JDateChooser();
        comboClient = new javax.swing.JComboBox<>();
        lblClient = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPaymentNo = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonexit = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonExit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePayments = new javax.swing.JTable();
        txtSearching = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lbl_exception = new javax.swing.JLabel();

        DialogPayments.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogPayments.setUndecorated(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Paid Amount:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Payment Date:");

        chooserDate.setDateFormatString("yyyy-MM-dd");

        comboClient.setBackground(new java.awt.Color(255, 255, 255));
        comboClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        comboClient.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboClientPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        lblClient.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblClient.setForeground(new java.awt.Color(153, 153, 153));
        lblClient.setText("                                ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooserDate, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboClient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(27, 27, 27))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chooserDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblClient, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Payment #:");

        txtPaymentNo.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtPaymentNo.setForeground(new java.awt.Color(153, 0, 0));
        txtPaymentNo.setText("                   ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(233, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaymentNo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPaymentNo))
                .addGap(2, 2, 2)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReset)
                    .addComponent(buttonexit)
                    .addComponent(buttonSave)
                    .addComponent(buttonUpdate)
                    .addComponent(buttonDelete))
                .addContainerGap())
        );

        javax.swing.GroupLayout DialogPaymentsLayout = new javax.swing.GroupLayout(DialogPayments.getContentPane());
        DialogPayments.getContentPane().setLayout(DialogPaymentsLayout);
        DialogPaymentsLayout.setHorizontalGroup(
            DialogPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogPaymentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogPaymentsLayout.setVerticalGroup(
            DialogPaymentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogPaymentsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(1057, 524));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/payments.png"))); // NOI18N
        jLabel1.setText("Payments");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/edit.png"))); // NOI18N
        buttonAdd.setText("Add/Edit");
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 759, Short.MAX_VALUE)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonExit)
                    .addComponent(buttonAdd))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tablePayments.setBackground(new java.awt.Color(255, 255, 204));
        tablePayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablePayments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePaymentsMouseClicked(evt);
            }
        });
        tablePayments.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablePaymentsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tablePayments);

        txtSearching.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchingKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchingKeyReleased(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1015, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(397, 397, 397)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_exception.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
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
                        .addGap(290, 290, 290)
                        .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogPayments.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        showdialogpayments();
        reset();
        buttonAdd.setEnabled(false);
        buttonExit.setEnabled(false);
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        reset();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        DialogPayments.dispose();
        buttonAdd.setEnabled(true);
        buttonExit.setEnabled(true);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
        char vchar = evt.getKeyChar();
        if ((!(Character.isDigit(vchar)))
                || (vchar == KeyEvent.VK_BACK_SPACE)
                || (vchar == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAmountKeyTyped

    private void comboClientPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboClientPopupMenuWillBecomeInvisible
        try{
            lbl_exception.setText("");
            String sql = "SELECT * FROM clientstable WHERE name = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String)comboClient.getSelectedItem());
            rs = pst.executeQuery();
                if(rs.next()){
                    lblClient.setText(rs.getString("email"));
                    clientid = rs.getInt("clientid");
                }else{
                    lblClient.setText("");
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" comboClient");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_comboClientPopupMenuWillBecomeInvisible

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        save();
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void txtSearchingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchingKeyReleased
        loadtable();
    }//GEN-LAST:event_txtSearchingKeyReleased

    private void tablePaymentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePaymentsMouseClicked
        selectedrow();
    }//GEN-LAST:event_tablePaymentsMouseClicked

    private void tablePaymentsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablePaymentsKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tablePaymentsKeyReleased

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        update();
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        delete();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void txtSearchingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchingKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtSearchingKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAmountKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogPayments;
    public javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    public javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonUpdate;
    private javax.swing.JButton buttonexit;
    private com.toedter.calendar.JDateChooser chooserDate;
    private javax.swing.JComboBox<String> comboClient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClient;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JTable tablePayments;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JLabel txtPaymentNo;
    private javax.swing.JTextField txtSearching;
    // End of variables declaration//GEN-END:variables
}
