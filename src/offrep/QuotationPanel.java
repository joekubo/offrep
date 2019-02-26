
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
import javax.swing.table.TableColumn;

public class QuotationPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private int serviceid;
    private int info_id;
    public String loggeduserid;
    public String email;
    public String email_pass;
    public String companyname;
    public String logopath;
    public int loggedcompanyid;
    private String product;
    
    Calendar c = Calendar.getInstance();
    Manage manage = new Manage();
    public QuotationPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
        removeothercolumns();
    }

    public void reset(){
        c.add(Calendar.YEAR, 0);
        chooserQuotationDate.getDateEditor().setDate(c.getTime());
        txtClientName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtPhoneNo.setText("");
        txtEmailAddress.setText("");
        txtClientName.requestFocus();
        txtTotal.setText("0.00");
        chooserQuotationDate.setEnabled(true);
        buttonAddQuotationItems.setEnabled(false);
        lbl_total.setText("");
        generateQuotation_no();
        findQuotation();
        buttonReset.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonDelete.setEnabled(true);
        buttonPrint.setEnabled(false);
        buttonEmail.setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);
    }
     public void resetInfo(){
        txtProduct.setText("");
        txtTotalPrice.setText("");
        txtQty.setText("");
        txtUnitPrice.setText("");
        buttonsave.setEnabled(true);
        buttondelete.setEnabled(false);
        buttonPrint.setEnabled(false);
        add.setEnabled(false);
        product = "";
        txtProduct.requestFocus();
    }
    private void cleartablequotationInfo(){
        removeothercolumns();
        tableQuotationDetails.setModel(new DefaultTableModel(null,new String[]{"invoiceno","Product","Qty","Unit Price","Total Price","s","company_id"}));
    }
    private void calc(){
        String qty = txtQty.getText();
        qty = qty.trim();
        int quantity = Integer.parseInt(qty);
        
        String unit = txtUnitPrice.getText();
        unit = unit.trim();
        
        
        double unitprice = Double.parseDouble(unit);
        txtTotalPrice.setText(String.format("%.2f", quantity * unitprice ));
    }
    //calculate total value of all the invoice services
    private double getSumtotal(){
        int rowscount = tableQuotationDetails.getRowCount();
        double sum_totals = 0;
            for(int i = 0; i < rowscount; i++){
                sum_totals = sum_totals + Double.parseDouble(tableQuotationDetails.getValueAt(i, 4).toString());
            }
        return sum_totals;
    }
    
    
    
   
    private void savequotation(){
        try{
            String sql = "INSERT INTO quotation_table(quotation_no,client_name,address,city,phone_no,email,quotation_date,total,user_id,s,company_id)VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setInt(11, loggedcompanyid);
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
            String query_quote = "DELETE FROM quotation_table WHERE quotation_no = '"+txtQuotationNo.getText()+"'";
            manage.delete(query_quote);
            System.out.println(e+"savequotation");
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
                  //conn.setAutoCommit(false);

                  String sql = "INSERT INTO quotation_info(quotation_no,product,price,s,qty,unit_price,company_id)VALUES(?,?,?,?,?,?,?)";

                  pst = conn.prepareStatement(sql);
                      for(int row = 0; row<rows; row++){
                          String quotationno = (String)tableQuotationDetails.getValueAt(row, 0);
                          String product_ = (String)tableQuotationDetails.getValueAt(row, 1);
                          double price = (Double)tableQuotationDetails.getValueAt(row, 4);
                          String s = (String)tableQuotationDetails.getValueAt(row, 5);
                          String qty = (String)tableQuotationDetails.getValueAt(row, 2);
                          Double unitprice = (Double)tableQuotationDetails.getValueAt(row, 3); 
                          int companyid = (Integer)tableQuotationDetails.getValueAt(row, 6);
                          
                          pst.setString(1, quotationno);
                          pst.setString(2, product_);
                          pst.setDouble(3, price);
                          pst.setString(4, s);
                          pst.setString(5, qty);
                          pst.setDouble(6, unitprice);
                          pst.setInt(7, companyid);
                          pst.execute();
                      }
        }catch(Exception e){
            System.out.println( e+ "savequotation_info");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void loadquotationinfo_transaction(){

        String product_ = txtProduct.getText();
        
        String price = txtTotalPrice.getText();
        price = price.trim();
        double $price = Double.parseDouble(price);
        
        String unit_ = txtUnitPrice.getText();
        unit_ = unit_.trim();
        double $unit = Double.parseDouble(unit_);
        
                DefaultTableModel model = (DefaultTableModel)tableQuotationDetails.getModel();
                model.addRow(new Object[]{txtQuotationNo.getText(),
                                          product_,
                                          txtQty.getText(),
                                          $unit,
                                          $price,
                                          "1",
                                          loggedcompanyid});
                                          
                lbl_total.setText(Double.toString(getSumtotal()));
                txtTotal.setText(Double.toString(getSumtotal()));
                
                resetInfo();
    }
    
    private void removeothercolumns(){
            TableColumn idColumn = tableQuotationDetails.getColumn("invoiceno");
            TableColumn idColumn1 = tableQuotationDetails.getColumn("s");
            TableColumn idColumn2 = tableQuotationDetails.getColumn("company_id");
            
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
            System.out.println(e+" generateQuotation_no");
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
            int row = tableQuotation.getSelectedRow();
            String table_click = tableQuotation.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM quotation_table WHERE s = '1' AND quotation_no = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    txtQuotationNo.setText(rs.getString("quotation_no"));
                    ((JTextField)chooserQuotationDate.getDateEditor().getUiComponent()).setText(rs.getString("quotation_date"));
                    txtClientName.setText(rs.getString("client_name"));
                    txtAddress.setText(rs.getString("address"));
                    txtCity.setText(rs.getString("city"));
                    txtPhoneNo.setText(rs.getString("phone_no"));
                    txtEmailAddress.setText(rs.getString("email"));
                    txtTotal.setText(rs.getString("total"));
                    manage.showdialog(DialogEditQuote, jPanel1,679, 446);
                    lbl_exception.setText("");
                    buttonAddQuotationItems.setEnabled(false);
                    buttonReset.setEnabled(false);
                    buttonDelete.setEnabled(false);
                    buttonPrint.setEnabled(false);
                    buttonEmail.setEnabled(false);
                    buttonExit.setEnabled(false);
                    loadquote_items();
                }
            
        }catch(Exception e){
            System.out.println(e+" selectedrow");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void loadquote_items(){
        String query = "SELECT id,product AS 'Product',qty AS 'Qty',unit_price AS 'Unit Price',price AS 'Total Price' FROM quotation_info WHERE s = '1' "
                + "AND quotation_no = '"+txtQuotationNo.getText()+"'";
                    manage.update_table(query, tableInfo);
    }
    private void selectedrow_info(){
        try{
            int row = tableInfo.getSelectedRow();
            String table_click = tableInfo.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM quotation_info WHERE s = '1' AND id = '"+table_click+"' AND company_id = '"+loggedcompanyid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    info_id = rs.getInt("id");
                    product = rs.getString("product");
                    txtProductedit.setText(product);
                    txtQtyInfo.setText(rs.getString("qty"));
                    txtUnitpriceInfo.setText(rs.getString("unit_price"));
                    txtTotalpriceInfo.setText(rs.getString("price"));
                }
        }catch(Exception e){
            System.out.println(e+" selectedrow_info");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void deleteQuote(){
        String sql = "DELETE FROM quotation_table WHERE s = '1' AND quotation_no = '"+txtQuotationNo.getText()+"'";
        manage.delete(sql);
        String query = "DELETE FROM quotation_info WHERE s = '1' AND quotation_no = '"+txtQuotationNo.getText()+"'";
        manage.delete(query);
        String name = "Invoice # "+txtQuotationNo.getText()+"";
        generateQuotation_no();
        reset();
        resetInfo();
    }
    private void save_editinfo(){
        try{
            String total_ = txtTotalpriceInfo.getText();
            total_ = total_.trim();
            double total = Double.parseDouble(total_);
            
            if(txtQtyInfo.getText().equals("")){
                lbl_exception.setText("Please Enter Qty...");
                txtQtyInfo.requestFocus();
            }else if(txtUnitpriceInfo.getText().equals("")){
                lbl_exception.setText("Please Enter Unit Price...");
                txtUnitpriceInfo.requestFocus();
            }else{
                String sql = "INSERT INTO quotation_info(quotation_no,product,qty,unit_price,price,s,company_id)VALUES(?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                
                pst.setString(1, txtQuotationNo.getText());
                pst.setString(2, txtProductedit.getText());
                pst.setString(3, txtQtyInfo.getText());
                pst.setString(4, txtUnitpriceInfo.getText());
                pst.setString(5, txtTotalpriceInfo.getText());
                pst.setString(6, "1");
                pst.setInt(7, loggedcompanyid);
                
                pst.execute();
                String query_quote = "UPDATE quotation_table SET total = total + '"+total+"' WHERE  s = '1' AND quotation_no = '"+txtQuotationNo.getText()+"'"
                        + " AND company_id = '"+loggedcompanyid+"'";
                manage.update(query_quote);
                reset_quote_info();
                lbl_exception.setText("Quotation Service/Item "+txtProductedit.getText()+" Added Successfully...");
            }
        }catch(Exception e){
            System.out.println(e+" save_editinfo");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void reset_quote_info(){
        txtProductedit.setText("");
        txtQtyInfo.setText("");
        txtUnitpriceInfo.setText("");
        txtTotalpriceInfo.setText("");
        txtProductedit.requestFocus();
        lbl_exception.setText("");
        delete_nill();
        loadquote_items();
    }
    private void delete_info(){
        String total_ = txtTotalpriceInfo.getText();
        total_ = total_.trim();
        double total = Double.parseDouble(total_);
        
        String sql = "DELETE FROM quotation_info WHERE id = '"+info_id+"'AND s = '1'";
        manage.delete(sql);
        String quote = "UPDATE quotation_table SET total = total - '"+total+"' WHERE quotation_no = '"+txtQuotationNo.getText()+"' AND s = '1'";
        manage.update(quote);
        reset_quote_info();
        lbl_exception.setText(""+txtProductedit.getText()+" Deleted Successfully...");
        
    }
    private void delete_nill(){
        String sql = "DELETE FROM quotation_table WHERE total = '0'";
        manage.delete(sql);
        findQuotation();
    }
    public ArrayList<SearchQuotation> ListQuotation(String ValToSearch){
        ArrayList<SearchQuotation>viewQuotation = new ArrayList<SearchQuotation>();
        try{
            String searchQuery = "SELECT * FROM quotation_table,userstable WHERE quotation_table.user_id = userstable.id AND CONCAT(quotation_table.quotation_no,'',"
                    + "quotation_table.client_name,'',quotation_table.phone_no,'',quotation_table.email,'',quotation_table.quotation_date,'',userstable.name) "
                    + "LIKE'%"+ValToSearch+"%' AND quotation_table.s = '1' AND quotation_table.company_id = '"+loggedcompanyid+"'";
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
            System.out.println(e+" SearchQuotation/findQuotation");
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
        ArrayList<SearchQuotation> view= ListQuotation(txtSearch.getText());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Quotation #","Client Name","Phone #","Email","Quotation Date","Total"});
        
        Object[] row = new Object[6];
        
            for(int i = 0; i < view.size(); i ++){
                
             row[0] = view.get(i).getQuotationno();
             row[1] = view.get(i).getClientname();
             row[2] = view.get(i).getPhoneno();
             row[3] = view.get(i).getEmail();
             row[4] = view.get(i).getInvoicedate();
             row[5] = view.get(i).getTotal();
             
             model.addRow(row);
            }
            tableQuotation.setModel(model);
    }
    private void calc_edit(){
        String qty = txtQtyInfo.getText();
        qty = qty.trim();
        int $qty = Integer.parseInt(qty);
        
        String unit = txtUnitpriceInfo.getText();
        unit = unit.trim();
        double $unit = Double.parseDouble(unit);
        
        txtTotalpriceInfo.setText(String.format("%.2f", $qty * $unit));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogQuotations = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        txtTotalPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtProduct = new javax.swing.JTextArea();
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
        DialogEditQuote = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtQtyInfo = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtUnitpriceInfo = new javax.swing.JTextField();
        txtTotalpriceInfo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtProductedit = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableInfo = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        buttonResetInfo = new javax.swing.JButton();
        buttonExitInfo = new javax.swing.JButton();
        buttonsaveInfo = new javax.swing.JButton();
        buttondeleteInfo = new javax.swing.JButton();
        lbl_exception = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();
        buttonExit = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonPrint = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txtEmailAddress = new javax.swing.JTextField();
        txtTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        buttonAddQuotationItems = new javax.swing.JButton();
        txtClientName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        chooserQuotationDate = new com.toedter.calendar.JDateChooser();
        txtAddress = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtPhoneNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableQuotation = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        labelserch = new javax.swing.JLabel();
        panelQuote = new javax.swing.JPanel();
        buttonEmail = new javax.swing.JButton();

        DialogQuotations.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogQuotations.setUndecorated(true);
        DialogQuotations.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                DialogQuotationsWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(214, 193, 240));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel7.setBackground(new java.awt.Color(214, 193, 240));

        txtTotalPrice.setEditable(false);
        txtTotalPrice.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTotalPriceFocusGained(evt);
            }
        });
        txtTotalPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalPriceKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalPriceKeyPressed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("Product:");

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("Total Price:");

        jLabel21.setForeground(new java.awt.Color(0, 0, 153));
        jLabel21.setText("Qty:");

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

        jLabel22.setForeground(new java.awt.Color(0, 0, 153));
        jLabel22.setText("Unit Price:");

        txtUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUnitPriceFocusGained(evt);
            }
        });
        txtUnitPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUnitPriceKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUnitPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitPriceKeyReleased(evt);
            }
        });

        txtProduct.setColumns(20);
        txtProduct.setRows(5);
        txtProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(txtProduct);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        lbl_total.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lbl_total.setText("              ");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("Total:");

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
                .addGap(144, 144, 144)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_total, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
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
                "invoiceno", "Product", "Qty", "Unit Price", "Total Price", "s", "company_id"
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

        DialogEditQuote.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogEditQuote.setUndecorated(true);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel16.setForeground(new java.awt.Color(0, 0, 153));
        jLabel16.setText("Qty:");

        txtQtyInfo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyInfoFocusGained(evt);
            }
        });
        txtQtyInfo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyInfoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtyInfoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyInfoKeyReleased(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(0, 0, 153));
        jLabel17.setText("Unit Price:");

        txtUnitpriceInfo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUnitpriceInfoFocusGained(evt);
            }
        });
        txtUnitpriceInfo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUnitpriceInfoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUnitpriceInfoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUnitpriceInfoKeyReleased(evt);
            }
        });

        txtTotalpriceInfo.setEditable(false);
        txtTotalpriceInfo.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalpriceInfo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTotalpriceInfoFocusGained(evt);
            }
        });
        txtTotalpriceInfo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalpriceInfoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalpriceInfoKeyPressed(evt);
            }
        });

        jLabel19.setForeground(new java.awt.Color(0, 0, 153));
        jLabel19.setText("Total Price:");

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("Product:");

        txtProductedit.setColumns(20);
        txtProductedit.setRows(5);
        txtProductedit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProducteditKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(txtProductedit);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtQtyInfo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalpriceInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUnitpriceInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtUnitpriceInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(txtTotalpriceInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(txtQtyInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        tableInfo.setBackground(new java.awt.Color(255, 255, 204));
        tableInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInfoMouseClicked(evt);
            }
        });
        tableInfo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableInfoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableInfoKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tableInfo);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonResetInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reset.png"))); // NOI18N
        buttonResetInfo.setText("Reset");
        buttonResetInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetInfoActionPerformed(evt);
            }
        });

        buttonExitInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/exit.png"))); // NOI18N
        buttonExitInfo.setText("Exit");
        buttonExitInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitInfoActionPerformed(evt);
            }
        });

        buttonsaveInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonsaveInfo.setText("Save");
        buttonsaveInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonsaveInfoActionPerformed(evt);
            }
        });

        buttondeleteInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttondeleteInfo.setText("Delete");
        buttondeleteInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondeleteInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(buttonResetInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(buttonsaveInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(buttondeleteInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(buttonExitInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonResetInfo)
                    .addComponent(buttonExitInfo)
                    .addComponent(buttonsaveInfo)
                    .addComponent(buttondeleteInfo))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout DialogEditQuoteLayout = new javax.swing.GroupLayout(DialogEditQuote.getContentPane());
        DialogEditQuote.getContentPane().setLayout(DialogEditQuoteLayout);
        DialogEditQuoteLayout.setHorizontalGroup(
            DialogEditQuoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEditQuoteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogEditQuoteLayout.setVerticalGroup(
            DialogEditQuoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEditQuoteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBackground(new java.awt.Color(214, 193, 240));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        lbl_exception.setFont(new java.awt.Font("SansSerif", 3, 12)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/quotation.png"))); // NOI18N
        jLabel1.setText("Quotation");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jTabbedPane1.setBackground(new java.awt.Color(214, 193, 240));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

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

        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/trash.png"))); // NOI18N
        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        buttonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/print.png"))); // NOI18N
        buttonPrint.setText("Print Quote");
        buttonPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrintActionPerformed(evt);
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
                .addComponent(buttonPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74)
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
                    .addComponent(buttonDelete)
                    .addComponent(buttonPrint))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(214, 193, 240));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("Quotation #:");

        txtQuotationNo.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtQuotationNo.setForeground(new java.awt.Color(153, 0, 0));
        txtQuotationNo.setText("                   ");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        jPanel9.setBackground(new java.awt.Color(214, 193, 240));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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

        txtTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtTotal.setText("                        ");

        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("City:");

        buttonAddQuotationItems.setText("Add Quotation Item");
        buttonAddQuotationItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddQuotationItemsActionPerformed(evt);
            }
        });

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

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Address:");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Total:");

        chooserQuotationDate.setDateFormatString("yyyy-MM-dd");

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

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Client Name:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("Date:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Phone #:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("Email Address:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooserQuotationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(46, 46, 46)
                                        .addComponent(buttonAddQuotationItems))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(27, 27, 27)
                                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chooserQuotationDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonAddQuotationItems)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(214, 193, 240));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

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
        jScrollPane1.setViewportView(tableQuotation);

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
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addComponent(labelserch)
                                .addGap(374, 374, 374))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(207, 207, 207))))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addComponent(labelserch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
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
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 293, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 739, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Create Quote", jPanel2);

        javax.swing.GroupLayout panelQuoteLayout = new javax.swing.GroupLayout(panelQuote);
        panelQuote.setLayout(panelQuoteLayout);
        panelQuoteLayout.setHorizontalGroup(
            panelQuoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1042, Short.MAX_VALUE)
        );
        panelQuoteLayout.setVerticalGroup(
            panelQuoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Loading Report", panelQuote);

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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1044, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(301, 301, 301)
                        .addComponent(lbl_exception, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

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

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION){

        }else if (response == JOptionPane.YES_OPTION){
            deleteQuote();
        }else if(response == JOptionPane.CLOSED_OPTION){

        }
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrintActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                String quoteno = txtQuotationNo.getText();
                String sql = "SELECT companytable.company_name,companytable.Dealer_in,companytable.address AS 'company_address',companytable.city AS"
                        + " 'company_city',companytable.phone_no AS 'company_phone',companytable.email AS 'company_email',companytable.website AS 'company_website'"
                        + ",companytable.company_name,companytable.image AS 'company_image',quotation_table.quotation_date,userstable.name AS 'user_name'"
                        + ",quotation_table.quotation_no,quotation_table.client_name,quotation_table.total,companytable.image FROM companytable,userstable,quotation_table WHERE "
                        + "companytable.id = quotation_table.company_id AND userstable.id = quotation_table.user_id AND quotation_table.s = '1' "
                        + "AND quotation_table.quotation_no = '"+quoteno+"' AND companytable.image = '"+logopath+"'";
                String path = "Reports/quotation.jrxml";
                manage.report_(sql,path,panelQuote);
                jTabbedPane1.setSelectedIndex(1);
                buttonEmail.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_buttonPrintActionPerformed

    private void txtEmailAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailAddressActionPerformed

    private void txtEmailAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtEmailAddressKeyPressed

    private void txtEmailAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyReleased
        txtEmailAddress.setText(txtEmailAddress.getText().toLowerCase());
    }//GEN-LAST:event_txtEmailAddressKeyReleased

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
            manage.showdialog(DialogQuotations, jPanel1,807, 436);
            buttonAddQuotationItems.setEnabled(false);
            chooserQuotationDate.setEnabled(false);
            buttonReset.setEnabled(false);
            buttonExit.setEnabled(false);
            resetInfo();
        }
    }//GEN-LAST:event_buttonAddQuotationItemsActionPerformed

    private void txtClientNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClientNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClientNameActionPerformed

    private void txtClientNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClientNameKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtClientNameKeyPressed

    private void txtClientNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClientNameKeyReleased
        txtClientName.setText(txtClientName.getText().toUpperCase());
    }//GEN-LAST:event_txtClientNameKeyReleased

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        txtAddress.setText(txtAddress.getText().toUpperCase());
    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCityActionPerformed

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        lbl_exception.setText("");
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        txtCity.setText(txtCity.getText().toUpperCase());
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtPhoneNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNoActionPerformed

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
        buttonAddQuotationItems.setEnabled(true);
    }//GEN-LAST:event_txtPhoneNoKeyPressed

    private void txtPhoneNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneNoKeyReleased
        txtPhoneNo.setText(txtPhoneNo.getText().toUpperCase());
    }//GEN-LAST:event_txtPhoneNoKeyReleased

    private void tableQuotationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQuotationMouseClicked
        selectedrow();
    }//GEN-LAST:event_tableQuotationMouseClicked

    private void tableQuotationKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableQuotationKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableQuotationKeyReleased

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        findQuotation();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        buttonEmail.setEnabled(false);
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void buttonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEmailActionPerformed
         Date invoicedate = chooserQuotationDate.getDate();
        if(invoicedate.after(manage.expiry())){
            lbl_exception.setText("Date Expired. Renew System...");
        }else{
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
                @Override
                protected Void doInBackground() throws Exception {

                    String quotedate = ((JTextField)chooserQuotationDate.getDateEditor().getUiComponent()).getText();
                    String email_to = txtEmailAddress.getText();
                    String subject = "Quotation from "+companyname+" ON "+quotedate+" - ["+txtQuotationNo.getText()+"]";
                    String msg = "Thank you in advance for working with us...";
                    String file_name = "QUOTATION # "+txtQuotationNo.getText()+".pdf";
                    manage.sendemail_attachment(email,email_pass,email_to,subject,msg, file_name);
                    buttonEmail.setEnabled(false);
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_buttonEmailActionPerformed

    private void txtTotalPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalPriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPriceFocusGained

    private void txtTotalPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalPriceKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtTotalPriceKeyTyped

    private void txtTotalPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalPriceKeyPressed
        if(txtTotalPrice.getText().equals("")){
            add.setEnabled(false);
        }else{
            add.setEnabled(true);
        }
    }//GEN-LAST:event_txtTotalPriceKeyPressed

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
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyKeyPressed

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        calc();
    }//GEN-LAST:event_txtQtyKeyReleased

    private void txtUnitPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnitPriceFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitPriceFocusGained

    private void txtUnitPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitPriceKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtUnitPriceKeyTyped

    private void txtUnitPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitPriceKeyPressed
        add.setEnabled(true);
    }//GEN-LAST:event_txtUnitPriceKeyPressed

    private void txtUnitPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitPriceKeyReleased
        calc();
    }//GEN-LAST:event_txtUnitPriceKeyReleased

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        loadquotationinfo_transaction();
    }//GEN-LAST:event_addActionPerformed

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

    private void buttonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveActionPerformed

        Date quotationdate = chooserQuotationDate.getDate();
        if(manage.expiry().before(quotationdate)){
            lbl_exception.setText("Date Expired. Renew System...");
        }else{
            savequotation();
        }
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void buttondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteActionPerformed
        //removeothercolumns();
        int row = tableQuotationDetails.getSelectedRow();
        ((DefaultTableModel)tableQuotationDetails.getModel()).removeRow(row);
        lbl_total.setText(Double.toString(getSumtotal()));
        txtTotal.setText(Double.toString(getSumtotal()));
    }//GEN-LAST:event_buttondeleteActionPerformed

    private void tableQuotationDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQuotationDetailsMouseClicked
        buttonsave.setEnabled(false);
        buttondelete.setEnabled(true);
        buttonPrint.setEnabled(false);
        int row = tableQuotationDetails.getSelectedRow();
        int table_click = (Integer)tableQuotationDetails.getValueAt(row, 1);
        serviceid = table_click;
    }//GEN-LAST:event_tableQuotationDetailsMouseClicked

    private void DialogQuotationsWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DialogQuotationsWindowOpened

    }//GEN-LAST:event_DialogQuotationsWindowOpened

    private void txtQtyInfoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyInfoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyInfoFocusGained

    private void txtQtyInfoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyInfoKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtQtyInfoKeyTyped

    private void txtQtyInfoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyInfoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyInfoKeyPressed

    private void txtQtyInfoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyInfoKeyReleased
        calc_edit();
    }//GEN-LAST:event_txtQtyInfoKeyReleased

    private void txtUnitpriceInfoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUnitpriceInfoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitpriceInfoFocusGained

    private void txtUnitpriceInfoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceInfoKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtUnitpriceInfoKeyTyped

    private void txtUnitpriceInfoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceInfoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitpriceInfoKeyPressed

    private void txtUnitpriceInfoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUnitpriceInfoKeyReleased
        calc_edit();
    }//GEN-LAST:event_txtUnitpriceInfoKeyReleased

    private void txtTotalpriceInfoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalpriceInfoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalpriceInfoFocusGained

    private void txtTotalpriceInfoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalpriceInfoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalpriceInfoKeyTyped

    private void txtTotalpriceInfoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalpriceInfoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalpriceInfoKeyPressed

    private void tableInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInfoMouseClicked
        selectedrow_info();
    }//GEN-LAST:event_tableInfoMouseClicked

    private void tableInfoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableInfoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableInfoKeyPressed

    private void tableInfoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableInfoKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow_info();
        }
    }//GEN-LAST:event_tableInfoKeyReleased

    private void buttonResetInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetInfoActionPerformed
        reset_quote_info();
    }//GEN-LAST:event_buttonResetInfoActionPerformed

    private void buttonExitInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitInfoActionPerformed
        DialogEditQuote.dispose();
        buttonAddQuotationItems.setEnabled(true);
        chooserQuotationDate.setEnabled(true);
        buttonReset.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonDelete.setEnabled(true);
        buttonPrint.setEnabled(true);
        buttonAddQuotationItems.setEnabled(false);
        add.setEnabled(true);
        reset_quote_info();
    }//GEN-LAST:event_buttonExitInfoActionPerformed

    private void buttonsaveInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveInfoActionPerformed
        try{
            String sql = "SELECT * FROM quotation_info WHERE quotation_no = '"+txtQuotationNo.getText()+"' AND s = '1' AND product = '"+product+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                lbl_exception.setText("Can only Add Product "+txtProductedit.getText()+". Already Exists...");
            }else{
                save_editinfo();
            }
        }catch(Exception e){
            System.out.println(e+" buttonsaveInfoActionPerformed");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){

            }
        }
    }//GEN-LAST:event_buttonsaveInfoActionPerformed

    private void buttondeleteInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteInfoActionPerformed
        delete_info();
    }//GEN-LAST:event_buttondeleteInfoActionPerformed

    private void txtProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductKeyReleased
        txtProduct.setText(txtProduct.getText().toUpperCase());
    }//GEN-LAST:event_txtProductKeyReleased

    private void txtProducteditKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProducteditKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProducteditKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogEditQuote;
    public javax.swing.JDialog DialogQuotations;
    private javax.swing.JButton add;
    private javax.swing.JButton buttonAddQuotationItems;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonEmail;
    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonExitInfo;
    private javax.swing.JButton buttonPrint;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonResetInfo;
    private javax.swing.JButton buttondelete;
    private javax.swing.JButton buttondeleteInfo;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton buttonreset;
    private javax.swing.JButton buttonsave;
    private javax.swing.JButton buttonsaveInfo;
    private com.toedter.calendar.JDateChooser chooserQuotationDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelserch;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel panelQuote;
    private javax.swing.JTable tableInfo;
    private javax.swing.JTable tableQuotation;
    private javax.swing.JTable tableQuotationDetails;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextArea txtProduct;
    private javax.swing.JTextArea txtProductedit;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtQtyInfo;
    private javax.swing.JLabel txtQuotationNo;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JLabel txtTotal;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtTotalpriceInfo;
    private javax.swing.JTextField txtUnitPrice;
    private javax.swing.JTextField txtUnitpriceInfo;
    // End of variables declaration//GEN-END:variables
}
