
package offrep;
import com.tolclin.manage.Manage;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class ExpensesPanel extends javax.swing.JPanel {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        private String expenseid;
        public String loggeduserid;
        public int loggedcompanyid;
        Manage manage = new Manage();
        
        Calendar c = Calendar.getInstance();
   
    public ExpensesPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
        loadtable();
    }

    public ArrayList<SearchExpense> ListExpenses(String ValToSearch){
        ArrayList<SearchExpense>viewExpenses = new ArrayList<SearchExpense>();
       
        try{
            String searchQuery = "SELECT * FROM expensestable WHERE CONCAT(expense_id,'',expense_date,'',expense_name,'',mop,'',transactionNo,'',amount) "
                    + "LIKE'%"+ValToSearch+"%' AND s = '1' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(searchQuery);
            rs = pst.executeQuery();
            SearchExpense search;
            while(rs.next()){
                search = new SearchExpense(
                                    rs.getString("expense_id"),
                                    rs.getString("expense_date"),
                                    rs.getString("expense_name"),
                                    rs.getString("mop"),
                                    rs.getString("transactionNo"),
                                    rs.getDouble("amount")
                                            );
                viewExpenses.add(search);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null ,e+" SearchProduct/Findproduct Method");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return viewExpenses;
    }
    public void loadtable(){
        ArrayList<SearchExpense> expenses = ListExpenses(txtSearching.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Expense #","Expense Date","Expense Name","Payment Means","Transaction #","Amount(Kshs.)"});
        
        Object[] row = new Object[6];
        
            for(int i = 0; i < expenses.size(); i ++){
                
             row[0] = expenses.get(i).getExpenseid();
             row[1] = expenses.get(i).getExpensedate();
             row[2] = expenses.get(i).getExpensename();
             row[3] = expenses.get(i).getMop();
             row[4] = expenses.get(i).getTransactionno();
             row[5] = expenses.get(i).getAmount();
             
             model.addRow(row);
            }
            tableExpenses.setModel(model);
    }
  
    private void generateexpenseno(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        expenseid = "EXP-"+dateFormat.format(date.getTime());
    }
    public void reset(){
        c.add(Calendar.YEAR, 0);
        ChooserDate.getDateEditor().setDate(c.getTime());
        txtExpenseName.setText("");
        txtAmount.setText("");
        comboPaymentMeans.setSelectedItem("Select Payment Means");
        txtTransactionNo.setText("");
        txtTransactionNo.setEnabled(false);
        txtExpenseName.requestFocus();
        loadtable();
        expenseid = "";
        generateexpenseno();
        buttonSave.setEnabled(true);
        buttonUpdate.setEnabled(false);
        buttonDelete.setEnabled(false);
    }
    private void save(){
        try{
            String expense = txtExpenseName.getText();
            String amount = txtAmount.getText();
            String means = (String)comboPaymentMeans.getSelectedItem();
            String transactionno = txtTransactionNo.getText();
                if(expenseid.equals("")){
                     lbl_exception.setText("---something went wrong---");
                } else if(expense.equals("")){
                    lbl_exception.setText("Please Enter Expense Name...");
                    txtExpenseName.requestFocus();
                }else if(amount.equals("")){
                    lbl_exception.setText("Please Enter Amount...");
                    txtAmount.requestFocus();
                }else if(means.equals("")){
                    lbl_exception.setText("Please Select Payment Means...");
                }else if(transactionno.equals("")){
                    lbl_exception.setText("Please Enter Transaction #...");
                    txtTransactionNo.requestFocus();
                }else{
                    String sql = "INSERT INTO expensestable(expense_id,expense_date,expense_name,mop,transactionNo,amount,s,company_id)VALUES(?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, expenseid);
                    pst.setString(2, ((JTextField)ChooserDate.getDateEditor().getUiComponent()).getText());
                    pst.setString(3, txtExpenseName.getText());
                    pst.setString(4, (String)comboPaymentMeans.getSelectedItem());
                    pst.setString(5, txtTransactionNo.getText());
                    pst.setString(6, txtAmount.getText());
                    pst.setString(7, "1");
                    pst.setInt(8, loggedcompanyid);
            
                    pst.execute();
                    save_to_payment();
                    lbl_exception.setText("Expense Saved Successfully...");
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
    private void save_to_payment(){
        try{
            
            String sql = "INSERT INTO paymentstable(details,payment_date,debit,credit,expenses_amount,s,user_id,client_id,company_id)VALUES(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, expenseid);
            pst.setString(2, ((JTextField)ChooserDate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, "0");
            pst.setString(4, "0");
            pst.setString(5, txtAmount.getText());
            pst.setString(6, "1");
            pst.setString(7, loggeduserid);
            pst.setString(8, "1");
            pst.setInt(9, loggedcompanyid);
            
            pst.execute();
        }catch(Exception e){
            System.out.println(e+" save_to_payment");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void selectedrow(){
        try{
            buttonSave.setEnabled(false);
            buttonUpdate.setEnabled(true);
            buttonDelete.setEnabled(true);
            
            int row = tableExpenses.getSelectedRow();
            String table_click = tableExpenses.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM expensestable WHERE s = '1' AND expense_id = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    String expensename = rs.getString("expense_name");
                    txtExpenseName.setText(expensename);
                        if(expensename.equals("TOTAL SALARIES")){
                            lbl_exception.setText("Cannot Edit this record here. Please Go to the payroll tab");
                        }else{
                    manage.showdialog(DialogExpenses, jPanel1,830, 310);
                    
                    expenseid = rs.getString("expense_id");
                    ((JTextField)ChooserDate.getDateEditor().getUiComponent()).setText(rs.getString("expense_date"));
                    txtAmount.setText(rs.getString("amount"));
                    comboPaymentMeans.setSelectedItem(rs.getString("mop"));
                    txtTransactionNo.setText(rs.getString("transactionNo"));
                    
                    lbl_exception.setText("");
                    String means = (String)comboPaymentMeans.getSelectedItem();
                        if(means.equals("Cash")){
                            txtTransactionNo.setText("-");
                            txtTransactionNo.setEnabled(false);
                        }else if(means.equals("Mpesa")){
                            txtTransactionNo.setEnabled(true);
                        }else if(means.equals("Cheque")){
                            txtTransactionNo.setEnabled(true);
                        }else{
                            txtTransactionNo.setText("");
                            txtTransactionNo.setEnabled(false);
                        }
                    }
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
    
    private void update(){
        String sql = "UPDATE expensestable SET expense_date = '"+((JTextField)ChooserDate.getDateEditor().getUiComponent()).getText()+"',"
                + "expense_name = '"+txtExpenseName.getText()+"',mop = '"+(String)comboPaymentMeans.getSelectedItem()+"',transactionNo = "
                + "'"+txtTransactionNo.getText()+"',amount = '"+txtAmount.getText()+"' WHERE expense_id = '"+expenseid+"' AND s = '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update(sql);
        String query = "UPDATE paymentstable SET payment_date = '"+((JTextField)ChooserDate.getDateEditor().getUiComponent()).getText()+"',"
                + " expenses_amount = '"+txtAmount.getText()+"' WHERE details = '"+expenseid+"' AND s = '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update(query);
        lbl_exception.setText(""+txtExpenseName.getText()+" Expense Updated Successfully...");
        reset();
    }
    
    private void delete(){
        String sql = "DELETE FROM expensestable WHERE expense_id = '"+expenseid+"' AND s = '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update(sql);
        String query = "DELETE FROM paymentstable WHERE details = '"+expenseid+"' AND s = '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update(query);
        lbl_exception.setText("Expense Deleted Successfully...");
        reset();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogExpenses = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtExpenseName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        comboPaymentMeans = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtTransactionNo = new javax.swing.JTextField();
        ChooserDate = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        buttonUpdate = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        buttonexit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableExpenses = new javax.swing.JTable();
        txtSearching = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lbl_exception = new javax.swing.JLabel();

        DialogExpenses.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogExpenses.setUndecorated(true);
        DialogExpenses.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogExpensesWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        txtExpenseName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExpenseNameActionPerformed(evt);
            }
        });
        txtExpenseName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpenseNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpenseNameKeyReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Expense Name:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Amount:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Date:");

        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAmountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Payment Means:");

        comboPaymentMeans.setBackground(new java.awt.Color(255, 255, 255));
        comboPaymentMeans.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Payment Means", "Cash", "Mpesa", "Cheque" }));
        comboPaymentMeans.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        comboPaymentMeans.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboPaymentMeansPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        comboPaymentMeans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPaymentMeansActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Transaction #:");

        txtTransactionNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTransactionNoKeyPressed(evt);
            }
        });

        ChooserDate.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtExpenseName, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(ChooserDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                            .addComponent(comboPaymentMeans, 0, 222, Short.MAX_VALUE)
                            .addComponent(txtTransactionNo))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChooserDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtExpenseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(comboPaymentMeans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTransactionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reset.png"))); // NOI18N
        buttonReset.setText("Reset");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonExit.setText("Exit");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/update.png"))); // NOI18N
        buttonUpdate.setText("Update");
        buttonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateActionPerformed(evt);
            }
        });

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
                .addGap(51, 51, 51)
                .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(buttonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
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

        javax.swing.GroupLayout DialogExpensesLayout = new javax.swing.GroupLayout(DialogExpenses.getContentPane());
        DialogExpenses.getContentPane().setLayout(DialogExpensesLayout);
        DialogExpensesLayout.setHorizontalGroup(
            DialogExpensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogExpensesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogExpensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogExpensesLayout.setVerticalGroup(
            DialogExpensesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogExpensesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(1001, 516));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/expenses.png"))); // NOI18N
        jLabel1.setText("Expenses");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.setToolTipText("");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/edit.png"))); // NOI18N
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
                .addGap(30, 30, 30)
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
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

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tableExpenses.setBackground(new java.awt.Color(255, 255, 153));
        tableExpenses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableExpenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableExpensesMouseClicked(evt);
            }
        });
        tableExpenses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableExpensesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableExpenses);

        txtSearching.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchingKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchingKeyReleased(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(374, 374, 374)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        manage.showdialog(DialogExpenses, jPanel1,830, 310);
        reset();
        generateexpenseno();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        reset();
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogExpenses.dispose();
        jPanel1.setEnabled(false);
        buttonAdd.setEnabled(true);
        buttonexit.setEnabled(true);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonExitActionPerformed

    private void txtSearchingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchingKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtSearchingKeyPressed

    private void txtSearchingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchingKeyReleased
        loadtable();
    }//GEN-LAST:event_txtSearchingKeyReleased

    private void comboPaymentMeansPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboPaymentMeansPopupMenuWillBecomeInvisible
        lbl_exception.setText("");
        String means = (String)comboPaymentMeans.getSelectedItem();
        if(means.equals("Cash")){
            txtTransactionNo.setText("-");
            txtTransactionNo.setEnabled(false);
        }else if(means.equals("Mpesa")){
            txtTransactionNo.setText("");
            txtTransactionNo.setEnabled(true);
        }else if(means.equals("Cheque")){
            txtTransactionNo.setText("");
            txtTransactionNo.setEnabled(true);
        }else{
            txtTransactionNo.setText("");
            txtTransactionNo.setEnabled(false);
        }
    }//GEN-LAST:event_comboPaymentMeansPopupMenuWillBecomeInvisible

    private void comboPaymentMeansActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPaymentMeansActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPaymentMeansActionPerformed

    private void txtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyTyped
      char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            ||(vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtAmountKeyTyped

    private void txtExpenseNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpenseNameKeyReleased
        txtExpenseName.setText(txtExpenseName.getText().toUpperCase());
    }//GEN-LAST:event_txtExpenseNameKeyReleased

    private void txtExpenseNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpenseNameKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtExpenseNameKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtTransactionNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTransactionNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtTransactionNoKeyPressed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        save();
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void tableExpensesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableExpensesMouseClicked
        
        selectedrow();
    }//GEN-LAST:event_tableExpensesMouseClicked

    private void tableExpensesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableExpensesKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableExpensesKeyReleased

    private void buttonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateActionPerformed
        update();
    }//GEN-LAST:event_buttonUpdateActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        delete();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void DialogExpensesWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogExpensesWindowOpened
    
    }//GEN-LAST:event_DialogExpensesWindowOpened

    private void txtExpenseNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExpenseNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtExpenseNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ChooserDate;
    public javax.swing.JDialog DialogExpenses;
    public javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonUpdate;
    public javax.swing.JButton buttonexit;
    private javax.swing.JComboBox<String> comboPaymentMeans;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JTable tableExpenses;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtExpenseName;
    private javax.swing.JTextField txtSearching;
    private javax.swing.JTextField txtTransactionNo;
    // End of variables declaration//GEN-END:variables
}
