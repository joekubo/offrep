
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
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class InvoicesPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private int info_id;
    private int clientid;
    public String loggeduserid;
    public String companyname;
    public int loggedcompanyid;
    public String email;
    public String email_pass;
    private String invoice_no;
    private int invoiceinfo_id;
    private String product;
    public String logopath;
    Manage manage = new Manage();
    
    Calendar c = Calendar.getInstance();
    public InvoicesPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
        c.add(Calendar.YEAR, 0);
        chooserInvoiceDate.getDateEditor().setDate(c.getTime());
        removeothercolumns();
        
    }
    public void resetInfo(){
        txtProduct.setText("");
        txtQty.setText("");
        txtUnitprice.setText("");
        txtTotalprice.setText("");
        txtProduct.requestFocus();
        buttonsave.setEnabled(false);
        buttondelete.setEnabled(false);
        buttonPrint.setEnabled(false);
        buttonPrintVat.setEnabled(false);
        buttonPrintedit.setEnabled(false);
        buttonPrintVatedit.setEnabled(false);
        add.setEnabled(false);
        product = "";
        findInvoice_edit();
        txtSearching.setText("");
        txtSearching.requestFocus();
    }
    private void calc(){
        String qty = txtQty.getText();
        qty = qty.trim();
        int quantity = Integer.parseInt(qty);
        
        String unitprice = txtUnitprice.getText();
        unitprice = unitprice.trim();
        double unit_price = Double.parseDouble(unitprice);
        
            if(quantity == 0 || unit_price == 0){
                txtTotalprice.setText("0.00");
            }else{
                txtTotalprice.setText(String.format("%.2f", quantity * unit_price));
            }
        
    }
    public void reset(){
        txtTotal.setText("0.00");
        txtDiscount.setText("0.00");
        filldatacombo();
        clientid = 0;
        comboBoxClient.setEnabled(true);
        buttonAddInvoiceItems.setEnabled(false);
        buttonPrint.setEnabled(false);
        buttonPrintVat.setEnabled(false);
        buttonEmail.setEnabled(false);
        buttondeleteinvoice.setEnabled(false);
        txtTotal.setText("");
        lblClient.setText("");
        lbl_total.setText("");
        lbl_exception.setText("");
        generateInvoiceno();
        findInvoice();
        generateInvoiceno();
        jTabbedPane2.setSelectedIndex(0);
    }

    private void cleartableinvoiceInfo(){
        removeothercolumns();
        tableInvoiceDetails.setModel(new DefaultTableModel(null,new String[]{"invoiceno","Product","Qty","Unit Price","Total Price","s","company_id"}));
    }
    
    //calculate total value of all the invoice services
    private double getSumtotal(){
        int rowscount = tableInvoiceDetails.getRowCount();
        double sum_totals = 0;
            for(int i = 0; i < rowscount; i++){
                sum_totals = sum_totals + Double.parseDouble(tableInvoiceDetails.getValueAt(i, 4).toString());
            }
        return sum_totals;
    }
    
    private void saveinvoice(){
        try{
            String sql = "INSERT INTO invoicetable(invoice_no,client_id,invoice_date,total,user_id,s,discount,company_id)VALUES(?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,txtInvoiceno.getText());
            pst.setInt(2, clientid);
            pst.setString(3, ((JTextField)chooserInvoiceDate.getDateEditor().getUiComponent()).getText());
            pst.setString(4, lbl_total.getText());
            pst.setString(5, loggeduserid);
            pst.setString(6, "1");
            pst.setString(7, txtDiscount.getText());
            pst.setInt(8, loggedcompanyid);
            
            pst.execute();
            save_payments();
            saveinvoice_info();
            setbalance_clients();
            DialogInvoices.dispose();
            buttonAddInvoiceItems.setEnabled(false);
            comboBoxClient.setEnabled(true);
            buttonReset.setEnabled(true);
            buttonExit.setEnabled(true);
            resetInfo();
            reset();
            lbl_exception.setText("Invoice has been Saved Successfully...");
            cleartableinvoiceInfo();
            findInvoice();
            
            
        }catch(Exception e){
            String query_invoice = "DELETE FROM invoicetable WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_invoice);
            String query_invoice_line = "DELETE FROM invoiceinfo WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_invoice_line);
            String query_payments = "DELETE FROM paymentstable WHERE details = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_payments);
            setbalance_clients();
            System.out.println(e+" saveinvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void delete_nill(){
        String sql = "DELETE FROM invoicetable WHERE  total = '0'";
        manage.delete(sql);
        String query = "DELETE FROM paymentstable WHERE credit = '0' AND debit = '0'";
        manage.delete(query);
    }
    private void saveinvoice_info(){
        try{
                  int rows = tableInvoiceDetails.getRowCount();
                  String sql = "INSERT INTO invoiceinfo(invoice_no,product,qty,unit_price,price,s,company_id)VALUES(?,?,?,?,?,?,?)";
                  pst = conn.prepareStatement(sql);
                      for(int row = 0; row<rows; row++){
                          String invoiceno = (String)tableInvoiceDetails.getValueAt(row, 0);
                          String product_ = (String)tableInvoiceDetails.getValueAt(row, 1);
                          String qty = (String)tableInvoiceDetails.getValueAt(row, 2);
                          String unit_price = (String)tableInvoiceDetails.getValueAt(row, 3);
                          double price = (Double)tableInvoiceDetails.getValueAt(row, 4);
                          String s = (String)tableInvoiceDetails.getValueAt(row, 5);
                          int companyid = (Integer)tableInvoiceDetails.getValueAt(row, 6);

                          pst.setString(1, invoiceno);
                          pst.setString(2, product_);
                          pst.setString(3, qty);
                          pst.setString(4, unit_price);
                          pst.setDouble(5, price);
                          pst.setString(6, s);
                          pst.setInt(7, companyid);

                          pst.execute();
                      }
        }catch(Exception e){
            String query_invoice = "DELETE FROM invoicetable WHERE invoice_no = '"+txtInvoiceno.getText()+"' AND company_id = '"+loggedcompanyid+"'";
            manage.delete(query_invoice);
            String query_invoice_line = "DELETE FROM invoiceinfo WHERE invoice_no = '"+txtInvoiceno.getText()+"' AND company_id = '"+loggedcompanyid+"'";
            manage.delete(query_invoice_line);
            String query_payments = "DELETE FROM paymentstable WHERE details = '"+txtInvoiceno.getText()+"' AND company_id = '"+loggedcompanyid+"'";
            manage.delete(query_payments);
            System.out.println(e+ "saveinvoice_info");
            
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }

    private void delete_invoiceLines(){
        String total_ = txtTotal_edit.getText();
        total_ = total_.trim();
        double total = Double.parseDouble(total_);
        
        String sql = "DELETE FROM invoiceinfo WHERE id = '"+info_id+"'";
        manage.delete(sql);
        String query_invoicetable = "UPDATE invoicetable SET total = total - '"+total+"' WHERE invoice_no = '"+lbl_invoiceno.getText()+"' "
                + "AND company_id = '"+loggedcompanyid+"'";
        manage.delete(query_invoicetable);
        String query_payments = "UPDATE paymentstable SET credit = credit - '"+total+"' WHERE details = '"+lbl_invoiceno.getText()+"' AND company_id = '"+loggedcompanyid+"'";
        manage.delete(query_payments);
        setbalance_clients();
        reset_edit();
        lbl_exception.setText("Deleted "+txtProduct_edit.getText()+" Deleted Successfully...");
    }
    private void loadinvoiceinfo_transaction(){

        String price = txtTotalprice.getText();
        price = price.trim();
        double $price = Double.parseDouble(price);
                DefaultTableModel model = (DefaultTableModel)tableInvoiceDetails.getModel();
                model.addRow(new Object[]{txtInvoiceno.getText(),
                                          txtProduct.getText(),
                                          txtQty.getText(),
                                          txtUnitprice.getText(),
                                          $price,
                                          "1",
                                           loggedcompanyid});
                lbl_total.setText(Double.toString(getSumtotal()));
                resetInfo();
                findInvoice();
    }

    private void save_payments(){
        try{
            String sql = "INSERT INTO paymentstable(details,payment_date,debit,credit,s,user_id,expenses_amount,client_id,company_id)VALUES(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtInvoiceno.getText());
            pst.setString(2, ((JTextField)chooserInvoiceDate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, "0");
            pst.setString(4, lbl_total.getText());
            pst.setString(5, "1");
            pst.setString(6, loggeduserid);
            pst.setString(7, "0");
            pst.setInt(8, clientid);
            pst.setInt(9, loggedcompanyid);
            
            pst.execute();
            
        }catch(Exception e){
            String query_invoice = "DELETE FROM invoicetable WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_invoice);
            String query_invoice_line = "DELETE FROM invoiceinfo WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_invoice_line);
            String query_payments = "DELETE FROM paymentstable WHERE details = '"+txtInvoiceno.getText()+"'";
            manage.delete(query_payments);
            System.out.println(e+" save_payments");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void reset_edit(){
        product = "";
        txtProduct_edit.setText("");
        txtQty_edit.setText("");
        txtUnit_edit.setText("");
        txtTotal_edit.setText("0.00");
        buttonsave_edit.setEnabled(true);
        buttondelete_edit.setEnabled(false);
        delete_nill();
        load_editinvoice();
    }
    private void selectedrow(){
        try{
            
            int row = tableInvoices_edit.getSelectedRow();
            String table_click = tableInvoices_edit.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM invoicetable WHERE  s = '1' AND invoice_no = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    clientid = rs.getInt("client_id");
                    lbl_invoiceno.setText(rs.getString("invoice_no"));
                    double discount = rs.getDouble("discount");
                    
                        if(discount > 0){
                            lbl_exception.setText("Cannot Edit this Invoice, but can be Deleted...");
                        }else{
                            manage.showdialog(DialogEditInvoice,jPanel1, 676, 399);
                            reset_edit();
                            jTabbedPane2.setEnabled(false);
                        }
                }
        }catch(Exception e){
            System.out.println(e+" selectedrow_editinvoice");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    
    private void selectedroweditinfo(){
         try{
            int row = tableEdit.getSelectedRow();
            String table_click = tableEdit.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM invoiceinfo WHERE  s = '1' AND id = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    buttondelete_edit.setEnabled(true);
                    buttonsave_edit.setEnabled(false);
                    info_id = rs.getInt("id");
                    txtProduct_edit.setText(rs.getString("product"));
                    product = rs.getString("product");
                    txtQty_edit.setText(rs.getString("qty"));
                    txtUnit_edit.setText(rs.getString("unit_price"));
                    txtTotal_edit.setText(rs.getString("price"));
                }
        }catch(Exception e){
            System.out.println(e+" selectedroweditinfo");
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
            TableColumn idColumn1 = tableInvoiceDetails.getColumn("s");
            TableColumn idColumn2 = tableInvoiceDetails.getColumn("company_id");
            
            
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
        String sql = "SELECT name FROM clientstable WHERE s = '1' AND clientid != '1'";
        String value = "name";
        manage.fillcombo(sql, comboBoxClient,value);
    }
    
    public void generateInvoiceno(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        txtInvoiceno.setText(String.format("%s","INV-"+dateFormat.format(date.getTime())));
    }
    
    public void setbalance_clients(){
        
        try{
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
    public ArrayList<SearchInvoice> ListInvoice(String ValToSearch){
        ArrayList<SearchInvoice>viewInvoice = new ArrayList<SearchInvoice>();
        try{
            String searchQuery = "SELECT * FROM invoicetable,clientstable,userstable WHERE invoicetable.user_id = userstable.id AND invoicetable.client_id "
                    + "= clientstable.clientid AND CONCAT(invoicetable.invoice_no,'',clientstable.name,'',invoicetable.invoice_date,userstable.name) LIKE'%"+ValToSearch+"%'"
                    + " AND invoicetable.s = '1' AND clientstable.balance > '0' AND invoicetable.company_id = '"+loggedcompanyid+"'";
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
            System.out.println(e+" SearchInvoice");
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
        model.setColumnIdentifiers(new Object[]{"Invoice #","Client Name","Invoice Date","Total"});
        
        Object[] row = new Object[4];
        
            for(int i = 0; i < view.size(); i ++){
                
             row[0] = view.get(i).getInvoiceno();
             row[1] = view.get(i).getClientname();
             row[2] = view.get(i).getInvoicedate();
             row[3] = view.get(i).getTotal();
             
             model.addRow(row);
            }
            tableInvoices.setModel(model);
    }
    public ArrayList<SearchInvoice> ListInvoice2(String ValToSearch){
        ArrayList<SearchInvoice>viewInvoice = new ArrayList<SearchInvoice>();
        try{
            String searchQuery = "SELECT * FROM invoicetable,clientstable,userstable WHERE invoicetable.user_id = userstable.id AND invoicetable.client_id "
                    + "= clientstable.clientid AND CONCAT(invoicetable.invoice_no,'',clientstable.name,'',invoicetable.invoice_date,userstable.name) LIKE'%"+ValToSearch+"%'"
                    + " AND invoicetable.s = '1'";
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
            System.out.println(e+" SearchInvoice2");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
        return viewInvoice;
    }
    public void findInvoice_edit(){
        ArrayList<SearchInvoice> view= ListInvoice2(txtSearching.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Invoice #","Client Name","Invoice Date","Total"});
        
        Object[] row = new Object[4];
        
            for(int i = 0; i < view.size(); i ++){
                
             row[0] = view.get(i).getInvoiceno();
             row[1] = view.get(i).getClientname();
             row[2] = view.get(i).getInvoicedate();
             row[3] = view.get(i).getTotal();
             
             model.addRow(row);
            }
            tableInvoices_edit.setModel(model);
    }
    private void load_editinvoice(){
        String sql = "SELECT id,product AS 'Product',qty AS 'Qty',unit_price AS 'Unitprice',price AS 'Total Price' FROM invoiceinfo WHERE s = '1' AND"
                + " invoice_no = '"+lbl_invoiceno.getText()+"' AND company_id = '"+loggedcompanyid+"'";
        manage.update_table(sql, tableEdit);
    }
    private void save_invoiceinfo(){
        try{
            String product_ = txtProduct_edit.getText();
            String total_ = txtTotal_edit.getText();
            total_ = total_.trim();
            double $total = Double.parseDouble(total_);
            
            
            String sql = "INSERT INTO invoiceinfo(invoice_no,product,qty,unit_price,price,s,company_id) VALUES (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, lbl_invoiceno.getText());
            pst.setString(2, product_);
            pst.setString(3, txtQty_edit.getText());
            pst.setString(4, txtUnit_edit.getText());
            pst.setString(5, txtTotal_edit.getText());
            pst.setString(6, "1");
            pst.setInt(7, loggedcompanyid);
            
            pst.execute();
            
            String query_invoice = "UPDATE invoicetable SET total = total + '"+$total+"' WHERE s = '1' AND invoice_no = '"+lbl_invoiceno.getText()+"'";
            manage.update(query_invoice);
            String query_payment = "UPDATE paymentstable SET credit = credit + '"+$total+"' WHERE s = '1' AND details = '"+lbl_invoiceno.getText()+"'";
            manage.update(query_payment);
            setbalance_clients();
            lbl_exception.setText("Product "+txtProduct_edit.getText()+" Added to invoice # "+lbl_invoiceno.getText()+" Successfully...");
            reset_edit();
            
        }catch(Exception e){
            System.out.println(e+" save_invoiceinfo");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void calc_(){
        String qty_ = txtQty_edit.getText();
        qty_ = qty_.trim();
        int qty = Integer.parseInt(qty_);
        
        String unit_ = txtUnit_edit.getText();
        unit_ = unit_.trim();
        double unitprice = Double.parseDouble(unit_);
        
        
        txtTotal_edit.setText(String.format("%.2f", qty * unitprice ));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogInvoices = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtUnitprice = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtProduct = new javax.swing.JTextArea();
        lbl_total = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtTotalprice = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        buttonreset = new javax.swing.JButton();
        buttonexit = new javax.swing.JButton();
        buttonsave = new javax.swing.JButton();
        buttondelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableInvoiceDetails = new javax.swing.JTable();
        DialogEditInvoice = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lbl_invoiceno = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtProduct_edit = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtUnit_edit = new javax.swing.JTextField();
        txtTotal_edit = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtQty_edit = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableEdit = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        buttonreset_edit = new javax.swing.JButton();
        buttonexit_edit = new javax.swing.JButton();
        buttonsave_edit = new javax.swing.JButton();
        buttondelete_edit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtInvoiceno = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        comboBoxClient = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        chooserInvoiceDate = new com.toedter.calendar.JDateChooser();
        lblClient = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonAddInvoiceItems = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        labelserch = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableInvoices = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonPrint = new javax.swing.JButton();
        buttondeleteinvoice = new javax.swing.JButton();
        buttonPrintVat = new javax.swing.JButton();
        paneledit = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableInvoices_edit = new javax.swing.JTable();
        labelserch1 = new javax.swing.JLabel();
        txtSearching = new javax.swing.JTextField();
        buttonPrintedit = new javax.swing.JButton();
        buttonPrintVatedit = new javax.swing.JButton();
        panelReport = new javax.swing.JPanel();
        buttonEmail = new javax.swing.JButton();
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

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("Product:");

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("Qty:");

        jLabel14.setForeground(new java.awt.Color(0, 0, 153));
        jLabel14.setText("Unit Price:");

        txtUnitprice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUnitpriceFocusGained(evt);
            }
        });
        txtUnitprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUnitpriceKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUnitpriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitpriceKeyReleased(evt);
            }
        });

        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
        });

        txtProduct.setColumns(20);
        txtProduct.setRows(5);
        txtProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(txtProduct);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(txtQty)
                    .addComponent(txtUnitprice))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtUnitprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lbl_total.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lbl_total.setText("              ");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("Total:");

        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/add.png"))); // NOI18N
        add.setText("Add Items");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("Total Price:");

        txtTotalprice.setEditable(false);
        txtTotalprice.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalprice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTotalpriceFocusGained(evt);
            }
        });
        txtTotalprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalpriceKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalpriceKeyPressed(evt);
            }
        });

        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountFocusGained(evt);
            }
        });
        txtDiscount.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtDiscountInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDiscountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(0, 0, 153));
        jLabel19.setText("Discount:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalprice, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 222, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtTotalprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonreset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reset.png"))); // NOI18N
        buttonreset.setText("Reset");
        buttonreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonresetActionPerformed(evt);
            }
        });

        buttonexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonexit.setText("Exit");
        buttonexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitActionPerformed(evt);
            }
        });

        buttonsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonsave.setText("Save");
        buttonsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonsaveActionPerformed(evt);
            }
        });

        buttondelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
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
                .addGap(127, 127, 127)
                .addComponent(buttonsave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addComponent(buttondelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
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
                "invoiceno", "Product", "Qty", "Unit Price", "Total Price", "s", "company_id"
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
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        DialogEditInvoice.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogEditInvoice.setUndecorated(true);

        jPanel10.setBackground(new java.awt.Color(214, 193, 240));

        jPanel11.setBackground(new java.awt.Color(214, 193, 240));

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Product:");

        lbl_invoiceno.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lbl_invoiceno.setForeground(new java.awt.Color(153, 0, 0));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Invoice #:");

        txtProduct_edit.setColumns(20);
        txtProduct_edit.setRows(5);
        txtProduct_edit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProduct_editKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(txtProduct_edit);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6)
                    .addComponent(lbl_invoiceno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_invoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel12.setBackground(new java.awt.Color(214, 193, 240));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 153));
        jLabel10.setText("Unit Price:");

        txtUnit_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnit_editActionPerformed(evt);
            }
        });
        txtUnit_edit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUnit_editKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnit_editKeyReleased(evt);
            }
        });

        txtTotal_edit.setEditable(false);
        txtTotal_edit.setBackground(new java.awt.Color(204, 204, 204));
        txtTotal_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotal_editActionPerformed(evt);
            }
        });
        txtTotal_edit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotal_editKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 153));
        jLabel17.setText("Total Price:");

        txtQty_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQty_editActionPerformed(evt);
            }
        });
        txtQty_edit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQty_editKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQty_editKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("Qty:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUnit_edit)
                            .addComponent(txtTotal_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQty_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtQty_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtUnit_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtTotal_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tableEdit.setBackground(new java.awt.Color(255, 255, 204));
        tableEdit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEditMouseClicked(evt);
            }
        });
        tableEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableEditKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tableEdit);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonreset_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reset.png"))); // NOI18N
        buttonreset_edit.setText("Reset");
        buttonreset_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonreset_editActionPerformed(evt);
            }
        });

        buttonexit_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonexit_edit.setText("Exit");
        buttonexit_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexit_editActionPerformed(evt);
            }
        });

        buttonsave_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonsave_edit.setText("Save");
        buttonsave_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonsave_editActionPerformed(evt);
            }
        });

        buttondelete_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttondelete_edit.setText("Delete");
        buttondelete_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondelete_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(buttonreset_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(buttonsave_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttondelete_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(buttonexit_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonreset_edit)
                    .addComponent(buttonexit_edit)
                    .addComponent(buttonsave_edit)
                    .addComponent(buttondelete_edit))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DialogEditInvoiceLayout = new javax.swing.GroupLayout(DialogEditInvoice.getContentPane());
        DialogEditInvoice.getContentPane().setLayout(DialogEditInvoiceLayout);
        DialogEditInvoiceLayout.setHorizontalGroup(
            DialogEditInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEditInvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogEditInvoiceLayout.setVerticalGroup(
            DialogEditInvoiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEditInvoiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(214, 193, 240));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setPreferredSize(new java.awt.Dimension(1043, 605));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/invoice.png"))); // NOI18N
        jLabel1.setText("Invoices");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jTabbedPane2.setBackground(new java.awt.Color(214, 193, 240));
        jTabbedPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));
        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel3.setBackground(new java.awt.Color(214, 193, 240));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Invoice #:");

        txtInvoiceno.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtInvoiceno.setForeground(new java.awt.Color(153, 0, 0));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jPanel9.setBackground(new java.awt.Color(214, 193, 240));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

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

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Date:");

        txtTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtTotal.setText("                        ");

        chooserInvoiceDate.setDateFormatString("yyyy-MM-dd");
        chooserInvoiceDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                chooserInvoiceDatePropertyChange(evt);
            }
        });

        lblClient.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblClient.setForeground(new java.awt.Color(153, 153, 153));
        lblClient.setText("                                ");

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client:");

        buttonAddInvoiceItems.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/add.png"))); // NOI18N
        buttonAddInvoiceItems.setText("Add Invoice Items");
        buttonAddInvoiceItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddInvoiceItemsActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Total:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buttonAddInvoiceItems)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxClient, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chooserInvoiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chooserInvoiceDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonAddInvoiceItems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblClient, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(214, 193, 240));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        labelserch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/search.png"))); // NOI18N

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
        jScrollPane1.setViewportView(tableInvoices);

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(258, 258, 258)
                .addComponent(labelserch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(254, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelserch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        buttonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrint.setText("Print Invoice");
        buttonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintActionPerformed(evt);
            }
        });

        buttondeleteinvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttondeleteinvoice.setText("Delete Invoice");
        buttondeleteinvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondeleteinvoiceActionPerformed(evt);
            }
        });

        buttonPrintVat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintVat.setText("Print Invoice (VAT)");
        buttonPrintVat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintVatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttondeleteinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonPrintVat, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    .addComponent(buttonPrint)
                    .addComponent(buttondeleteinvoice)
                    .addComponent(buttonPrintVat))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInvoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 738, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceno, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Create Invoices", jPanel2);

        paneledit.setBackground(new java.awt.Color(214, 193, 240));
        paneledit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));

        tableInvoices_edit.setBackground(new java.awt.Color(255, 255, 204));
        tableInvoices_edit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableInvoices_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInvoices_editMouseClicked(evt);
            }
        });
        tableInvoices_edit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableInvoices_editKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tableInvoices_edit);

        labelserch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/search.png"))); // NOI18N

        txtSearching.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchingKeyReleased(evt);
            }
        });

        buttonPrintedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintedit.setText("Print Invoice");
        buttonPrintedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrinteditActionPerformed(evt);
            }
        });

        buttonPrintVatedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrintVatedit.setText("Print Invoice (VAT)");
        buttonPrintVatedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintVateditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneleditLayout = new javax.swing.GroupLayout(paneledit);
        paneledit.setLayout(paneleditLayout);
        paneleditLayout.setHorizontalGroup(
            paneleditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneleditLayout.createSequentialGroup()
                .addGap(389, 389, 389)
                .addComponent(labelserch1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(402, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneleditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneleditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneleditLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonPrintedit, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonPrintVatedit, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        paneleditLayout.setVerticalGroup(
            paneleditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneleditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneleditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearching, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelserch1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneleditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonPrintedit)
                    .addComponent(buttonPrintVatedit))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Edit Invoices", paneledit);

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1013, Short.MAX_VALUE)
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Loading Report", panelReport);

        buttonEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/icons8-new-post-30.png"))); // NOI18N
        buttonEmail.setText("Send Email");
        buttonEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_exception.setFont(new java.awt.Font("SansSerif", 3, 12)); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddInvoiceItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddInvoiceItemsActionPerformed
        
        manage.showdialog(DialogInvoices, jPanel1,869, 496);
        buttonAddInvoiceItems.setEnabled(false);
        comboBoxClient.setEnabled(false);
        buttonReset.setEnabled(false);
        buttonExit.setEnabled(false);
        add.setEnabled(false);
        resetInfo();
        buttonsave.setEnabled(false);
        jTabbedPane2.setEnabled(false);
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
        buttonReset.setEnabled(true);
        buttonExit.setEnabled(true);
        jTabbedPane2.setEnabled(true);
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
       Date invoicedate = chooserInvoiceDate.getDate();
        if(invoicedate.after(manage.expiry())){
            lbl_exception.setText("Date Expired. Renew System...");
        }else{
            saveinvoice();
            DialogInvoices.dispose();
            cleartableinvoiceInfo();
            buttonAddInvoiceItems.setEnabled(false);
            comboBoxClient.setEnabled(true);
            buttonReset.setEnabled(true);
            buttonExit.setEnabled(true);
            jTabbedPane2.setEnabled(true);
            removeothercolumns();
            reset();
            resetInfo();
        }
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        if(txtProduct.getText().equals("")){
            lbl_exception.setText("Please Enter Product...");
            txtProduct.requestFocus();
        }else if(txtQty.getText().equals("")){
            lbl_exception.setText("Please Enter Qty...");
            txtQty.requestFocus();
        }else if(txtUnitprice.getText().equals("")){
            lbl_exception.setText("Please Enter Unit Price...");
            txtUnitprice.requestFocus();
        }else{
            loadinvoiceinfo_transaction();
            buttonsave.setEnabled(true);
        }
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
        buttonPrint.setEnabled(true);
        buttonPrintVat.setEnabled(true);
        int row = tableInvoiceDetails.getSelectedRow();
        int table_click = (Integer)tableInvoiceDetails.getValueAt(row, 1);
        //serviceid = table_click;
    }//GEN-LAST:event_tableInvoiceDetailsMouseClicked

    private void txtTotalpriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalpriceKeyPressed
        
    }//GEN-LAST:event_txtTotalpriceKeyPressed

    private void txtTotalpriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalpriceKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtTotalpriceKeyTyped

    private void txtTotalpriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalpriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalpriceFocusGained

    private void txtUnitpriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnitpriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitpriceFocusGained

    private void txtUnitpriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtUnitpriceKeyTyped

    private void txtUnitpriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceKeyPressed
      
            add.setEnabled(true);
        
    }//GEN-LAST:event_txtUnitpriceKeyPressed

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void txtQtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyPressed
        add.setEnabled(true);
    }//GEN-LAST:event_txtQtyKeyPressed

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        if(txtQty.getText().equals("")){
            txtTotalprice.setText("0.00");
        }else{
            calc();
        }
    }//GEN-LAST:event_txtQtyKeyReleased

    private void txtUnitpriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceKeyReleased
        if(txtUnitprice.getText().equals("")){
            txtTotalprice.setText("0.00");
        }else{
            calc();
        }
    }//GEN-LAST:event_txtUnitpriceKeyReleased

    private void chooserInvoiceDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_chooserInvoiceDatePropertyChange
        findInvoice();
    }//GEN-LAST:event_chooserInvoiceDatePropertyChange

    private String email_to;
    private String clientname;
    private void tableInvoicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInvoicesMouseClicked
     
        try{
            lbl_exception.setText("");
            int row = tableInvoices.getSelectedRow();
            String table_click = tableInvoices.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM invoicetable,userstable,companytable,clientstable WHERE invoicetable.user_id = userstable.id AND invoicetable.company_id = "
                    + "companytable.id AND invoicetable.client_id = clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){  
                    txtInvoiceno.setText(rs.getString("invoicetable.invoice_no"));
                    clientid = rs.getInt("invoicetable.client_id");
                    email_to = rs.getString("clientstable.email");
                    clientname = rs.getString("clientstable.name");
                    buttonPrint.setEnabled(true);
                    buttonPrintVat.setEnabled(true);
                    buttondeleteinvoice.setEnabled(true);
                    
                }
        }catch(Exception e){
            System.out.println(e+" tableInvoicesMouseClicked");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_tableInvoicesMouseClicked

    private void tableInvoicesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableInvoicesKeyReleased
    
    }//GEN-LAST:event_tableInvoicesKeyReleased

    private void buttonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
               String invoiceno = txtInvoiceno.getText();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.phone_no AS 'company_phone',companytable.address AS "
                        + "'company_address',companytable.city AS 'company_city',companytable.email AS 'company_email',companytable.website AS 'company_website'"
                        + ",companytable.company_name,invoicetable.invoice_date,clientstable.name AS 'clients_name',clientstable.balance,userstable.name AS "
                        + "'user_name',invoicetable.invoice_no,invoicetable.total,invoicetable.discount,companytable.image FROM companytable,userstable,invoicetable,"
                        + "clientstable WHERE companytable.id = invoicetable.company_id AND userstable.id = invoicetable.user_id AND invoicetable.client_id = "
                        + "clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+invoiceno+"' AND companytable.image = '"+logopath+"'";
                String path = "Reports/invoice.jrxml";
                manage.report_(sql,path,panelReport,"generated_report.pdf");
                jTabbedPane2.setSelectedIndex(2);
                buttonEmail.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        findInvoice();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tableInvoices_editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInvoices_editMouseClicked
        selectedrow();
    }//GEN-LAST:event_tableInvoices_editMouseClicked

    private void tableInvoices_editKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableInvoices_editKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tableInvoices_editKeyReleased

    private void txtSearchingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchingKeyReleased
        findInvoice_edit();
    }//GEN-LAST:event_txtSearchingKeyReleased

    private void txtQty_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQty_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQty_editActionPerformed

    private void txtUnit_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnit_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnit_editActionPerformed

    private void txtTotal_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_editActionPerformed

    private void buttonreset_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonreset_editActionPerformed
        reset_edit();
    }//GEN-LAST:event_buttonreset_editActionPerformed

    private void buttonexit_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexit_editActionPerformed
        DialogEditInvoice.dispose();
        buttonReset.setEnabled(true);
        buttonPrintedit.setEnabled(true);
        buttonPrintVatedit.setEnabled(true);
        jTabbedPane2.setEnabled(true);
        removeothercolumns();
        reset();
        resetInfo();
        buttonPrintedit.setEnabled(true);
        buttonPrintVatedit.setEnabled(true);

    }//GEN-LAST:event_buttonexit_editActionPerformed

    private void buttonsave_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsave_editActionPerformed
        
        try{
            if(txtProduct_edit.getText().equals("")){
                lbl_exception.setText("Please Enter Product...");
            }else if(txtQty_edit.getText().equals("")){
                lbl_exception.setText("Please Enter Qty...");
                txtQty_edit.requestFocus();
            }else if(txtUnit_edit.getText().equals("")){
                lbl_exception.setText("Please Enter Unit Price...");
                txtUnit_edit.requestFocus();
            }else{
                String sql = "SELECT * FROM invoiceinfo WHERE product = '"+product+"' AND s = '1' AND invoice_no = '"+lbl_invoiceno.getText()+"'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                    if(rs.next()){
                        lbl_exception.setText(""+txtProduct_edit.getText()+" Already Exists...");
                        reset_edit();
                    }else{
                        save_invoiceinfo();
                    }
                }
        }catch(Exception e){
            System.out.println(e+" buttonsave_editActionPerformed");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }//GEN-LAST:event_buttonsave_editActionPerformed

    private void buttondelete_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondelete_editActionPerformed
       delete_invoiceLines();
    }//GEN-LAST:event_buttondelete_editActionPerformed

    private void txtQty_editKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQty_editKeyTyped
        char vchar = evt.getKeyChar();
          if((!(Character.isDigit(vchar)))
              ||(vchar == KeyEvent.VK_BACK_SPACE)

              || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtQty_editKeyTyped

    private void txtUnit_editKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnit_editKeyReleased
        calc_();
    }//GEN-LAST:event_txtUnit_editKeyReleased

    private void txtUnit_editKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnit_editKeyTyped
        char vchar = evt.getKeyChar();
          if((!(Character.isDigit(vchar)))
              ||(vchar == KeyEvent.VK_BACK_SPACE)

              || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtUnit_editKeyTyped

    private void txtTotal_editKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotal_editKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_editKeyTyped

    private void tableEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEditMouseClicked
        selectedroweditinfo();
    }//GEN-LAST:event_tableEditMouseClicked

    private void tableEditKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableEditKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedroweditinfo();
        }
    }//GEN-LAST:event_tableEditKeyReleased

    private void txtQty_editKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQty_editKeyReleased
        calc_();
    }//GEN-LAST:event_txtQty_editKeyReleased

    private void txtDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountFocusGained

    private void txtDiscountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountKeyTyped

    private void txtDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyPressed
       if(txtDiscount.getText().equals("")){
           lbl_total.setText(String.format("%.2f", getSumtotal()));
       }
    }//GEN-LAST:event_txtDiscountKeyPressed

    private void discount_calc(){
        String disc = txtDiscount.getText();
        disc = disc.trim();
        double discount = Double.parseDouble(disc);
            if(txtDiscount.getText().equals("")){
                lbl_total.setText(String.format("%.2f", getSumtotal()));
            }else{
                lbl_total.setText(String.format("%.2f", getSumtotal() - discount));
            }
    }
    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        if(txtDiscount.getText().equals("")){
            lbl_total.setText(String.format("%.2f", getSumtotal()));
        }else{
            discount_calc();
        }
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void txtDiscountInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtDiscountInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountInputMethodTextChanged

    private void buttondeleteinvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteinvoiceActionPerformed
        String query_invoice = "DELETE FROM invoicetable WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
        manage.delete(query_invoice);
        String query_invoice_line = "DELETE FROM invoiceinfo WHERE invoice_no = '"+txtInvoiceno.getText()+"'";
        manage.delete(query_invoice_line);
        String query_payments = "DELETE FROM paymentstable WHERE details = '"+txtInvoiceno.getText()+"'";
        manage.delete(query_payments);
        setbalance_clients();
        reset();
        lbl_exception.setText("Invoice # "+txtInvoiceno.getText()+" Deleted Successfully...");
        findInvoice_edit();
    }//GEN-LAST:event_buttondeleteinvoiceActionPerformed

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        lbl_exception.setText("");
    }//GEN-LAST:event_txtSearchFocusGained

    private void buttonPrintVatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintVatActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                String invoiceno = txtInvoiceno.getText();
               
                Manage manage = new Manage();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.phone_no AS 'company_phone',companytable.address AS "
                        + "'company_address',companytable.city AS 'company_city',companytable.email AS 'company_email',companytable.website AS 'company_website'"
                        + ",companytable.company_name,invoicetable.invoice_date,clientstable.name AS 'clients_name',clientstable.balance,userstable.name AS "
                        + "'user_name',invoicetable.invoice_no,invoicetable.total,invoicetable.discount,companytable.image FROM companytable,userstable,invoicetable"
                        + ",clientstable WHERE companytable.id = invoicetable.company_id AND userstable.id = invoicetable.user_id AND invoicetable.client_id = "
                        + "clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+invoiceno+"' AND companytable.image = '"+logopath+"'";
                String path = "Reports/invoice_vat.jrxml";
                manage.report(sql, path, panelReport);
                manage.report_(sql,path,panelReport,"generated_report.pdf");
                jTabbedPane2.setSelectedIndex(2);
                buttonEmail.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintVatActionPerformed

    private void buttonPrinteditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrinteditActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
               
                Manage manage = new Manage();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.phone_no AS 'company_phone',companytable.address"
                        + " AS 'company_address',companytable.city AS 'company_city',companytable.email AS 'company_email',companytable.website AS "
                        + "'company_website',companytable.company_name,invoicetable.invoice_date,clientstable.name AS 'clients_name',clientstable.balance"
                        + ",userstable.name AS 'user_name',invoicetable.invoice_no,invoicetable.total,invoicetable.discount FROM companytable,userstable,"
                        + "invoicetable,clientstable WHERE companytable.id = invoicetable.company_id AND userstable.id = invoicetable.user_id AND invoicetable"
                        + ".client_id = clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+lbl_invoiceno.getText()+"'";
                String path = "Reports/invoice.jrxml";
                manage.report(sql, path, panelReport);
                jTabbedPane2.setSelectedIndex(2);
                buttonEmail.setEnabled(true);
                resetInfo();
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrinteditActionPerformed

    private void buttonPrintVateditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintVateditActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
               
                Manage manage = new Manage();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.phone_no AS 'company_phone',companytable.address "
                        + "AS 'company_address',companytable.city AS 'company_city',companytable.email AS 'company_email',companytable.website AS "
                        + "'company_website',companytable.company_name,invoicetable.invoice_date,clientstable.name AS 'clients_name',clientstable.balance"
                        + ",userstable.name AS 'user_name',invoicetable.invoice_no,invoicetable.total,invoicetable.discount FROM companytable,userstable,invoicetable"
                        + ",clientstable WHERE companytable.id = invoicetable.company_id AND userstable.id = invoicetable.user_id AND invoicetable.client_id "
                        + "= clientstable.clientid AND invoicetable.s = '1' AND invoicetable.invoice_no = '"+lbl_invoiceno.getText()+"'";
                String path = "Reports/invoice_vat.jrxml";
                //manage.report(sql, path, panelReport);
                manage.report_(sql,path,panelReport,"generated_report.pdf");
                jTabbedPane2.setSelectedIndex(2);
                buttonEmail.setEnabled(true);
                resetInfo();
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintVateditActionPerformed

    private void buttonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEmailActionPerformed
        Date invoicedate = chooserInvoiceDate.getDate();
        if(invoicedate.after(manage.expiry())){
            lbl_exception.setText("Date Expired. Renew System...");
        }else{
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
                @Override
                protected Void doInBackground() throws Exception {
                    String quotedate = ((JTextField)chooserInvoiceDate.getDateEditor().getUiComponent()).getText();
                    String subject = "Invoice from "+companyname+" ON "+quotedate+" - ["+txtInvoiceno.getText()+"]";
                    String msg = "Thank you for working with us...";
                    String file_name = "INVOICE # "+txtInvoiceno.getText();
                    manage.sendemail_attachment(email,email_pass,email_to,subject,msg, file_name);
                    buttonEmail.setEnabled(false);
                    return null;
                }
            };
            worker.execute();
        }
        
    }//GEN-LAST:event_buttonEmailActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
        buttonEmail.setEnabled(false);
    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void txtProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductKeyReleased
        txtProduct.setText(txtProduct.getText().toUpperCase());
    }//GEN-LAST:event_txtProductKeyReleased

    private void txtProduct_editKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProduct_editKeyReleased
        txtProduct_edit.setText(txtProduct_edit.getText().toUpperCase());
    }//GEN-LAST:event_txtProduct_editKeyReleased
 
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogEditInvoice;
    public javax.swing.JDialog DialogInvoices;
    private javax.swing.JButton add;
    private javax.swing.JButton buttonAddInvoiceItems;
    private javax.swing.JButton buttonEmail;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonPrint;
    private javax.swing.JButton buttonPrintVat;
    private javax.swing.JButton buttonPrintVatedit;
    private javax.swing.JButton buttonPrintedit;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttondelete;
    private javax.swing.JButton buttondelete_edit;
    private javax.swing.JButton buttondeleteinvoice;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton buttonexit_edit;
    private javax.swing.JButton buttonreset;
    private javax.swing.JButton buttonreset_edit;
    private javax.swing.JButton buttonsave;
    private javax.swing.JButton buttonsave_edit;
    private com.toedter.calendar.JDateChooser chooserInvoiceDate;
    private javax.swing.JComboBox<String> comboBoxClient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
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
    public javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel labelserch;
    private javax.swing.JLabel labelserch1;
    private javax.swing.JLabel lblClient;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel lbl_invoiceno;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel panelReport;
    private javax.swing.JPanel paneledit;
    private javax.swing.JTable tableEdit;
    private javax.swing.JTable tableInvoiceDetails;
    private javax.swing.JTable tableInvoices;
    private javax.swing.JTable tableInvoices_edit;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JLabel txtInvoiceno;
    private javax.swing.JTextArea txtProduct;
    private javax.swing.JTextArea txtProduct_edit;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtQty_edit;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearching;
    private javax.swing.JLabel txtTotal;
    private javax.swing.JTextField txtTotal_edit;
    private javax.swing.JTextField txtTotalprice;
    private javax.swing.JTextField txtUnit_edit;
    private javax.swing.JTextField txtUnitprice;
    // End of variables declaration//GEN-END:variables
}
