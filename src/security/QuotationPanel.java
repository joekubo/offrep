
package security;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class QuotationPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private int serviceid;
    public String loggeduserid;

    
    Calendar c = Calendar.getInstance();
    public QuotationPanel() {
        initComponents();
        conn = Javaconnect.ConnecrDb();
        c.add(Calendar.YEAR, 0);
        chooserQuotationDate.getDateEditor().setDate(c.getTime());
        removeothercolumns();
        
    }
    public void reset(){
        txtClientName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtPhoneNo.setText("");
        txtEmailAddress.setText("");
        txtClientName.requestFocus();
        txtTotal.setText("0.00");
        chooserQuotationDate.setEnabled(true);
        buttonAddQuotationItems.setEnabled(false);
        txtTotal.setText("");
        lbl_total.setText("");
        generateQuotation_no();
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
    private void cleartablequotationInfo(){
        removeothercolumns();
        tableQuotationDetails.setModel(new DefaultTableModel(null,new String[]{"invoiceno","serviceid","Service Offered","Price","s"}));
    }
    
    //calculate total value of all the invoice services
    private double getSumtotal(){
        int rowscount = tableQuotationDetails.getRowCount();
        double sum_totals = 0;
            for(int i = 0; i < rowscount; i++){
                sum_totals = sum_totals + Double.parseDouble(tableQuotationDetails.getValueAt(i, 3).toString());
            }
        return sum_totals;
    }
    
    
    
   
    private void savequotation(){
        try{
            String sql = "INSERT INTO quotation_table(quotation_no,client_name,address,city,phone_no,email,quotation_date,total,user_id,s)VALUES(?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,txtQuotationNo.getText());
            pst.setString(2, txtClientName.getText());
            pst.setString(3, txtAddress.getText());
            pst.setString(4, txtCity.getText());
            pst.setString(5, txtPhoneNo.getText());
            pst.setString(6, txtEmailAddress.getText());
            pst.setString(7, ((JTextField)chooserQuotationDate.getDateEditor().getUiComponent()).getText());
            pst.setString(8, txtTotal.getText());
            pst.setString(9, loggeduserid);
            pst.setString(10, "1");
            
            pst.execute();
            savequotation_info();
            lbl_exception.setText("Quotation has been Saved Successfully...");
            generateQuotation_no();
            resetInfo();
            reset();
            cleartablequotationInfo();
            DialogQuotations.dispose();
            buttonAddQuotationItems.setEnabled(false);
            chooserQuotationDate.setEnabled(true);
            buttonReset.setEnabled(true);
            buttonExit.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+"savequotation");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void savequotation_info(){
        try{
                  int rows = tableQuotationDetails.getRowCount();
                  conn.setAutoCommit(false);

                  String sql = "INSERT INTO quotation_info(quotation_no,service_id,price,s)VALUES(?,?,?,?)";

                  pst = conn.prepareStatement(sql);
                      for(int row = 0; row<rows; row++){
                          String quotationno = (String)tableQuotationDetails.getValueAt(row, 0);
                          int service_id = (Integer)tableQuotationDetails.getValueAt(row, 1);
                          double price = (Double)tableQuotationDetails.getValueAt(row, 3);
                          String s = (String)tableQuotationDetails.getValueAt(row, 4);

                          pst.setString(1, quotationno);
                          pst.setInt(2, service_id);
                          pst.setDouble(3, price);
                          pst.setString(4, s);

                          pst.addBatch();
                      }
                      pst.executeBatch();
                      conn.commit();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+ "savequotation_info");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    private void loadquotationinfo_transaction(){

        String price = txtPrice.getText();
        price = price.trim();
        double $price = Double.parseDouble(price);
                DefaultTableModel model = (DefaultTableModel)tableQuotationDetails.getModel();
                model.addRow(new Object[]{txtQuotationNo.getText(),
                                          serviceid,
                                          comboService.getSelectedItem().toString(),
                                          $price,
                                          "1"});
                lbl_total.setText(Double.toString(getSumtotal()));
                txtTotal.setText(Double.toString(getSumtotal()));
                resetInfo();
    }
    
    private void removeothercolumns(){
        TableColumn idColumn = tableQuotationDetails.getColumn("invoiceno");
            TableColumn idColumn1 = tableQuotationDetails.getColumn("serviceid");
            TableColumn idColumn2 = tableQuotationDetails.getColumn("s");
            
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
    
    private void filldata_service(){
        comboService.removeAllItems();
        comboService.addItem("Select Service");
        Manage manage = new Manage();
        String sql = "SELECT service_name FROM servicetable WHERE s = '1'";
        String value = "service_name";
        manage.fillcombo(sql, comboService, value);
    }
    
    public void showdialogquotation(){
        DialogQuotations.setVisible(true);
        DialogQuotations.setSize(798, 362);
        DialogQuotations.setAlwaysOnTop(true);
        DialogQuotations.setLocationRelativeTo(this);
        DialogQuotations.setResizable(false);
    }
    
   
    public void generateQuotation_no(){
        try{
             String sql = "SELECT quotation_no FROM quotation_table ORDER BY quotation_no DESC LIMIT 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    int quotationno = rs.getInt("quotation_no");
                    txtQuotationNo.setText(String.format("%d",quotationno+1));
                        
                }else{
                    txtQuotationNo.setText("1");
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" generateQuotation_no");
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

        DialogQuotations = new javax.swing.JDialog();
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
        tableQuotationDetails = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        buttonAddQuotationItems = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        txtClientName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEmailAddress = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        chooserQuotationDate = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        lbl_exception = new javax.swing.JLabel();

        DialogQuotations.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogQuotations.setUndecorated(true);
        DialogQuotations.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogQuotationsWindowOpened(evt);
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

        tableQuotationDetails.setBackground(new java.awt.Color(255, 255, 153));
        tableQuotationDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "invoiceno", "serviceid", "Service Offered", "Price", "s"
            }
        ));
        tableQuotationDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableQuotationDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableQuotationDetails);

        javax.swing.GroupLayout DialogQuotationsLayout = new javax.swing.GroupLayout(DialogQuotations.getContentPane());
        DialogQuotations.getContentPane().setLayout(DialogQuotationsLayout);
        DialogQuotationsLayout.setHorizontalGroup(
            DialogQuotationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogQuotationsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogQuotationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogQuotationsLayout.setVerticalGroup(
            DialogQuotationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogQuotationsLayout.createSequentialGroup()
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
        jLabel9.setText("Quotation #:");

        txtQuotationNo.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtQuotationNo.setForeground(new java.awt.Color(153, 0, 0));
        txtQuotationNo.setText("                   ");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        buttonAddQuotationItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/add.png"))); // NOI18N
        buttonAddQuotationItems.setText("Add Quotation Item");
        buttonAddQuotationItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddQuotationItemsActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Total:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client Name:");

        txtTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtTotal.setText("                        ");

        txtClientName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClientNameActionPerformed(evt);
            }
        });
        txtClientName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClientNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtClientNameKeyReleased(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Phone #:");

        txtPhoneNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneNoActionPerformed(evt);
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
        jLabel6.setText("Email Address:");

        txtEmailAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailAddressActionPerformed(evt);
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

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Address:");

        txtAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddressActionPerformed(evt);
            }
        });
        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("City:");

        txtCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCityActionPerformed(evt);
            }
        });
        txtCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCityKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCityKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(251, 251, 251)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(27, 27, 27)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonAddQuotationItems)
                        .addGap(42, 42, 42)))
                .addContainerGap(286, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonAddQuotationItems)
                .addGap(26, 26, 26))
        );

        chooserQuotationDate.setDateFormatString("yyyy-MM-dd");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Date:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 500, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chooserQuotationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chooserQuotationDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 551, Short.MAX_VALUE)
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

    private void buttonAddQuotationItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddQuotationItemsActionPerformed
        if(txtClientName.getText().equals("")){
            lbl_exception.setText("Please Enter Client Name...");
            txtClientName.requestFocus();
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
        }else{
            showdialogquotation();
            buttonAddQuotationItems.setEnabled(false);
            chooserQuotationDate.setEnabled(false);
            buttonReset.setEnabled(false);
            buttonExit.setEnabled(false);
            add.setEnabled(false);
            resetInfo();            
        }
    }//GEN-LAST:event_buttonAddQuotationItemsActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        generateQuotation_no();
        reset();
        resetInfo();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogQuotations.dispose();
        this.setVisible(false);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonresetActionPerformed
         resetInfo();
    }//GEN-LAST:event_buttonresetActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        DialogQuotations.dispose();
        cleartablequotationInfo();
        buttonAddQuotationItems.setEnabled(false);
        txtClientName.setEnabled(true);
        chooserQuotationDate.setEnabled(true);
        buttonReset.setEnabled(true);
        buttonExit.setEnabled(true);
        removeothercolumns();
        reset();
        resetInfo();
    }//GEN-LAST:event_buttonexitActionPerformed

    private void DialogQuotationsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogQuotationsWindowOpened
        
    }//GEN-LAST:event_DialogQuotationsWindowOpened

    private void buttonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveActionPerformed
        savequotation();
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        loadquotationinfo_transaction();
        buttonsave.setEnabled(true);
    }//GEN-LAST:event_addActionPerformed

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

    private void buttondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteActionPerformed
        //removeothercolumns();
        int row = tableQuotationDetails.getSelectedRow();
        ((DefaultTableModel)tableQuotationDetails.getModel()).removeRow(row);
        lbl_total.setText(Double.toString(getSumtotal()));
        txtTotal.setText(Double.toString(getSumtotal()));
    }//GEN-LAST:event_buttondeleteActionPerformed

    private void txtPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriceFocusGained

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
    
    private void tableQuotationDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQuotationDetailsMouseClicked
        buttonsave.setEnabled(false);
        buttondelete.setEnabled(true);
        int row = tableQuotationDetails.getSelectedRow();
        int table_click = (Integer)tableQuotationDetails.getValueAt(row, 1);
        serviceid = table_click;
    }//GEN-LAST:event_tableQuotationDetailsMouseClicked

    private void txtClientNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClientNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClientNameActionPerformed

    private void txtPhoneNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNoActionPerformed

    private void txtEmailAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailAddressActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCityActionPerformed

    private void txtClientNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClientNameKeyReleased
        txtClientName.setText(txtClientName.getText().toUpperCase());
    }//GEN-LAST:event_txtClientNameKeyReleased

    private void txtClientNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClientNameKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtClientNameKeyPressed

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        txtAddress.setText(txtAddress.getText().toUpperCase());
    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        txtCity.setText(txtCity.getText().toUpperCase());
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtPhoneNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyReleased
        txtPhoneNo.setText(txtPhoneNo.getText().toUpperCase());
    }//GEN-LAST:event_txtPhoneNoKeyReleased

    private void txtPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtPhoneNoKeyPressed

    private void txtEmailAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtEmailAddressKeyPressed

    private void txtEmailAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyReleased
        txtEmailAddress.setText(txtEmailAddress.getText().toLowerCase());
    }//GEN-LAST:event_txtEmailAddressKeyReleased

    private void txtPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
              ||(vchar == KeyEvent.VK_BACK_SPACE)

              || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneNoKeyTyped
     

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogQuotations;
    private javax.swing.JButton add;
    private javax.swing.JButton buttonAddQuotationItems;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttondelete;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton buttonreset;
    private javax.swing.JButton buttonsave;
    private com.toedter.calendar.JDateChooser chooserQuotationDate;
    private javax.swing.JComboBox<String> comboService;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JTable tableQuotationDetails;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JLabel txtQuotationNo;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
