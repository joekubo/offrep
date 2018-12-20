
package offrep;
import com.tolclin.manage.Manage;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class ReportPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private int clientid;
    Calendar c = Calendar.getInstance();
    private String invoiceno;
    private String quotationno;
    public String email;
    public String email_pass;
    private String client_email;
    public String companyname;
    public int loggedcompanyid;
    public String logopath;
    Manage manage = new Manage();
    public ReportPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
        c.add(Calendar.YEAR, 0);
        chooserFrom.getDateEditor().setDate(c.getTime());
        chooserTo.getDateEditor().setDate(c.getTime());
        
    }
    public void reset(){
         c.add(Calendar.YEAR, 0);
        chooserFrom.getDateEditor().setDate(c.getTime());
        chooserTo.getDateEditor().setDate(c.getTime());
        loadClients();
        fill_combo_clients();
        loadPayments();
        clientid = 0;
        lbl_client.setText("");
        total_balance.setText("");
        buttonPrintPayment.setEnabled(false);
        buttonEmail.setEnabled(false);
        loadPayments();
        load_generalreport();
        findInvoice();
        findQuotation();
        invoiceno = "";
        client_email = "";
        buttonDeleteInvoice.setEnabled(false);
        buttonPrintInvoice.setEnabled(false);
        buttonDeleteQuotation.setEnabled(false);
        buttonPrintQuote.setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);
    }
//_____________________________________________________clients______________________________________________________________
    private void loadClients(){
        manage = new Manage();
        String sql = "SELECT clientid AS 'id',name AS 'Client Name',address AS 'Address',city AS 'City',phone_no AS 'Phone #',email AS 'Email Address',"
                + "pin  AS 'PIN' FROM clientstable WHERE s = '1' AND clientid != '1' AND company_id = '"+loggedcompanyid+"'";
        manage.update_table(sql, tableClients);
    }
    //_______________________________________________________________________________________________________________________
    //_____________________________________________________________________________________payments__________________________
    private  void fill_combo_clients(){
        comboClients.removeAllItems();
        comboClients.addItem("Select Client");
        String sql = "SELECT * FROM clientstable WHERE s = '1' AND clientid != '1' AND company_id = '"+loggedcompanyid+"'";
        String value = "name";
        manage.fillcombo(sql, comboClients, value);
    }
    public void loadPayments(){
        String sql = "SELECT id,payment_date AS 'Payment Date',details AS 'Details',debit AS 'Paid Amount' FROM paymentstable WHERE s = '1' AND client_id = '"+clientid+"'"
                + "AND debit > 0 AND company_id = '"+loggedcompanyid+"' AND payment_date BETWEEN '"+((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText()+"' AND"
                + " '"+((JTextField)chooserTo.getDateEditor().getUiComponent()).getText()+"' ";
        manage.update_table(sql, tablePayments);
        try{
            String query = "SELECT COALESCE(SUM(credit),0),COALESCE(SUM(debit),0) FROM paymentstable WHERE s = '1' AND client_id = '"+clientid+"'"
                + "AND payment_date BETWEEN '"+((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText()+"' AND"
                + " '"+((JTextField)chooserTo.getDateEditor().getUiComponent()).getText()+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
                if(rs.next()){
                    total_balance.setText("Balance is "+String.format("%.2f",rs.getDouble("COALESCE(SUM(credit),0)") - rs.getDouble("COALESCE(SUM(debit),0)")));
                }
        }catch(Exception e){
            
        }
    }
    //_______________________________________________________________________________________________________________________
    //______________________________________________________________________________________________________________________________general reports---------------
    private void load_generalreport(){
         String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
         String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
        String sql = "SELECT paymentstable.id AS 'id',paymentstable.payment_date AS 'Date',clientstable.name AS 'Client Name',clientstable.email AS "
                + "'Email Address',paymentstable.details AS 'Details',paymentstable.debit AS 'Debit',paymentstable.credit AS 'Credit',paymentstable.expenses_amount"
                + " AS 'Our Expenses' FROM userstable,clientstable,paymentstable WHERE paymentstable.user_id = userstable.id AND clientstable.clientid = "
                + "paymentstable.client_id AND paymentstable.s = '1' AND paymentstable.company_id = '"+loggedcompanyid+"' AND paymentstable.payment_date BETWEEN "
                + "'"+from+"' AND '"+to+"'";
        manage.update_table(sql, tableGeneralReports);
        
        try{
            String query = "SELECT COALESCE(SUM(credit),0),COALESCE(SUM(debit),0),COALESCE(SUM(expenses_amount),0) FROM paymentstable WHERE s = '1' "
                    + "AND company_id = '"+loggedcompanyid+"' AND payment_date BETWEEN '"+from+"' AND '"+to+"'";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_totalAmount.setText("Total Amount is Kshs "+String.format("%.2f",rs.getDouble("COALESCE(SUM(credit),0)")));
                    lbl_TotalExpenses.setText("Total Expenses is Kshs "+String.format("%.2f",rs.getDouble("COALESCE(SUM(expenses_amount),0)")));
                    lbl_totalDebtors.setText("Total Debtors is Kshs "+String.format("%.2f",(rs.getDouble("COALESCE(SUM(credit),0)") - rs.getDouble("COALESCE(SUM(debit),0)"))));
                    lbl_profits.setText("Totat Profit is Kshs "+String.format("%.2f", rs.getDouble("COALESCE(SUM(credit),0)") - rs.getDouble("COALESCE(SUM(expenses_amount),0)")));
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
    }
    
    //_______________________________________________________________________________________________________________________
    @SuppressWarnings("unchecked")
    //_______________________________________________________________________________________________________________________invoices-----
    public ArrayList<SearchInvoice> ListInvoice(String ValToSearch){
        ArrayList<SearchInvoice>viewInvoice = new ArrayList<SearchInvoice>();
        String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
        String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
        try{
            String searchQuery = "SELECT * FROM invoicetable,clientstable,userstable WHERE invoicetable.user_id = userstable.id AND invoicetable.client_id "
                    + "= clientstable.clientid AND CONCAT(invoicetable.invoice_no,'',clientstable.name,'',invoicetable.invoice_date,userstable.name) LIKE'%"+ValToSearch+"%'"
                    + " AND invoicetable.s = '1' AND invoicetable.company_id = '"+loggedcompanyid+"' AND invoicetable.invoice_date BETWEEN '"+from+"' AND '"+to+"'";
            pst = conn.prepareStatement(searchQuery);
            rs = pst.executeQuery();
            SearchInvoice search;
            while(rs.next()){
                search = new SearchInvoice(
                                    rs.getString("invoicetable.invoice_no"),
                                    rs.getString("clientstable.name"),
                                    rs.getString("invoicetable.invoice_date"),
                                    rs.getDouble("invoicetable.total"),
                                    rs.getString("userstable.name")
                                            );
                viewInvoice.add(search);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null ,e+" SearchInvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return viewInvoice;
    }
    public void findInvoice(){
        ArrayList<SearchInvoice> view= ListInvoice(txtSearch.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Invoice #","Client Name","Invoice Date","Total","Served By"});
        
        Object[] row = new Object[6];
        
            for(int i = 0; i < view.size(); i ++){
                
             row[0] = view.get(i).getInvoiceno();
             row[1] = view.get(i).getClientname();
             row[2] = view.get(i).getInvoicedate();
             row[3] = view.get(i).getTotal();
             row[4] = view.get(i).getServedby();
             
             model.addRow(row);
            }
            tableInvoices.setModel(model);
    }
    private void selectedrow(){
        try{
            int row = tableInvoices.getSelectedRow();
            String table_click = tableInvoices.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM invoicetable WHERE s = '1' AND invoice_no = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    invoiceno = rs.getString("invoice_no");
                    clientid = rs.getInt("invoicetable.client_id");
                    buttonDeleteInvoice.setEnabled(true);
                    buttonPrintInvoice.setEnabled(true);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" selectedrowinvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    //_______________________________________________________________________________________________________________________
//   ____________________________________________________________________________________________________________________________________________________--expense--------
    public void loadExpenses(){
        manage = new Manage();
        String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
        String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
        String sql = "SELECT expense_id AS 'id',expense_name AS 'Expense',mop AS 'Payment Means',transactionNo AS 'Transaction#',amount AS 'Amount' "
                + "FROM expensestable WHERE s = '1' AND company_id = '"+loggedcompanyid+"' AND expense_date BETWEEN '"+from+"' AND '"+to+"'";
        manage.update_table(sql, tableExpenses);
        totalexpenses();
    }
    public void totalexpenses(){
        String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
        String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
        try{
            String sql = "SELECT COALESCE(SUM(amount),0) FROM expensestable WHERE s = '1' AND company_id = '"+loggedcompanyid+"' AND  expense_date BETWEEN '"+from+"' AND '"+to+"'";
            pst = conn.prepareStatement(sql); 
            rs = pst.executeQuery();
                if(rs.next()){
                    double totalamount = rs.getDouble("COALESCE(SUM(amount),0)");
                    total.setText(String.format("%.2f", totalamount));
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" totalexpenses");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    //___________________________________________________________________________________________________________________________________________________________
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClients = new javax.swing.JTable();
        buttonPrintclients = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePayments = new javax.swing.JTable();
        comboClients = new javax.swing.JComboBox<>();
        lbl_client = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        total_balance = new javax.swing.JLabel();
        buttonPrintPayment = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableQuotation = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtSearchquotation = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        buttonDeleteQuotation = new javax.swing.JButton();
        buttonPrintQuote = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        panelinvoice = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableInvoices = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        buttonDeleteInvoice = new javax.swing.JButton();
        buttonPrintInvoice = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableGeneralReports = new javax.swing.JTable();
        buttonPrintGeneral = new javax.swing.JButton();
        lbl_totalAmount = new javax.swing.JLabel();
        lbl_TotalExpenses = new javax.swing.JLabel();
        lbl_totalDebtors = new javax.swing.JLabel();
        lbl_profits = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableExpenses = new javax.swing.JTable();
        total = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        chooserFrom = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chooserTo = new com.toedter.calendar.JDateChooser();
        buttonEmail = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        tableClients.setBackground(new java.awt.Color(255, 255, 204));
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
        jScrollPane1.setViewportView(tableClients);

        buttonPrintclients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintclients.setText("Report");
        buttonPrintclients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintclientsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonPrintclients, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPrintclients, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane1.addTab("All Clients", jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jPanel5.setBackground(new java.awt.Color(255, 255, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

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
        jScrollPane2.setViewportView(tablePayments);

        comboClients.setBackground(new java.awt.Color(255, 255, 255));
        comboClients.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        comboClients.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboClientsPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        lbl_client.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lbl_client.setForeground(new java.awt.Color(153, 153, 153));
        lbl_client.setText("                                   ");

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Clients");

        total_balance.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        total_balance.setForeground(new java.awt.Color(153, 153, 153));
        total_balance.setText("                                   ");

        buttonPrintPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintPayment.setText("Report");
        buttonPrintPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(306, 306, 306)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboClients, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_client, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(total_balance)))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(buttonPrintPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboClients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_client)
                        .addComponent(jLabel4))
                    .addComponent(total_balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPrintPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Payments", jPanel3);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jPanel15.setBackground(new java.awt.Color(255, 255, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        tableQuotation.setBackground(new java.awt.Color(255, 255, 204));
        tableQuotation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableQuotation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableQuotationMouseClicked(evt);
            }
        });
        tableQuotation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableQuotationKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tableQuotation);

        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/search.png"))); // NOI18N
        jLabel7.setText("Search");

        txtSearchquotation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchquotationKeyReleased(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(255, 255, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        buttonDeleteQuotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttonDeleteQuotation.setText("Delete");
        buttonDeleteQuotation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonDeleteQuotationMouseClicked(evt);
            }
        });
        buttonDeleteQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteQuotationActionPerformed(evt);
            }
        });

        buttonPrintQuote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintQuote.setText("Quote");
        buttonPrintQuote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintQuoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonDeleteQuotation, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPrintQuote, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDeleteQuotation, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonPrintQuote, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(344, 344, 344)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearchquotation, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchquotation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quotation", jPanel14);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        panelinvoice.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        tableInvoices.setBackground(new java.awt.Color(255, 255, 204));
        tableInvoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableInvoices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInvoicesMouseClicked(evt);
            }
        });
        tableInvoices.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableInvoicesKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tableInvoices);

        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/search.png"))); // NOI18N
        jLabel6.setText("Search");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(255, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        buttonDeleteInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttonDeleteInvoice.setText("Delete");
        buttonDeleteInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonDeleteInvoiceMouseClicked(evt);
            }
        });
        buttonDeleteInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteInvoiceActionPerformed(evt);
            }
        });

        buttonPrintInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintInvoice.setText("Invoice");
        buttonPrintInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintInvoiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPrintInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDeleteInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonPrintInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(344, 344, 344)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelinvoiceLayout = new javax.swing.GroupLayout(panelinvoice);
        panelinvoice.setLayout(panelinvoiceLayout);
        panelinvoiceLayout.setHorizontalGroup(
            panelinvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelinvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelinvoiceLayout.setVerticalGroup(
            panelinvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelinvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(panelinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Invoices", jPanel7);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel6.setBackground(new java.awt.Color(255, 255, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        tableGeneralReports.setBackground(new java.awt.Color(255, 255, 204));
        tableGeneralReports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tableGeneralReports);

        buttonPrintGeneral.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintGeneral.setText("Report");
        buttonPrintGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintGeneralActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonPrintGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 987, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPrintGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lbl_totalAmount.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        lbl_totalAmount.setForeground(new java.awt.Color(204, 0, 0));
        lbl_totalAmount.setText("          ");

        lbl_TotalExpenses.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        lbl_TotalExpenses.setForeground(new java.awt.Color(204, 0, 0));
        lbl_TotalExpenses.setText("          ");

        lbl_totalDebtors.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        lbl_totalDebtors.setForeground(new java.awt.Color(204, 0, 0));
        lbl_totalDebtors.setText("          ");

        lbl_profits.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        lbl_profits.setForeground(new java.awt.Color(204, 0, 0));
        lbl_profits.setText("          ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbl_totalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_TotalExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_totalDebtors, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_profits, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbl_totalDebtors, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_totalAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_TotalExpenses, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_profits, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General Report", jPanel2);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel12.setBackground(new java.awt.Color(255, 255, 204));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tableExpenses.setBackground(new java.awt.Color(255, 255, 204));
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
        jScrollPane6.setViewportView(tableExpenses);

        total.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        total.setForeground(new java.awt.Color(204, 0, 0));
        total.setText("                 ");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("Total Expenses");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("View Expenses", jPanel11);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1058, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Loading Report", jPanel9);

        chooserFrom.setDateFormatString("yyyy-MM-dd");
        chooserFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                chooserFromPropertyChange(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("From");

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("To");

        chooserTo.setDateFormatString("yyyy-MM-dd");
        chooserTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                chooserToPropertyChange(evt);
            }
        });

        buttonEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/icons8-new-post-30.png"))); // NOI18N
        buttonEmail.setText("Send Email");
        buttonEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1060, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chooserFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chooserTo, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chooserFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chooserTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonPrintclientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintclientsActionPerformed
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
                Manage manage = new Manage();
                String sql = "SELECT * FROM companytable,clientstable WHERE clientstable.s = '1' AND clientstable.clientid > '1' ";
                String path = "Reports/clientsreport.jrxml";
                manage.report(sql,path,jPanel9);
                jTabbedPane1.setSelectedIndex(6);
                return null;
           }
           
       };
       worker.execute();           
    }//GEN-LAST:event_buttonPrintclientsActionPerformed

    private void comboClientsPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboClientsPopupMenuWillBecomeInvisible
       try{
            String sql = "SELECT * FROM clientstable WHERE name = ? ";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, (String)comboClients.getSelectedItem());
            rs = pst.executeQuery();
                if(rs.next()){
                    clientid = rs.getInt("clientid");
                    client_email = rs.getString("email");
                    lbl_client.setText(client_email);
                    loadPayments();
                    buttonPrintPayment.setEnabled(true);
                }else{
                    lbl_client.setText("");
                    total_balance.setText("");
                    clientid = 0;
                    loadPayments();
                    buttonPrintPayment.setEnabled(false);
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
    }//GEN-LAST:event_comboClientsPopupMenuWillBecomeInvisible

    private void chooserFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_chooserFromPropertyChange
        loadPayments();
        load_generalreport();
        findInvoice();
        loadExpenses();
        findQuotation();
    }//GEN-LAST:event_chooserFromPropertyChange

    private void chooserToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_chooserToPropertyChange
        loadPayments();
        load_generalreport();
        findInvoice();
        loadExpenses();
        findQuotation();
    }//GEN-LAST:event_chooserToPropertyChange

    private void buttonPrintPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintPaymentActionPerformed
       SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
               String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
               String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
               int countrows = tablePayments.getRowCount();
                if(countrows < 1){
                    JOptionPane.showMessageDialog(null, "No record displayed to print report. Please Check on the Dates");
                }else{
                    Manage manage = new Manage();
                    String sql = "SELECT companytable.company_name,companytable.location,companytable.address,companytable.city,companytable.phone_no,"
                            + "companytable.email,clientstable.name,clientstable.email AS 'client_email',clientstable.balance,paymentstable.payment_date,"
                            + "paymentstable.details,paymentstable.debit,paymentstable.credit,userstable.name AS 'user_name' FROM companytable,userstable"
                            + ",paymentstable,clientstable WHERE paymentstable.company_id = companytable.id AND userstable.id = paymentstable.user_id AND "
                            + "paymentstable.client_id = clientstable.clientid AND paymentstable.client_id = '"+clientid+"' AND paymentstable.payment_date "
                            + "BETWEEN  '"+from+"' AND '"+to+"' AND paymentstable.s = '1' ";
                    String path = "Reports/payments.jrxml";
                    manage.report_(sql,path,jPanel9,"generated_report.pdf");
                    jTabbedPane1.setSelectedIndex(6);
                    buttonEmail.setEnabled(true);
                }
               
                return null;
           }
           
       };
       worker.execute();       
    }//GEN-LAST:event_buttonPrintPaymentActionPerformed

    private void buttonPrintGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintGeneralActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
               String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
               String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
               int countrows = tableGeneralReports.getRowCount();
                if(countrows < 1){
                    JOptionPane.showMessageDialog(null, "No record displayed to print report. Please Check on the Dates");
                }else{
                    Manage manage = new Manage();
                    String sql = "SELECT companytable.company_name,companytable.location AS 'company_location',companytable.address AS "
                            + "'company_address',companytable.city AS 'company_city',companytable.phone_no AS 'company_phone',companytable.email "
                            + "AS 'companytable.email', paymentstable.id AS 'id',paymentstable.payment_date AS 'Date',clientstable.name AS 'Client Name'"
                            + ",clientstable.email AS 'Email Address',paymentstable.details AS 'Details',paymentstable.debit AS 'Debit',paymentstable.credit AS "
                            + "'Credit',paymentstable.expenses_amount AS 'Our Expenses',userstable.name AS 'served_by' FROM userstable,clientstable,paymentstable,"
                            + "companytable WHERE paymentstable.user_id = userstable.id AND clientstable.clientid = paymentstable.client_id AND paymentstable.company_id "
                            + "= companytable.id AND paymentstable.s = '1' AND paymentstable.company_id = '"+loggedcompanyid+"' AND paymentstable.payment_date BETWEEN "
                            + "'"+from+"' AND '"+to+"'";
                    String path = "Reports/general.jrxml";
                    manage.report(sql, path, jPanel9);
                    jTabbedPane1.setSelectedIndex(6);
                }
               
                return null;
           }
           
       };
       worker.execute();       
    }//GEN-LAST:event_buttonPrintGeneralActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        findInvoice();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void buttonDeleteInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDeleteInvoiceMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonDeleteInvoiceMouseClicked

    public void setbalance_clients(){
        
        try{
            double balance;
            
            String sql = "SELECT COALESCE(SUM(credit), 0),COALESCE(SUM(debit), 0) FROM paymentstable WHERE s = '1' AND company_id = '"+loggedcompanyid+"' "
                    + "AND client_id = '"+clientid+"'";
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
    private void deleteinvoice(){
        String sql = "UPDATE invoicetable SET s = '0' WHERE invoice_no = '"+invoiceno+"' ";
        manage.update(sql);
        String query = "UPDATE invoiceinfo SET s = '0' WHERE invoice_no = '"+invoiceno+"'";
        manage.update(query);
        String sql_pay = "UPDATE paymentstable SET s = '0' WHERE details = '"+invoiceno+"'";
        manage.update(sql_pay);
        setbalance_clients();
        String name = "Invoice # "+invoiceno+"";
        findInvoice();
    }
    private void buttonDeleteInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteInvoiceActionPerformed
        
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                
            }else if (response == JOptionPane.YES_OPTION){
                deleteinvoice();
            }else if(response == JOptionPane.CLOSED_OPTION){
                    
            }       
    }//GEN-LAST:event_buttonDeleteInvoiceActionPerformed
    
    private void buttonPrintInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintInvoiceActionPerformed

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
               
                Manage manage = new Manage();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.phone_no AS 'company_phone',companytable.address AS "
                        + "'company_address',companytable.city AS 'company_city',companytable.email AS 'company_email',companytable.website AS 'company_website'"
                        + ",companytable.company_name,invoicetable.invoice_date,clientstable.name AS 'clients_name',clientstable.balance,userstable.name AS "
                        + "'user_name',invoicetable.invoice_no,invoicetable.total,invoicetable.discount,companytable.image FROM companytable,userstable,invoicetable,"
                        + "clientstable WHERE companytable.id = invoicetable.company_id AND userstable.id = invoicetable.user_id AND invoicetable.client_id = "
                        + "clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+invoiceno+"' AND companytable.image = '"+logopath+"'";
                String path = "Reports/invoice.jrxml";
                manage.report(sql, path, jPanel9);
                jTabbedPane1.setSelectedIndex(6);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintInvoiceActionPerformed

    //_______________________________________________________________________________________________________________________________________---quotation---
    public ArrayList<SearchQuotation> ListQuotation(String ValToSearch){
        ArrayList<SearchQuotation>viewQuotation = new ArrayList<SearchQuotation>();
        String from = ((JTextField)chooserFrom.getDateEditor().getUiComponent()).getText();
        String to = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
        try{
            String searchQuery = "SELECT * FROM quotation_table,userstable WHERE quotation_table.user_id = userstable.id AND CONCAT(quotation_table.quotation_no,'',"
                    + "quotation_table.client_name,'',quotation_table.address,'',quotation_table.city,'',quotation_table.phone_no,'',quotation_table.email,'',"
                    + "quotation_table.quotation_date,'',userstable.name) LIKE'%"+ValToSearch+"%' AND quotation_table.s = '1' AND quotation_table.company_id = "
                    + "'"+loggedcompanyid+"' AND quotation_table.quotation_date BETWEEN '"+from+"' AND '"+to+"'";
            pst = conn.prepareStatement(searchQuery);
            rs = pst.executeQuery();
            SearchQuotation search;
            while(rs.next()){
                search = new SearchQuotation(
                                    rs.getString("quotation_table.quotation_no"),
                                    rs.getString("quotation_table.client_name"),
                                    rs.getString("quotation_table.address"),
                                    rs.getString("quotation_table.city"),
                                    rs.getString("quotation_table.phone_no"),
                                    rs.getString("quotation_table.email"),
                                    rs.getString("quotation_table.quotation_date"),
                                    rs.getDouble("quotation_table.total"),
                                    rs.getString("userstable.name")
                                            );
                viewQuotation.add(search);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null ,e+" SearchQuotation");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return viewQuotation;
    }
    public void findQuotation(){
        ArrayList<SearchQuotation> view= ListQuotation(txtSearchquotation.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Quotation #","Client Name","Address","City","Phone #","Email","Quotation Date","Total","Served By"});
        
        Object[] row = new Object[9];
        
            for(int i = 0; i < view.size(); i ++){
                
             row[0] = view.get(i).getQuotationno();
             row[1] = view.get(i).getClientname();
             row[2] = view.get(i).getAddress();
             row[3] = view.get(i).getCity();
             row[4] = view.get(i).getPhoneno();
             row[5] = view.get(i).getEmail();
             row[6] = view.get(i).getInvoicedate();
             row[7] = view.get(i).getTotal();
             row[8] = view.get(i).getServedby();
             
             model.addRow(row);
            }
            tableQuotation.setModel(model);
    }
    private void deleteQuotation(){
        String sql = "DELETE FROM quotation_table WHERE quotation_no = '"+quotationno+"' ";
        manage.delete(sql);
        String query = "DELETE FROM  quotation_info  WHERE quotation_no = '"+quotationno+"'";
        manage.delete(query);
        setbalance_clients();
        String name = "Quotation # "+quotationno+"";
        findQuotation();
    }
    private void selectedrowquotation(){
        try{
            int row = tableQuotation.getSelectedRow();
            String table_click = tableQuotation.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM quotation_table WHERE s = '1' AND quotation_no = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    quotationno = rs.getString("quotation_no");
                    buttonDeleteQuotation.setEnabled(true);
                    buttonPrintQuote.setEnabled(true);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" selectedrowinvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    //___________________________________________________________________________________________________________________________________________________________
    
    private void tableInvoicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInvoicesMouseClicked
        selectedrow();
    }//GEN-LAST:event_tableInvoicesMouseClicked

    private void tableInvoicesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableInvoicesKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableInvoicesKeyReleased

    private void tableQuotationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQuotationMouseClicked
        selectedrowquotation();
    }//GEN-LAST:event_tableQuotationMouseClicked

    private void tableQuotationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableQuotationKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrowquotation();
        }
    }//GEN-LAST:event_tableQuotationKeyReleased

    private void txtSearchquotationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchquotationKeyReleased
        findQuotation();
    }//GEN-LAST:event_txtSearchquotationKeyReleased

    private void buttonDeleteQuotationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDeleteQuotationMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonDeleteQuotationMouseClicked

    private void buttonDeleteQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteQuotationActionPerformed
         JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                
            }else if (response == JOptionPane.YES_OPTION){
                deleteQuotation();
            }else if(response == JOptionPane.CLOSED_OPTION){
                    
            }       
    }//GEN-LAST:event_buttonDeleteQuotationActionPerformed

    private void buttonPrintQuoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintQuoteActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
               
                Manage manage = new Manage();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.address AS 'company_address',companytable.city AS"
                        + " 'company_city',companytable.phone_no AS 'company_phone',companytable.email AS 'company_email',companytable.website AS 'company_website'"
                        + ",companytable.company_name,companytable.image AS 'company_image',quotation_table.quotation_date,userstable.name AS 'user_name'"
                        + ",quotation_table.quotation_no,quotation_table.client_name,quotation_table.total,companytable.image FROM companytable,userstable,quotation_table WHERE "
                        + "companytable.id = quotation_table.company_id AND userstable.id = quotation_table.user_id AND quotation_table.s = '1' "
                        + "AND quotation_table.quotation_no = '"+quotationno+"' AND companytable.image = '"+logopath+"'";
                String path = "Reports/quotation.jrxml";
                manage.report(sql, path, jPanel9);
                jTabbedPane1.setSelectedIndex(6);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintQuoteActionPerformed

    private void buttonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEmailActionPerformed
        Date invoicedate = chooserTo.getDate();
        if(invoicedate.after(manage.expiry())){
            JOptionPane.showMessageDialog(null,"Date Expired. Renew System...");
        }else{
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
                @Override
                protected Void doInBackground() throws Exception {

                    String date = ((JTextField)chooserTo.getDateEditor().getUiComponent()).getText();
                    String email_to = client_email;
                    String subject = "CLIENT'S STATEMENT from "+companyname+" ON "+date+"";
                    String msg = "Thank you in advance for working with us. God bless you...";
                    String file_name = "Client Statement AS ON "+date;
                    manage.sendemail_attachment(email,email_pass,email_to,subject,msg, file_name);
                    buttonEmail.setEnabled(false);
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_buttonEmailActionPerformed

    private void tablePaymentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePaymentsMouseClicked
       
    }//GEN-LAST:event_tablePaymentsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDeleteInvoice;
    private javax.swing.JButton buttonDeleteQuotation;
    private javax.swing.JButton buttonEmail;
    private javax.swing.JButton buttonPrintGeneral;
    private javax.swing.JButton buttonPrintInvoice;
    private javax.swing.JButton buttonPrintPayment;
    private javax.swing.JButton buttonPrintQuote;
    private javax.swing.JButton buttonPrintclients;
    private com.toedter.calendar.JDateChooser chooserFrom;
    private com.toedter.calendar.JDateChooser chooserTo;
    private javax.swing.JComboBox<String> comboClients;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_TotalExpenses;
    private javax.swing.JLabel lbl_client;
    private javax.swing.JLabel lbl_profits;
    private javax.swing.JLabel lbl_totalAmount;
    private javax.swing.JLabel lbl_totalDebtors;
    private javax.swing.JPanel panelinvoice;
    private javax.swing.JTable tableClients;
    private javax.swing.JTable tableExpenses;
    private javax.swing.JTable tableGeneralReports;
    private javax.swing.JTable tableInvoices;
    private javax.swing.JTable tablePayments;
    private javax.swing.JTable tableQuotation;
    private javax.swing.JLabel total;
    private javax.swing.JLabel total_balance;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearchquotation;
    // End of variables declaration//GEN-END:variables
}
