
package security;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class InvoicesPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private int clientid;
    private int serviceid;
    public String loggeduserid;
    private int invoiceinfo_id;

    
    Calendar c = Calendar.getInstance();
    public InvoicesPanel() {
        initComponents();
        conn = Javaconnect.ConnecrDb();
        c.add(Calendar.YEAR, 0);
        chooserInvoiceDate.getDateEditor().setDate(c.getTime());
        removeothercolumns();
        
    }
    public void resetInfo(){
        filldata_service();
        comboService.setEnabled(true);
        comboService.setSelectedItem("Select Service");
        txtPrice.setText("");
        comboService.requestFocus();
        buttonsave.setEnabled(false);
        buttondelete.setEnabled(false);
        add.setEnabled(false);
        serviceid = 0;
    }
    public void reset(){
        txtTotal.setText("0.00");
        filldatacombo();
        clientid = 0;
        chooserInvoiceDate.setEnabled(true);
        comboBoxClient.setEnabled(true);
        buttonAddInvoiceItems.setEnabled(false);
        txtTotal.setText("");
        lblClient.setText("");
        lbl_total.setText("");
        generateInvoiceno();
    }

    private void cleartableinvoiceInfo(){
        removeothercolumns();
        tableInvoiceDetails.setModel(new DefaultTableModel(null,new String[]{"invoiceno","serviceid","Service Offered","Price","s"}));
    }
    
    //calculate total value of all the invoice services
    private double getSumtotal(){
        int rowscount = tableInvoiceDetails.getRowCount();
        double sum_totals = 0;
            for(int i = 0; i < rowscount; i++){
                sum_totals = sum_totals + Double.parseDouble(tableInvoiceDetails.getValueAt(i, 3).toString());
            }
        return sum_totals;
    }
    
    
    
   
    private void saveinvoice(){
        try{
            String sql = "INSERT INTO invoicetable(invoice_no,client_id,invoice_date,total,user_id,s)VALUES(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,txtInvoiceno.getText());
            pst.setInt(2, clientid);
            pst.setString(3, ((JTextField)chooserInvoiceDate.getDateEditor().getUiComponent()).getText());
            pst.setString(4, txtTotal.getText());
            pst.setString(5, loggeduserid);
            pst.setString(6, "1");
            
            pst.execute();
            save_payments();
            saveinvoice_info();
            setbalance_clients();
            lbl_exception.setText("Invoice has been Saved Successfully...");
            generateInvoiceno();
            resetInfo();
            reset();
            cleartableinvoiceInfo();
            DialogInvoices.dispose();
            buttonAddInvoiceItems.setEnabled(false);
            comboBoxClient.setEnabled(true);
            chooserInvoiceDate.setEnabled(true);
            buttonReset.setEnabled(true);
            buttonExit.setEnabled(true);
            
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+"saveinvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void saveinvoice_info(){
        try{
                  int rows = tableInvoiceDetails.getRowCount();
                  conn.setAutoCommit(false);

                  String sql = "INSERT INTO invoiceinfo(invoice_no,service_id,price,s)VALUES(?,?,?,?)";

                  pst = conn.prepareStatement(sql);
                      for(int row = 0; row<rows; row++){
                          String invoiceno = (String)tableInvoiceDetails.getValueAt(row, 0);
                          int service_id = (Integer)tableInvoiceDetails.getValueAt(row, 1);
                          double price = (Double)tableInvoiceDetails.getValueAt(row, 3);
                          String s = (String)tableInvoiceDetails.getValueAt(row, 4);

                          pst.setString(1, invoiceno);
                          pst.setInt(2, service_id);
                          pst.setDouble(3, price);
                          pst.setString(4, s);

                          pst.addBatch();
                      }
                      pst.executeBatch();
                      conn.commit();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+ "saveinvoice_info");
        }
    }
  
    private void deleteinvoice(){
        Manage manage = new Manage();
        String sql = "UPDATE invoicetable SET s = '0' WHERE invoice_no = '"+txtInvoiceno.getText()+"' ";
        manage.update(sql);
        String query = "UPDATE invoiceinfo SET s = '0' WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
        manage.update(query);
        String sql_pay = "UPDATE paymentstable SET s = '0' WHERE details = '"+txtInvoiceno.getText()+"'";
        manage.update(sql_pay);
        setbalance_clients();
        String name = "Invoice # "+txtInvoiceno.getText()+"";
        manage.loggedmessagedelete(name);
    }

    
    private void loadinvoiceinfo_transaction(){

        String price = txtPrice.getText();
        price = price.trim();
        double $price = Double.parseDouble(price);
                DefaultTableModel model = (DefaultTableModel)tableInvoiceDetails.getModel();
                model.addRow(new Object[]{txtInvoiceno.getText(),
                                          serviceid,
                                          comboService.getSelectedItem().toString(),
                                          $price,
                                          "1"});
                lbl_total.setText(Double.toString(getSumtotal()));
                txtTotal.setText(Double.toString(getSumtotal()));
                resetInfo();
    }
    private void save_payments(){
        try{
            String sql = "INSERT INTO paymentstable(details,payment_date,debit,credit,s,user_id,expenses_amount,client_id)VALUES(?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtInvoiceno.getText());
            pst.setString(2, ((JTextField)chooserInvoiceDate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, "0");
            pst.setString(4, txtTotal.getText());
            pst.setString(5, "1");
            pst.setString(6, loggeduserid);
            pst.setString(7, "0");
            pst.setInt(8, clientid);
            
            pst.execute();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+"save_payments");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    private void removeothercolumns(){
        TableColumn idColumn = tableInvoiceDetails.getColumn("invoiceno");
            TableColumn idColumn1 = tableInvoiceDetails.getColumn("serviceid");
            TableColumn idColumn2 = tableInvoiceDetails.getColumn("s");
            
            idColumn.setMaxWidth(0);
            idColumn1.setMaxWidth(0);
            idColumn2.setMaxWidth(0);
            
            idColumn.setMinWidth(0);
            idColumn1.setMinWidth(0);
            idColumn2.setMinWidth(0);
            
            idColumn.setPreferredWidth(0);
            idColumn1.setPreferredWidth(0);
            idColumn2.setPreferredWidth(0);
    }
    
    private void filldatacombo(){
        comboBoxClient.removeAllItems();
        comboBoxClient.addItem("Select Client");
        Manage manage = new Manage();
        String sql = "SELECT name FROM clientstable WHERE s = '1' AND clientid != '1'";
        String value = "name";
        manage.fillcombo(sql, comboBoxClient,value);
    }
    
    private void filldata_service(){
        comboService.removeAllItems();
        comboService.addItem("Select Service");
        Manage manage = new Manage();
        String sql = "SELECT service_name FROM servicetable WHERE s = '1'";
        String value = "service_name";
        manage.fillcombo(sql, comboService, value);
    }
    
    public void showdialoginvoice(){
        DialogInvoices.setVisible(true);
        DialogInvoices.setSize(798, 362);
        DialogInvoices.setAlwaysOnTop(true);
        DialogInvoices.setLocationRelativeTo(this);
        DialogInvoices.setResizable(false);
    }
    
   
    public void generateInvoiceno(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        txtInvoiceno.setText(String.format("%s","INV-"+dateFormat.format(date.getTime())));
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogInvoices = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        comboService = new javax.swing.JComboBox<>();
        lbl_total = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        buttonreset = new javax.swing.JButton();
        buttonexit = new javax.swing.JButton();
        buttonsave = new javax.swing.JButton();
        buttondelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableInvoiceDetails = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtInvoiceno = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buttonAddInvoiceItems = new javax.swing.JButton();
        lblClient = new javax.swing.JLabel();
        chooserInvoiceDate = new com.toedter.calendar.JDateChooser();
        comboBoxClient = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        lbl_exception = new javax.swing.JLabel();

        DialogInvoices.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogInvoices.setUndecorated(true);
        DialogInvoices.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogInvoicesWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        txtPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPriceFocusGained(evt);
            }
        });
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceKeyPressed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("Service:");

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("Price:");

        comboService.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboServicePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboService, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(comboService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        lbl_total.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lbl_total.setText("              ");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("Total:");

        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/add.png"))); // NOI18N
        add.setText("Add Items");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonreset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonreset.setText("Reset");
        buttonreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonresetActionPerformed(evt);
            }
        });

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonsave.setText("Save");
        buttonsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonsaveActionPerformed(evt);
            }
        });

        buttondelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
        buttondelete.setText("Delete");
        buttondelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonreset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonsave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(buttondelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonreset)
                    .addComponent(buttonexit)
                    .addComponent(buttonsave)
                    .addComponent(buttondelete))
                .addContainerGap())
        );

        tableInvoiceDetails.setBackground(new java.awt.Color(255, 255, 153));
        tableInvoiceDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "invoiceno", "serviceid", "Service Offered", "Price", "s"
            }
        ));
        tableInvoiceDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInvoiceDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableInvoiceDetails);

        javax.swing.GroupLayout DialogInvoicesLayout = new javax.swing.GroupLayout(DialogInvoices.getContentPane());
        DialogInvoices.getContentPane().setLayout(DialogInvoicesLayout);
        DialogInvoicesLayout.setHorizontalGroup(
            DialogInvoicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogInvoicesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogInvoicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogInvoicesLayout.setVerticalGroup(
            DialogInvoicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogInvoicesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(1043, 605));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/invoice.png"))); // NOI18N
        jLabel1.setText("Invoices");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReset)
                    .addComponent(buttonExit))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Invoice #:");

        txtInvoiceno.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtInvoiceno.setForeground(new java.awt.Color(153, 0, 0));
        txtInvoiceno.setText("                   ");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Date:");

        buttonAddInvoiceItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/add.png"))); // NOI18N
        buttonAddInvoiceItems.setText("Add Invoice Items");
        buttonAddInvoiceItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddInvoiceItemsActionPerformed(evt);
            }
        });

        lblClient.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblClient.setForeground(new java.awt.Color(153, 153, 153));
        lblClient.setText("                                ");

        chooserInvoiceDate.setDateFormatString("yyyy-MM-dd");

        comboBoxClient.setBackground(new java.awt.Color(255, 255, 255));
        comboBoxClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        comboBoxClient.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboBoxClientPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Total:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client:");

        txtTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtTotal.setText("                        ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonAddInvoiceItems)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxClient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chooserInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chooserInvoiceDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblClient, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonAddInvoiceItems)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInvoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 332, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(107, 107, 107)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(276, 276, 276)
                        .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddInvoiceItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddInvoiceItemsActionPerformed
        showdialoginvoice();
        buttonAddInvoiceItems.setEnabled(false);
        comboBoxClient.setEnabled(false);
        chooserInvoiceDate.setEnabled(false);
        buttonReset.setEnabled(false);
        buttonExit.setEnabled(false);
        add.setEnabled(false);
        resetInfo();
        
    }//GEN-LAST:event_buttonAddInvoiceItemsActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        generateInvoiceno();
        reset();
        resetInfo();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogInvoices.dispose();
        this.setVisible(false);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonresetActionPerformed
         resetInfo();
    }//GEN-LAST:event_buttonresetActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        DialogInvoices.dispose();
        cleartableinvoiceInfo();
        buttonAddInvoiceItems.setEnabled(false);
        comboBoxClient.setEnabled(true);
        chooserInvoiceDate.setEnabled(true);
        buttonReset.setEnabled(true);
        buttonExit.setEnabled(true);
        removeothercolumns();
        reset();
        resetInfo();
    }//GEN-LAST:event_buttonexitActionPerformed

    private void comboBoxClientPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboBoxClientPopupMenuWillBecomeInvisible
        try{
            generateInvoiceno();
            String sql = "SELECT * FROM clientstable WHERE name = ? AND s = '1'";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, comboBoxClient.getSelectedItem().toString());
            rs = pst.executeQuery();
                if(rs.next()){
                    clientid = rs.getInt("clientid");
                    lblClient.setText(rs.getString("email"));
                     buttonAddInvoiceItems.setEnabled(true);
                     lbl_exception.setText("");
                }else{
                    //clientid = 0;
                    lblClient.setText("");
                    buttonAddInvoiceItems.setEnabled(false);
                    lbl_exception.setText("");
                }
                
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_comboBoxClientPopupMenuWillBecomeInvisible

    private void DialogInvoicesWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogInvoicesWindowOpened
        
    }//GEN-LAST:event_DialogInvoicesWindowOpened

    private void buttonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveActionPerformed
        saveinvoice();
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        loadinvoiceinfo_transaction();
        buttonsave.setEnabled(true);
    }//GEN-LAST:event_addActionPerformed

    private void buttondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteActionPerformed
        //removeothercolumns();
        int row = tableInvoiceDetails.getSelectedRow();
        ((DefaultTableModel)tableInvoiceDetails.getModel()).removeRow(row);
        lbl_total.setText(Double.toString(getSumtotal()));
        txtTotal.setText(Double.toString(getSumtotal()));
    }//GEN-LAST:event_buttondeleteActionPerformed
    
    private void tableInvoiceDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInvoiceDetailsMouseClicked
        buttonsave.setEnabled(false);
        buttondelete.setEnabled(true);
        int row = tableInvoiceDetails.getSelectedRow();
        int table_click = (Integer)tableInvoiceDetails.getValueAt(row, 1);
        serviceid = table_click;
    }//GEN-LAST:event_tableInvoiceDetailsMouseClicked

    private void txtPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyPressed
        if(comboService.getSelectedItem().toString().equals("Select Service")){
            add.setEnabled(false);
        }else if(txtPrice.getText().equals("")){
            add.setEnabled(false);
        }else{
            add.setEnabled(true);
        }
    }//GEN-LAST:event_txtPriceKeyPressed

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtPriceKeyTyped

    private void txtPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceFocusGained

    private void comboServicePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboServicePopupMenuWillBecomeInvisible
        try{
            String sql = "SELECT * FROM servicetable WHERE service_name = ? ";
            pst = conn.prepareStatement(sql);

            pst.setString(1, (String)comboService.getSelectedItem());
            rs = pst.executeQuery();
            if(rs.next()){
                serviceid = rs.getInt("id");
                if(!txtPrice.getText().equals("")){
                    add.setEnabled(true);

                }else{
                    add.setEnabled(false);
                }
                txtPrice.requestFocus();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){

            }
        }
    }//GEN-LAST:event_comboServicePopupMenuWillBecomeInvisible
 
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogInvoices;
    private javax.swing.JButton add;
    private javax.swing.JButton buttonAddInvoiceItems;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttondelete;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton buttonreset;
    private javax.swing.JButton buttonsave;
    private com.toedter.calendar.JDateChooser chooserInvoiceDate;
    private javax.swing.JComboBox<String> comboBoxClient;
    private javax.swing.JComboBox<String> comboService;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblClient;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JTable tableInvoiceDetails;
    private javax.swing.JLabel txtInvoiceno;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
