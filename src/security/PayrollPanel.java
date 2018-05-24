
package security;
import com.toedter.calendar.JCalendar;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


public class PayrollPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private String expenseid;
    private int employeeid;
    private String paymentid;
    private String benefitid;
    private String deductionid;
    private double total_salaries;
    Manage manage;
    
    Calendar c = Calendar.getInstance();
        
    public PayrollPanel() {
        initComponents();
        conn = Javaconnect.ConnecrDb();
        
    }
//______________________________________________________________________________________________________________________---employees---_________________________________
    public void monthNyear(){
    JCalendar calendar = new JCalendar();
    String mymonth;
    SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM");
    java.util.Date date1 = calendar.getDate();
    mymonth = sdf1.format(date1);
    comboMonth.setSelectedItem(mymonth);
   
    SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY");
    java.util.Date date2 = calendar.getDate();
    mymonth = sdf2.format(date2);
    comboYear.setSelectedItem(mymonth);
    }
    
    public void loadEmployees(){
        manage = new Manage();
        String sql = "SELECT employee_id AS 'id',employee_no AS 'Emp #',name AS 'Employee Name',email AS 'Email Address',CONCAT(address,' ',city) AS 'Address',"
                + "telephone_no AS 'Tel #',keen AS 'Next of Keen',keen_phoneno AS 'N.O.Keen Phone #',nssf_no AS 'NSSF #',nhif_no AS 'NHIF #' FROM employeestable WHERE s = '1'";
        manage.update_table(sql, tableEmployees);
    }
    public void resetemployees(){
        txtEmployeeNo.setText("");
        txtEmployeeName.setText("");
        txtPinNo.setText("");
        txtEmailAddress.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtTelephoneNo.setText("");
        txtIdNo.setText("");
        txtNextKeen.setText("");
        txtKeenNo.setText("");
        txtNhifNo.setText("");
        txtNssfNo.setText("");
        txtAccountNo.setText("");
        txtBasicSalary.setText("");
        txtEmployeeNo.requestFocus();
        loadEmployees();
        employeeid = 0;
        buttonSaveEmployee.setEnabled(true);
        buttonUpdateEmployee.setEnabled(false);
        buttonDeleteEmployee.setEnabled(false);
    }
    private void saveemployee(){
        try{
            String employeeno = txtEmployeeNo.getText();
            String name = txtEmployeeName.getText();
            String pin = txtPinNo.getText();
            String email = txtEmailAddress.getText();
            String address = txtAddress.getText();
            String city = txtCity.getText();
            String tel = txtTelephoneNo.getText();
            String idno = txtIdNo.getText();
            String keen = txtNextKeen.getText();
            String keenno = txtKeenNo.getText();
            String nssf = txtNssfNo.getText();
            String nhif = txtNhifNo.getText();
            String accno = txtAccountNo.getText();
            String basic = txtBasicSalary.getText();
            
            if(employeeno.equals("")){
                lbl_exception.setText("Please Enter Employee #...");
                txtEmployeeNo.requestFocus();
            }else if(name.equals("")){
                lbl_exception.setText("Please Enter Employee Name...");
                txtEmployeeName.requestFocus();
            }else if(pin.equals("")){
                lbl_exception.setText("Please Enter PIN #...");
                txtPinNo.requestFocus();
            }else if(email.equals("")){
                lbl_exception.setText("Please Enter Email Address...");
                txtEmailAddress.requestFocus();
            }else if(address.equals("")){
                lbl_exception.setText("Please Enter Address...");
                txtAddress.requestFocus();
            }else if(city.equals("")){
                lbl_exception.setText("Please Enter City...");
                txtCity.requestFocus();
            }else if(tel.equals("")){
                lbl_exception.setText("Please Enter Telephon #...");
                txtTelephoneNo.requestFocus();
            }else if(idno.equals("")){
                lbl_exception.setText("Please Enter ID #...");
                txtIdNo.requestFocus();
            }else if(keen.equals("")){
                lbl_exception.setText("Please Enter Next of Keen...");
                txtNextKeen.requestFocus();
            }else if(keenno.equals("")){
                lbl_exception.setText("Please Enter Keen #...");
                txtKeenNo.requestFocus();
            }else if(nssf.equals("")){
                lbl_exception.setText("Please Enter NSSF #...");
                txtNssfNo.requestFocus();
            }else if(nhif.equals("")){
                lbl_exception.setText("Please Enter NHIF #...");
                txtNhifNo.requestFocus();
            }else if(accno.equals("")){
                lbl_exception.setText("Please Enter Account #...");
                txtAccountNo.requestFocus();
            }else if(basic.equals("")){
                lbl_exception.setText("Please Enter Basic Salary...");
                txtBasicSalary.requestFocus();
            }else{
                String sql = "INSERT INTO employeestable(employee_no,name,pin,email,address,city,telephone_no,id_no,nssf_no,nhif_no,account_no,basic_salary,company_id,s"
                        + ",keen,keen_phoneno)"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                
                pst.setString(1, employeeno);
                pst.setString(2, name);
                pst.setString(3, pin);
                pst.setString(4, email);
                pst.setString(5, address);
                pst.setString(6, city);
                pst.setString(7, tel);
                pst.setString(8, idno);
                pst.setString(9, nssf);
                pst.setString(10, nhif);
                pst.setString(11, accno);
                pst.setString(12, basic);
                pst.setString(13, "1");
                pst.setString(14, "1");
                pst.setString(15, keen);
                pst.setString(16, keenno);
                
                pst.execute();
                lbl_exception.setText("Employee Saved Successfully...");
                resetemployees();
            }
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" saveemployee");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void selectedrowemployees(){
        try{
            int row = tableEmployees.getSelectedRow();
            String table_click = tableEmployees.getValueAt(row, 0).toString();
            
            String sql = "SELECT * FROM employeestable WHERE employee_id = '"+table_click+"' AND s = '1'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    employeeid = rs.getInt("employee_id");
                    txtEmployeeNo.setText(rs.getString("employee_no"));
                    txtEmployeeName.setText(rs.getString("name"));
                    txtPinNo.setText(rs.getString("pin"));
                    txtEmailAddress.setText(rs.getString("email"));
                    txtAddress.setText(rs.getString("address"));
                    txtCity.setText(rs.getString("city"));
                    txtTelephoneNo.setText(rs.getString("telephone_no"));
                    txtIdNo.setText(rs.getString("id_no"));
                    txtNhifNo.setText(rs.getString("nhif_no"));
                    txtNssfNo.setText(rs.getString("nssf_no"));
                    txtAccountNo.setText(rs.getString("account_no"));
                    txtBasicSalary.setText(rs.getString("basic_salary"));
                    txtNextKeen.setText(rs.getString("keen"));
                    txtKeenNo.setText(rs.getString("keen_phoneno"));
                    
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e+" selectedrowemployees");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void updateemployee(){
                String employeeno = txtEmployeeNo.getText();
                String name = txtEmployeeName.getText();
                String pin = txtPinNo.getText();
                String email = txtEmailAddress.getText();
                String address = txtAddress.getText();
                String city = txtCity.getText();
                String tel = txtTelephoneNo.getText();
                String idno = txtIdNo.getText();
                String keen = txtNextKeen.getText();
                String keenno = txtKeenNo.getText();
                String nssf = txtNssfNo.getText();
                String nhif = txtNhifNo.getText();
                String accno = txtAccountNo.getText();
                String basic = txtBasicSalary.getText();
                
            if(employeeno.equals("")){
                lbl_exception.setText("Please Enter Employee #...");
                txtEmployeeNo.requestFocus();
            }else if(name.equals("")){
                lbl_exception.setText("Please Enter Employee Name...");
                txtEmployeeName.requestFocus();
            }else if(pin.equals("")){
                lbl_exception.setText("Please Enter PIN #...");
                txtPinNo.requestFocus();
            }else if(email.equals("")){
                lbl_exception.setText("Please Enter Email Address...");
                txtEmailAddress.requestFocus();
            }else if(address.equals("")){
                lbl_exception.setText("Please Enter Address...");
                txtAddress.requestFocus();
            }else if(city.equals("")){
                lbl_exception.setText("Please Enter City...");
                txtCity.requestFocus();
            }else if(tel.equals("")){
                lbl_exception.setText("Please Enter Telephon #...");
                txtTelephoneNo.requestFocus();
            }else if(idno.equals("")){
                lbl_exception.setText("Please Enter ID #...");
                txtIdNo.requestFocus();
            }else if(keen.equals("")){
                lbl_exception.setText("Please Enter Next of Keen...");
                txtNextKeen.requestFocus();
            }else if(keenno.equals("")){
                lbl_exception.setText("Please Enter Keen #...");
                txtKeenNo.requestFocus();
            }else if(nssf.equals("")){
                lbl_exception.setText("Please Enter NSSF #...");
                txtNssfNo.requestFocus();
            }else if(nhif.equals("")){
                lbl_exception.setText("Please Enter NHIF #...");
                txtNhifNo.requestFocus();
            }else if(accno.equals("")){
                lbl_exception.setText("Please Enter Account #...");
                txtAccountNo.requestFocus();
            }else if(basic.equals("")){
                lbl_exception.setText("Please Enter Basic Salary...");
                txtBasicSalary.requestFocus();
            }else{
                manage = new Manage();
                String sql = "UPDATE employeestable SET employee_no = '"+employeeno+"',name = '"+name+"',pin = '"+pin+"',email = '"+email+"',address = '"+address+"'"
                        + ",city = '"+city+"',telephone_no = '"+tel+"',id_no = '"+idno+"',nssf_no = '"+nssf+"',nhif_no = '"+nhif+"',account_no = '"+accno+"',"
                        + "basic_salary = '"+basic+"',keen = '"+keen+"',keen_phoneno = '"+keenno+"' WHERE s = '1' AND employee_id = '"+employeeid+"'";
                manage.update(sql);
                String empl = "Employee "+txtEmployeeName.getText()+"";
                manage.loggedmessageupdate(empl);
                lbl_exception.setText("Employee Details Updated Successfully...");
                resetemployees();
            }
    }
    private void deleteemployee(){
        DialogEmployees.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                showdialogemployees();
            }else if (response == JOptionPane.YES_OPTION){
                manage = new Manage();
                String sql = "UPDATE employeestable SET S = '0' WHERE s = '1' AND employee_id = '"+employeeid+"'";
                manage.delete(sql);
                String name = "Employee "+txtEmployeeName.getText()+"";
                manage.loggedmessagedelete(name);
                resetemployees();
                lbl_exception.setText("Employee Deleted Successfully...");
                buttonAdd.setEnabled(true);
                buttonExit.setEnabled(true);
            }else if(response == JOptionPane.CLOSED_OPTION){

            }       
    }
    
    private void showdialogemployees(){
        DialogEmployees.setVisible(true);
        DialogEmployees.setSize(994, 267);
        DialogEmployees.setResizable(false);
        DialogEmployees.setLocationRelativeTo(jPanel1);
        DialogEmployees.setAlwaysOnTop(true);
        
        buttonAdd.setEnabled(false);
        buttonExit.setEnabled(false);
        buttonSaveEmployee.setEnabled(false);
        buttonUpdateEmployee.setEnabled(true);
        buttonDeleteEmployee.setEnabled(true);
    }
    
    //____________________________________________________________________________________________________________________________________________________________________
    //___________________________________________________________________________________________________________________________----payroll----------------------
    private void showdialogpayroll(){
        DialogPayroll.setVisible(true);
        DialogPayroll.setSize(1050, 228);
        DialogPayroll.setLocationRelativeTo(jPanel2);
        DialogPayroll.setResizable(false);
        DialogPayroll.setAlwaysOnTop(true);
        
        buttonAddPayroll.setEnabled(false);
        buttonExitPayroll.setEnabled(false);
        buttonSavePayroll.setEnabled(false);
        buttonUpdatePayroll.setEnabled(true);
        buttonDeletePayroll.setEnabled(true);
        
    }
    public void resetpayroll(){
        c.add(Calendar.YEAR, 0);
        chooserPayroll.getDateEditor().setDate(c.getTime());
        txtEmpNo.setText("");
        txtEmpName.setText("");
        txtEmpEmail.setText("");
        txtEmpBasic.setText("");
        txtTotalBenefits.setText("");
        txtGrossSalary.setText("");
        txtNssf.setText("");
        txtTaxableAmount.setText("");
        txtNhif.setText("");
        txtPaye.setText("");
        txtTotalDeductions.setText("");
        txtNetSalary.setText("");
        txtEmpNo.requestFocus();
        buttonAddPayroll.setEnabled(true);
        buttonExitPayroll.setEnabled(true);
        buttonTotalDeductions.setEnabled(false);
        buttonTotalBenefits.setEnabled(false);
        buttonPayslips.setEnabled(false);
        buttonSavePayroll.setEnabled(true);
        buttonUpdatePayroll.setEnabled(false);
        buttonDeletePayroll.setEnabled(false);
        generatepayment_no();
        loadpayroll();
    }
    
    private void generatepayment_no(){
        String monthname = (String)comboMonth.getSelectedItem();
        int monthint = 0;
        switch(monthname){
            case "January"  :  monthint = 1;
            break;
            case "February" :  monthint = 2;
            break;
            case "March"    :  monthint = 3;
            break;
            case "April"    :  monthint = 4;
            break;
            case "May"      :  monthint = 5;
            break;
            case "June"     :  monthint = 6;
            break;
            case "July"     :  monthint = 7;
            break;
            case "August"   :  monthint = 8;
            break;
            case "September":  monthint = 9; 
            break;
            case "October"  :  monthint = 10;
            break;
            case "November" :  monthint = 11;
            break;
            case "December" :  monthint = 12;
            break;
            default:
                System.out.println("Invalid Month");
        }
        String year = (String)comboYear.getSelectedItem();
        String employeeno = txtEmpNo.getText();
        paymentid = "P"+""+monthint+""+year+"/"+employeeno;
        expenseid = "EXP-"+year+""+monthint;
    }
     private void showdialogbenefits(){
         DialogBenefits.setVisible(true);
         DialogBenefits.setSize(636, 388);
         DialogBenefits.setAlwaysOnTop(true);
         DialogBenefits.setResizable(false);
         DialogBenefits.setLocationRelativeTo(jPanel2);
     }
     public void resetbenefits(){
        txtBenefit.setText("");
        txtBenefitAmount.setText("");
        txtBenefit.requestFocus();
        buttonTotalDeductions.setEnabled(false);
        buttonTotalBenefits.setEnabled(false);
        buttonSaveBenefits.setEnabled(true);
        buttonUpdateBenefits.setEnabled(false);
        buttonDeleteBenefits.setEnabled(false);
        loadBenefits();
     }
     private void loadBenefits(){
         manage = new Manage();
         String sql = "SELECT id AS 'id', name AS 'Benefit',amount AS 'Amount' FROM benefits_table WHERE s = '1' AND payroll_id = '"+paymentid+"'";
         manage.update_table(sql, tableBenefits);
     }
     private void savebenefits(){
         try{
             if(txtBenefit.getText().equals("")){
                 lbl_exception.setText("Please Enter Benefit...");
                 txtBenefit.requestFocus();
             }else if(txtBenefitAmount.getText().equals("")){
                 lbl_exception.setText("Please Enter Amount...");
                 txtBenefitAmount.requestFocus();
             }else{
                 String sql = "INSERT INTO benefits_table(name,amount,payroll_id,s)VALUES(?,?,?,?)";
                 pst = conn.prepareStatement(sql);
                 
                 pst.setString(1, txtBenefit.getText());
                 pst.setString(2, txtBenefitAmount.getText());
                 pst.setString(3, paymentid);
                 pst.setString(4, "1");
                 
                 pst.execute();
                 lbl_exception.setText("Benefit Saved Successfully...");
                 resetbenefits();
             }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" savebenefits");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void selectedrowbenefits(){
         try{
             buttonSaveBenefits.setEnabled(false);
             buttonUpdateBenefits.setEnabled(true);
             buttonDeleteBenefits.setEnabled(true);
             int row = tableBenefits.getSelectedRow();
             String table_click = tableBenefits.getValueAt(row, 0).toString();
             
             String sql = "SELECT * FROM benefits_table WHERE s = '1' AND id = '"+table_click+"'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    benefitid = rs.getString("id");
                    txtBenefit.setText(rs.getString("name"));
                    txtBenefitAmount.setText(rs.getString("amount"));
                    paymentid = rs.getString("payroll_id");
                }
             
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" selectedrowbenefits");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void updatebenefit(){
        if(txtBenefit.getText().equals("")){
            lbl_exception.setText("Please Enter Benefit...");
            txtBenefit.requestFocus();
        }else if(txtBenefitAmount.getText().equals("")){
            lbl_exception.setText("Please Enter Amount...");
            txtBenefitAmount.requestFocus();
        }else{
            manage = new Manage();
            String sql = "UPDATE benefits_table SET name = '"+txtBenefit.getText()+"',amount = '"+txtBenefitAmount.getText()+"' WHERE id = '"+benefitid+"'"
                    + " AND s = '1'";
            manage.update(sql);
            String name = "Benefit "+txtBenefit.getText()+"";
            manage.loggedmessageupdate(name);
            lbl_exception.setText("Benefit Updated Successfully...");
            totaldeductions();
            totalbenefits();
            resetbenefits();
        }
     }
     
     private void deletebenefit(){
        manage = new Manage();
        String sql = "DELETE FROM benefits_table WHERE id = '"+benefitid+"'";
        manage.delete(sql);
        String name = "Benefit "+txtBenefit.getText()+"";
        manage.loggedmessagedelete(name);
        lbl_exception.setText("Benefit Deleted Successfully...");
        totaldeductions();
        totalbenefits();
        resetbenefits();
     }
     private void totalbenefits(){
         try{
             String sql = "SELECT COALESCE(SUM(amount), 0) FROM benefits_table WHERE s = '1' AND payroll_id = ?";
             pst = conn.prepareStatement(sql);
             
             pst.setString(1, paymentid);
             rs = pst.executeQuery();
                if(rs.next()){
                    double total_benefits = rs.getDouble("COALESCE(SUM(amount), 0)");
                    txtTotalBenefits.setText(String.format("%.2f", total_benefits));
                    
                    String basic = txtEmpBasic.getText();
                    basic = basic.trim();
                    double basic_salary = Double.parseDouble(basic);
                    
                    txtGrossSalary.setText(String.format("%.2f", basic_salary + total_benefits));
                    
                    double gross = basic_salary + total_benefits;
                    double nssfrate = 0.06;
                    double tier1,tier2,nssf,nhif,a,b;
                        if (gross < 6000){
                            tier1 = 0;
                            tier2 = 0;
                            nssf = 0;

                        }else if( gross  == 6000){
                            tier1 = 6000 * nssfrate;
                            tier2 = 0;
                            nssf = tier1;
                        }else if(gross  > 6000 && gross  < 12000){
                            tier1 = 6000 * nssfrate;
                            a = gross  - 6000;
                            tier2 = a * nssfrate;
                            nssf = tier1 + tier2;
                        }else if (gross  == 12000){
                            tier1 = 6000 * nssfrate;
                            tier2 = 6000 * nssfrate;
                            nssf = tier1 + tier2;
                        }else if(gross  > 12000 && gross < 18000){
                            tier1 = 6000 * nssfrate;
                            b = (gross - 12000) * nssfrate;
                            tier2 = (6000 * nssfrate) + b;
                            nssf = tier1 + tier2;

                        }else{
                            tier1 = 6000 * nssfrate;
                            tier2 = (6000 * nssfrate) + (6000 * nssfrate);
                            nssf = tier1 + tier2;
                        }
                        
                        txtNssf.setText(String.format("%.2f",nssf));
                        double taxableAmount = gross - nssf;
                        txtTaxableAmount.setText(String.format("%.2f", taxableAmount));
                        
                        if (gross <= 5999){
                            nhif = 150;
                        }else if(gross >= 6000 && gross < 7999){
                            nhif = 300;

                        }else if(gross >= 8000 && gross < 11999){
                            nhif = 400;

                        }else if(gross >= 12000 && gross < 14999){
                            nhif = 500;
                        }else if(gross >= 15000 && gross < 19999){
                            nhif = 600;
                        }else if(gross >= 20000 && gross < 24999){
                            nhif = 750;
                        }else if(gross >= 25000 && gross < 29999){
                            nhif = 850;
                        }else if(gross >= 30000 && gross < 34999){
                            nhif = 900;
                        }else if(gross >= 35000 && gross < 39999){
                            nhif = 950;
                        }else if(gross >= 40000 && gross < 44999){
                            nhif = 1000;
                        }else if(gross >= 45000 && gross < 49999){
                            nhif = 1100;
                        }else if(gross >= 50000 && gross < 59999){
                            nhif = 1200;
                        }else if(gross >= 60000 && gross < 69999){
                            nhif = 1300;
                        }else if(gross >= 70000 && gross < 79999){
                            nhif = 1400;
                        }else if(gross >= 80000 && gross < 89999){
                            nhif = 1500;
                        }else if(gross >= 90000 && gross < 99999){
                            nhif = 1600;
                        }else{
                            nhif = 1700;
                        }
                        txtNhif.setText(String.format("%.2f", nhif));
                        
                        try{
                            String query = "SELECT * FROM  taxablerangestable";
                            pst = conn.prepareStatement(query);
                            rs = pst.executeQuery();
                                if(rs.next()){
                                    double Amount1 = rs.getDouble("amount1");
                                    double Amount2 = rs.getDouble("amount2");
                                    double Amount3 = rs.getDouble("amount3");
                                    double Amount4 = rs.getDouble("amount4");
                                    double relief = rs.getDouble("reliefAmount");
                                    double amount1rate = rs.getDouble("amount1Rate");
                                    double amount2rate = rs.getDouble("amount2Rate");
                                    double amount3rate = rs.getDouble("amount3Rate");
                                    double amount4rate = rs.getDouble("amount4Rate");
                                    double amount5rate = rs.getDouble("amount5Rate");
                                    double c,d,e,f,g,tax,paye;

                                    c = Amount1 * amount1rate;
                                    d = ((taxableAmount - Amount1) * amount2rate) + c;
                                    e = ((taxableAmount - Amount2) * amount3rate) + ((Amount2 -Amount1) * amount2rate) + c;
                                    f = ((taxableAmount - Amount3) * amount4rate) + ((Amount3 - Amount2) * amount3rate) + ((Amount2 -Amount1) * amount2rate) + c;
                                    g = ((taxableAmount - Amount4) * amount5rate) +((Amount4 - Amount3) * amount4rate) + ((Amount3 - Amount2) * amount3rate) + ((Amount2 -Amount1) * amount2rate) + c;

                                        if (taxableAmount >= 1 && taxableAmount < Amount1){
                                        tax = c;
                                    }else if(taxableAmount > Amount1 && taxableAmount <= Amount2){
                                        tax = d;
                                    }else if(taxableAmount > Amount2 && taxableAmount <= Amount3){
                                        tax = e;
                                    }else if(taxableAmount > Amount3 && taxableAmount <= Amount4){
                                        tax = f;
                                    }else{
                                        tax = g;
                                    }
                                    paye = tax - relief;
                                    txtPaye.setText(String.format("%.2f",paye));
                                    String totaldeds = txtTotalDeductions.getText();
                                    totaldeds = totaldeds.trim();
                                    double ttlded = Double.parseDouble(totaldeds);
                                    txtNetSalary.setText(String.format("%.2f",(gross)-(nssf + nhif + paye + ttlded)));
                                }
                                
                        }catch(Exception e){
                            
                        }finally{
                            try{
                                rs.close();
                                pst.close();
                            }catch(Exception e){
                                
                            }
                        }
                }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" totalbenefits");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void totaldeductions(){
         try{
             String sql = "SELECT COALESCE(SUM(amount), 0) FROM deductions_table WHERE s = '1' AND payroll_id = ?";
             pst = conn.prepareStatement(sql);
             
             pst.setString(1, paymentid);
             rs = pst.executeQuery();
                if(rs.next()){
                    txtTotalDeductions.setText(rs.getString("COALESCE(SUM(amount), 0)"));
                }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" totaldeductions");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void loadpayroll(){
         manage = new Manage();
         String sql = "SELECT payroll_table.payroll_id AS 'id',payroll_table.pay_date AS 'Pay Date',employeestable.name AS 'Employee',"
                 + "payroll_table.basic_salary AS 'Basic',payroll_table.total_benefits AS 'Benefits',payroll_table.gross_salary AS 'Gross',"
                 + "payroll_table.nssf AS 'NSSF',payroll_table.taxable_amount AS 'Taxable',payroll_table.paye AS 'PAYE',payroll_table.nhif AS 'NHIF',"
                 + "payroll_table.total_deductions AS 'Other Deductions',payroll_table.net_salary AS 'Net Pay' FROM payroll_table,employeestable "
                 + "WHERE employeestable.employee_id = payroll_table.employee_id AND payroll_table.s = '1' AND "
                 + "payroll_table.month = '"+(String)comboMonth.getSelectedItem()+"' AND payroll_table.year = '"+(String)comboYear.getSelectedItem()+"'";
         manage.update_table(sql, tablePayroll);
     }
     private void savepayroll(){
         try{
             if(txtEmpNo.getText().equals("")){
                 lbl_exception.setText("Please Enter Employee #...");
                 txtEmpNo.requestFocus();
             }else if(txtEmpBasic.getText().equals("")){
                 lbl_exception.setText("Please Enter the correct Employee #...");
                 txtEmpNo.requestFocus();
             }else{
                 String sql = "INSERT INTO payroll_table(payroll_id,pay_date,month,year,employee_id,basic_salary,total_benefits,total_deductions,gross_salary,"
                         + "taxable_amount,nssf,nhif,paye,net_salary,s)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                 pst = conn.prepareStatement(sql);
                
                 pst.setString(1, paymentid);
                 pst.setString(2, ((JTextField)chooserPayroll.getDateEditor().getUiComponent()).getText());
                 pst.setString(3, (String)comboMonth.getSelectedItem());
                 pst.setString(4, (String)comboYear.getSelectedItem());
                 pst.setInt(5, employeeid);
                 pst.setString(6, txtEmpBasic.getText());
                 pst.setString(7, txtTotalBenefits.getText());
                 pst.setString(8, txtTotalDeductions.getText());
                 pst.setString(9, txtGrossSalary.getText());
                 pst.setString(10, txtTaxableAmount.getText());
                 pst.setString(11, txtNssf.getText());
                 pst.setString(12, txtNhif.getText());
                 pst.setString(13, txtPaye.getText());
                 pst.setString(14, txtNetSalary.getText());
                 pst.setString(15, "1");
                 
                 pst.execute();
                 checkiftosave();
                 lbl_exception.setText("Payment Saved Successfully...");
                 resetpayroll();
             }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" savepayroll");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void checkiftosave(){
         try{
             String sql = "SELECT * FROM expensestable WHERE s = '1' AND expense_id = ?";
             pst = conn.prepareStatement(sql);
             pst.setString(1, expenseid);
             rs = pst.executeQuery();
                if(rs.next()){
                    double amount_expense = rs.getDouble("amount");
                    if(amount_expense > 0){
                        updatepayrollas_expense();
                    }else{
                        String query = "DELETE FROM expensestable WHERE expense_id = '"+expenseid+"'";
                        manage.delete(query);
                    }
                }else{
                    savepayrollas_expense();
                }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" checkiftosave");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void updatepayroll(){
         generatepayment_no();
         manage = new Manage(); 
         String sql = "UPDATE payroll_table SET pay_date = '"+((JTextField)chooserPayroll.getDateEditor().getUiComponent()).getText()+"',month = '"+(String)comboMonth.
                 getSelectedItem()+"',year = '"+(String)comboYear.getSelectedItem()+"',employee_id = '"+employeeid+"',basic_salary = '"+txtEmpBasic.getText()+"',"
                 + "total_deductions = '"+txtTotalDeductions.getText()+"',total_benefits = '"+txtTotalBenefits.getText()+"',gross_salary = '"+txtGrossSalary.getText()+"',"
                 + "taxable_amount = '"+txtTaxableAmount.getText()+"',nssf = '"+txtNssf.getText()+"',nhif = '"+txtNhif.getText()+"',paye = '"+txtPaye.getText()+"',"
                 + "net_salary = '"+txtNetSalary.getText()+"' WHERE s = '1' AND payroll_id = '"+paymentid+"'";
         manage.update(sql);
         checkiftosave();
         String name = "Employee "+txtEmpNo.getText()+"--"+txtEmpName.getText()+"";
         manage.loggedmessageupdate(name);
         lbl_exception.setText("Payment Updated Successfully...");
         resetpayroll();
         
     }
     private void deletepayroll(){
         generatepayment_no();
         manage = new Manage();
         String sql = "DELETE FROM payroll_table WHERE payroll_id = '"+paymentid+"'";
         manage.delete(sql);
         checkiftosave();
         String name = "Employee "+txtEmpNo.getText()+"--"+txtEmpName.getText()+"";
         manage.loggedmessagedelete(name);
         loadpayroll();
     }
     private void totalsalaries(){
         try{
             String sql = "SELECT COALESCE(SUM(gross_salary), 0) FROM payroll_table WHERE s = '1' AND month = '"+(String)comboMonth.getSelectedItem()+"' AND year"
                     + " = '"+(String)comboYear.getSelectedItem()+"'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    total_salaries = rs.getDouble("COALESCE(SUM(gross_salary), 0)");
                    
                }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" totalsalaries");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void savepayrollas_expense(){
         generatepayment_no();
         totalsalaries();
         try{
             String sql = "INSERT INTO expensestable(expense_id,expense_date,expense_name,mop,transactionNo,amount,s)VALUES(?,?,?,?,?,?,?)";
             pst = conn.prepareStatement(sql);
             pst.setString(1, expenseid);
             pst.setString(2, ((JTextField)chooserPayroll.getDateEditor().getUiComponent()).getText());
             pst.setString(3, "TOTAL SALARIES");
             pst.setString(4, "-");
             pst.setString(5, "-");
             pst.setDouble(6, total_salaries);
             pst.setString(7, "1");
             
             pst.execute();
             
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" savepayrollas_expense");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void updatepayrollas_expense(){
         try{
             generatepayment_no();
             totalsalaries();
             String sql = "UPDATE expensestable SET amount = '"+total_salaries+"' WHERE expense_id = '"+expenseid+"'";
             pst = conn.prepareStatement(sql);
             pst.execute();
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" updatepayrollas_expense");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     
 
     private void selectedrowpayroll(){
         buttonPayslips.setEnabled(true);
         buttonSavePayroll.setEnabled(false);
         buttonUpdatePayroll.setEnabled(true);
         buttonDeletePayroll.setEnabled(true);
         buttonTotalBenefits.setEnabled(true);
         buttonTotalDeductions.setEnabled(true);
         try{
             int row = tablePayroll.getSelectedRow();
             String table_click = tablePayroll.getValueAt(row, 0).toString();
             
             String sql = "SELECT * FROM payroll_table,employeestable WHERE payroll_table.employee_id = employeestable.employee_id AND payroll_table.s = '1' "
                     + "AND payroll_table.payroll_id = '"+table_click+"'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    paymentid = rs.getString("payroll_table.payroll_id");
                    txtEmpNo.setText(rs.getString("employeestable.employee_no"));
                    txtEmpName.setText(rs.getString("employeestable.name"));
                    txtEmpEmail.setText(rs.getString("employeestable.address"));
                    txtEmpBasic.setText(rs.getString("employeestable.basic_salary"));
                    txtTotalBenefits.setText(rs.getString("payroll_table.total_benefits"));
                    txtTotalDeductions.setText(rs.getString("payroll_table.total_deductions"));
                    txtGrossSalary.setText(rs.getString("payroll_table.gross_salary"));
                    txtNssf.setText(rs.getString("payroll_table.nssf"));
                    txtNhif.setText(rs.getString("payroll_table.nhif"));
                    txtTaxableAmount.setText(rs.getString("payroll_table.taxable_amount"));
                    txtPaye.setText(rs.getString("payroll_table.paye"));
                    txtNetSalary.setText(rs.getString("payroll_table.net_salary"));
                    employeeid = rs.getInt("payroll_table.employee_id");
                    txtNextKeen.setText(rs.getString("employeestable.keen"));
                }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null ,e+" selectedrowpayroll");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     //____________________________________________________________________________________________________________________________-----deductions------------------------
     private void showdialogdeductions(){
         DialogDeductions.setVisible(true);
         DialogDeductions.setSize(636, 388);
         DialogDeductions.setResizable(false);
         DialogDeductions.setAlwaysOnTop(true);
         DialogDeductions.setLocationRelativeTo(jPanel2);
     }
     public void resetdeductions(){
        txtDeduction.setText("");
        txtDeductionAmount.setText("");
        txtDeduction.requestFocus();
        buttonTotalDeductions.setEnabled(false);
        buttonTotalBenefits.setEnabled(false);
        buttonSaveDeduction.setEnabled(true);
        buttonUpdateDeduction.setEnabled(false);
        buttonDeleteDeduction.setEnabled(false);
        loadDeductions();
     }
     private void loadDeductions(){
         manage = new Manage();
         String sql = "SELECT id AS 'id', name AS 'Deduction',amount AS 'Amount' FROM deductions_table WHERE s = '1' AND payroll_id = '"+paymentid+"'";
         manage.update_table(sql, tableDeductions);
     }
     private void savedeductions(){
         try{
             if(txtDeduction.getText().equals("")){
                 lbl_exception.setText("Please Enter Deduction...");
                 txtDeduction.requestFocus();
             }else if(txtDeductionAmount.getText().equals("")){
                 lbl_exception.setText("Please Enter Amount...");
                 txtDeductionAmount.requestFocus();
             }else{
                 String sql = "INSERT INTO deductions_table(name,amount,payroll_id,s)VALUES(?,?,?,?)";
                 pst = conn.prepareStatement(sql);
                 
                 pst.setString(1, txtDeduction.getText());
                 pst.setString(2, txtDeductionAmount.getText());
                 pst.setString(3, paymentid);
                 pst.setString(4, "1");
                 
                 pst.execute();
                 lbl_exception.setText("Deduction Saved Successfully...");
                 resetdeductions();
             }
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" savedeductions");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void selectedrowdeductions(){
         try{
             buttonSaveDeduction.setEnabled(false);
             buttonUpdateDeduction.setEnabled(true);
             buttonDeleteDeduction.setEnabled(true);
             int row = tableDeductions.getSelectedRow();
             String table_click = tableDeductions.getValueAt(row, 0).toString();
             
             String sql = "SELECT * FROM deductions_table WHERE s = '1' AND id = '"+table_click+"'";
             pst = conn.prepareStatement(sql);
             rs = pst.executeQuery();
                if(rs.next()){
                    deductionid = rs.getString("id");
                    txtDeduction.setText(rs.getString("name"));
                    txtDeductionAmount.setText(rs.getString("amount"));
                    paymentid = rs.getString("payroll_id");
                }
             
         }catch(Exception e){
             JOptionPane.showMessageDialog(null, e+" selectedrowdeductions");
         }finally{
             try{
                 rs.close();
                 pst.close();
             }catch(Exception e){
                 
             }
         }
     }
     private void updatedeductions(){
        if(txtDeduction.getText().equals("")){
            lbl_exception.setText("Please Enter Deduction...");
            txtDeduction.requestFocus();
        }else if(txtDeductionAmount.getText().equals("")){
            lbl_exception.setText("Please Enter Amount...");
            txtDeductionAmount.requestFocus();
        }else{
            manage = new Manage();
            String sql = "UPDATE deductions_table SET name = '"+txtDeduction.getText()+"',amount = '"+txtDeductionAmount.getText()+"' WHERE id = '"+deductionid+"'"
                    + " AND s = '1'";
            manage.update(sql);
            String name = "Deduction "+txtDeduction.getText()+"";
            manage.loggedmessageupdate(name);
            lbl_exception.setText("Deduction Updated Successfully...");
            totaldeductions();
            totalbenefits();
            resetdeductions();
        }
     }
     private void deletedeductions(){
        manage = new Manage();
        String sql = "DELETE FROM deductions_table WHERE id = '"+deductionid+"'";
        manage.delete(sql);
        String name = "Deduction "+txtDeduction.getText()+"";
        manage.loggedmessagedelete(name);
        lbl_exception.setText("Deduction Deleted Successfully...");
        totaldeductions();
        totalbenefits();
        resetdeductions();
     }
//     ____________________________________________________________________________________________________________________________________________________________________
    //____________________________________________________________________________________________________________________________________________________________
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DialogEmployees = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtPinNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        txtEmployeeNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmailAddress = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtNssfNo = new javax.swing.JTextField();
        txtAccountNo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNhifNo = new javax.swing.JTextField();
        txtBasicSalary = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtKeenNo = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtIdNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTelephoneNo = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtNextKeen = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        buttonResetEmployee = new javax.swing.JButton();
        buttonexitEmployee = new javax.swing.JButton();
        buttonSaveEmployee = new javax.swing.JButton();
        buttonUpdateEmployee = new javax.swing.JButton();
        buttonDeleteEmployee = new javax.swing.JButton();
        DialogPayroll = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtEmpEmail = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtEmpName = new javax.swing.JTextField();
        txtEmpNo = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtEmpBasic = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        txtTotalDeductions = new javax.swing.JTextField();
        txtNetSalary = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtPaye = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        buttonTotalDeductions = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        txtNhif = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        txtTaxableAmount = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtGrossSalary = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtTotalBenefits = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtNssf = new javax.swing.JTextField();
        buttonTotalBenefits = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        buttonResetPayroll = new javax.swing.JButton();
        buttonexitPayroll = new javax.swing.JButton();
        buttonSavePayroll = new javax.swing.JButton();
        buttonUpdatePayroll = new javax.swing.JButton();
        buttonDeletePayroll = new javax.swing.JButton();
        buttonPayslips = new javax.swing.JButton();
        DialogBenefits = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableBenefits = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        buttonResetBenefits = new javax.swing.JButton();
        buttonexitBenefits = new javax.swing.JButton();
        buttonSaveBenefits = new javax.swing.JButton();
        buttonUpdateBenefits = new javax.swing.JButton();
        buttonDeleteBenefits = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtBenefit = new javax.swing.JTextField();
        txtBenefitAmount = new javax.swing.JTextField();
        DialogDeductions = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableDeductions = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        buttonResetDeduction = new javax.swing.JButton();
        buttonexitDeduction = new javax.swing.JButton();
        buttonSaveDeduction = new javax.swing.JButton();
        buttonUpdateDeduction = new javax.swing.JButton();
        buttonDeleteDeduction = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtDeduction = new javax.swing.JTextField();
        txtDeductionAmount = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        buttonExit = new javax.swing.JButton();
        buttonAdd = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmployees = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablePayroll = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        buttonExitPayroll = new javax.swing.JButton();
        buttonAddPayroll = new javax.swing.JButton();
        comboMonth = new javax.swing.JComboBox();
        comboYear = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        chooserPayroll = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        panelpayslip = new javax.swing.JPanel();
        lbl_exception = new javax.swing.JLabel();

        DialogEmployees.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogEmployees.setUndecorated(true);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("PIN #:");

        txtPinNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPinNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPinNoKeyReleased(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("Employee No:");

        txtEmployeeName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeNameKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmployeeNameKeyReleased(evt);
            }
        });

        txtEmployeeNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmployeeNoKeyReleased(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(0, 0, 153));
        jLabel4.setText("Email Address:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Employee Name:");

        txtEmailAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailAddressKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailAddressKeyReleased(evt);
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
                        .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPinNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmployeeNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEmployeeNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPinNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEmailAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        txtNssfNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNssfNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNssfNoKeyReleased(evt);
            }
        });

        txtAccountNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAccountNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAccountNoKeyReleased(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(0, 0, 153));
        jLabel9.setText("NHIF #:");

        jLabel12.setForeground(new java.awt.Color(0, 0, 153));
        jLabel12.setText("Basic Salary:");

        txtNhifNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNhifNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNhifNoKeyReleased(evt);
            }
        });

        txtBasicSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBasicSalaryKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBasicSalaryKeyPressed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 153));
        jLabel10.setText("NSSF #:");

        jLabel11.setForeground(new java.awt.Color(0, 0, 153));
        jLabel11.setText("A/c #:");

        jLabel30.setForeground(new java.awt.Color(0, 0, 153));
        jLabel30.setText("Next of Keen Tel #:");

        txtKeenNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKeenNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeenNoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKeenNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNssfNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNhifNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtKeenNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtNhifNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNssfNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        txtIdNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdNoKeyPressed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("City:");

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

        jLabel8.setForeground(new java.awt.Color(0, 0, 153));
        jLabel8.setText("ID #:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 153));
        jLabel5.setText("Address:");

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAddressKeyReleased(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("Telephone #:");

        txtTelephoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelephoneNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelephoneNoKeyPressed(evt);
            }
        });

        jLabel31.setForeground(new java.awt.Color(0, 0, 153));
        jLabel31.setText("Next of Keen:");

        txtNextKeen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNextKeenKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNextKeenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNextKeenKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNextKeen, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelephoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 17, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTelephoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtIdNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtNextKeen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonResetEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonResetEmployee.setText("Reset");
        buttonResetEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetEmployeeActionPerformed(evt);
            }
        });

        buttonexitEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexitEmployee.setText("Exit");
        buttonexitEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitEmployeeActionPerformed(evt);
            }
        });

        buttonSaveEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonSaveEmployee.setText("Save");
        buttonSaveEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveEmployeeActionPerformed(evt);
            }
        });

        buttonUpdateEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/update.png"))); // NOI18N
        buttonUpdateEmployee.setText("Update");
        buttonUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateEmployeeActionPerformed(evt);
            }
        });

        buttonDeleteEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
        buttonDeleteEmployee.setText("Delete");
        buttonDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonResetEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(buttonSaveEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(buttonUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonDeleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(buttonexitEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonResetEmployee)
                    .addComponent(buttonexitEmployee)
                    .addComponent(buttonSaveEmployee)
                    .addComponent(buttonUpdateEmployee)
                    .addComponent(buttonDeleteEmployee))
                .addContainerGap())
        );

        javax.swing.GroupLayout DialogEmployeesLayout = new javax.swing.GroupLayout(DialogEmployees.getContentPane());
        DialogEmployees.getContentPane().setLayout(DialogEmployeesLayout);
        DialogEmployeesLayout.setHorizontalGroup(
            DialogEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        DialogEmployeesLayout.setVerticalGroup(
            DialogEmployeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogEmployeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DialogPayroll.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogPayroll.setUndecorated(true);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel13.setForeground(new java.awt.Color(0, 0, 153));
        jLabel13.setText("Email Address:");

        txtEmpEmail.setEditable(false);
        txtEmpEmail.setBackground(new java.awt.Color(204, 204, 255));
        txtEmpEmail.setForeground(new java.awt.Color(102, 102, 102));
        txtEmpEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpEmailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpEmailKeyReleased(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(0, 0, 153));
        jLabel14.setText("Employee #:");

        txtEmpName.setEditable(false);
        txtEmpName.setBackground(new java.awt.Color(204, 204, 255));
        txtEmpName.setForeground(new java.awt.Color(102, 102, 102));
        txtEmpName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmpNameKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpNameKeyReleased(evt);
            }
        });

        txtEmpNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpNoActionPerformed(evt);
            }
        });
        txtEmpNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpNoKeyReleased(evt);
            }
        });

        jLabel18.setForeground(new java.awt.Color(0, 0, 153));
        jLabel18.setText("Basic Salary:");

        jLabel19.setForeground(new java.awt.Color(0, 0, 153));
        jLabel19.setText("Employee Name:");

        txtEmpBasic.setEditable(false);
        txtEmpBasic.setBackground(new java.awt.Color(204, 204, 255));
        txtEmpBasic.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtEmpBasic.setForeground(new java.awt.Color(102, 102, 102));
        txtEmpBasic.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpBasicKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmpBasicKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpBasic, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtEmpEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtEmpBasic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        txtTotalDeductions.setEditable(false);
        txtTotalDeductions.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalDeductions.setForeground(new java.awt.Color(102, 102, 102));
        txtTotalDeductions.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalDeductionsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTotalDeductionsKeyReleased(evt);
            }
        });

        txtNetSalary.setEditable(false);
        txtNetSalary.setBackground(new java.awt.Color(204, 204, 255));
        txtNetSalary.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtNetSalary.setForeground(new java.awt.Color(153, 0, 0));
        txtNetSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNetSalaryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNetSalaryKeyReleased(evt);
            }
        });

        jLabel20.setForeground(new java.awt.Color(0, 0, 153));
        jLabel20.setText("PAYE:");

        txtPaye.setEditable(false);
        txtPaye.setBackground(new java.awt.Color(204, 204, 255));
        txtPaye.setForeground(new java.awt.Color(102, 102, 102));
        txtPaye.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPayeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPayeKeyReleased(evt);
            }
        });

        jLabel23.setForeground(new java.awt.Color(0, 0, 153));
        jLabel23.setText("Net Salary:");

        buttonTotalDeductions.setText("Total Deductions");
        buttonTotalDeductions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTotalDeductionsActionPerformed(evt);
            }
        });

        jLabel28.setForeground(new java.awt.Color(0, 0, 153));
        jLabel28.setText("NHIF:");

        txtNhif.setEditable(false);
        txtNhif.setBackground(new java.awt.Color(204, 204, 255));
        txtNhif.setForeground(new java.awt.Color(102, 102, 102));
        txtNhif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNhifKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNhifKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNetSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPaye, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(buttonTotalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTotalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNhif, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNhif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtPaye, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonTotalDeductions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtNetSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        txtTaxableAmount.setEditable(false);
        txtTaxableAmount.setBackground(new java.awt.Color(204, 204, 255));
        txtTaxableAmount.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtTaxableAmount.setForeground(new java.awt.Color(102, 102, 102));
        txtTaxableAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTaxableAmountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxableAmountKeyPressed(evt);
            }
        });

        jLabel24.setForeground(new java.awt.Color(0, 0, 153));
        jLabel24.setText("Gross Salary:");

        txtGrossSalary.setEditable(false);
        txtGrossSalary.setBackground(new java.awt.Color(204, 204, 255));
        txtGrossSalary.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtGrossSalary.setForeground(new java.awt.Color(102, 102, 102));
        txtGrossSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGrossSalaryActionPerformed(evt);
            }
        });
        txtGrossSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGrossSalaryKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGrossSalaryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGrossSalaryKeyReleased(evt);
            }
        });

        jLabel25.setForeground(new java.awt.Color(0, 0, 153));
        jLabel25.setText("Taxable Amount:");

        txtTotalBenefits.setEditable(false);
        txtTotalBenefits.setBackground(new java.awt.Color(204, 204, 255));
        txtTotalBenefits.setForeground(new java.awt.Color(102, 102, 102));
        txtTotalBenefits.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalBenefitsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTotalBenefitsKeyReleased(evt);
            }
        });

        jLabel27.setForeground(new java.awt.Color(0, 0, 153));
        jLabel27.setText("NSSF:");

        txtNssf.setEditable(false);
        txtNssf.setBackground(new java.awt.Color(204, 204, 255));
        txtNssf.setForeground(new java.awt.Color(102, 102, 102));
        txtNssf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNssfKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNssfKeyPressed(evt);
            }
        });

        buttonTotalBenefits.setText("Total Benefits");
        buttonTotalBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTotalBenefitsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTaxableAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNssf, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGrossSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(buttonTotalBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotalBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonTotalBenefits))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtGrossSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtNssf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtTaxableAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonResetPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonResetPayroll.setText("Reset");
        buttonResetPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetPayrollActionPerformed(evt);
            }
        });

        buttonexitPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexitPayroll.setText("Exit");
        buttonexitPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitPayrollActionPerformed(evt);
            }
        });

        buttonSavePayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonSavePayroll.setText("Save");
        buttonSavePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSavePayrollActionPerformed(evt);
            }
        });

        buttonUpdatePayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/update.png"))); // NOI18N
        buttonUpdatePayroll.setText("Update");
        buttonUpdatePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdatePayrollActionPerformed(evt);
            }
        });

        buttonDeletePayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
        buttonDeletePayroll.setText("Delete");
        buttonDeletePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeletePayrollActionPerformed(evt);
            }
        });

        buttonPayslips.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/print.png"))); // NOI18N
        buttonPayslips.setText("Print Payslips");
        buttonPayslips.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPayslipsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonResetPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(buttonSavePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(buttonUpdatePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(buttonDeletePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonPayslips, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(buttonexitPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonResetPayroll)
                    .addComponent(buttonexitPayroll)
                    .addComponent(buttonSavePayroll)
                    .addComponent(buttonUpdatePayroll)
                    .addComponent(buttonDeletePayroll)
                    .addComponent(buttonPayslips, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout DialogPayrollLayout = new javax.swing.GroupLayout(DialogPayroll.getContentPane());
        DialogPayroll.getContentPane().setLayout(DialogPayrollLayout);
        DialogPayrollLayout.setHorizontalGroup(
            DialogPayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogPayrollLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DialogPayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        DialogPayrollLayout.setVerticalGroup(
            DialogPayrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogPayrollLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DialogBenefits.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogBenefits.setUndecorated(true);

        tableBenefits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableBenefits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBenefitsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tableBenefitsMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(tableBenefits);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonResetBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonResetBenefits.setText("Reset");
        buttonResetBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetBenefitsActionPerformed(evt);
            }
        });

        buttonexitBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexitBenefits.setText("Exit");
        buttonexitBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitBenefitsActionPerformed(evt);
            }
        });

        buttonSaveBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonSaveBenefits.setText("Save");
        buttonSaveBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveBenefitsActionPerformed(evt);
            }
        });

        buttonUpdateBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/update.png"))); // NOI18N
        buttonUpdateBenefits.setText("Update");
        buttonUpdateBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateBenefitsActionPerformed(evt);
            }
        });

        buttonDeleteBenefits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
        buttonDeleteBenefits.setText("Delete");
        buttonDeleteBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteBenefitsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonResetBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonSaveBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpdateBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(buttonDeleteBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonexitBenefits, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonResetBenefits)
                    .addComponent(buttonexitBenefits)
                    .addComponent(buttonSaveBenefits)
                    .addComponent(buttonUpdateBenefits)
                    .addComponent(buttonDeleteBenefits))
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jLabel21.setForeground(new java.awt.Color(0, 0, 204));
        jLabel21.setText("Benefit:");

        jLabel22.setForeground(new java.awt.Color(0, 0, 204));
        jLabel22.setText("Amount:");

        txtBenefit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBenefitKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBenefitKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBenefitKeyReleased(evt);
            }
        });

        txtBenefitAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBenefitAmountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBenefitAmountKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBenefitAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtBenefit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtBenefitAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DialogBenefitsLayout = new javax.swing.GroupLayout(DialogBenefits.getContentPane());
        DialogBenefits.getContentPane().setLayout(DialogBenefitsLayout);
        DialogBenefitsLayout.setHorizontalGroup(
            DialogBenefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogBenefitsLayout.createSequentialGroup()
                .addGroup(DialogBenefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(DialogBenefitsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(DialogBenefitsLayout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(DialogBenefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        DialogBenefitsLayout.setVerticalGroup(
            DialogBenefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogBenefitsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        DialogDeductions.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DialogDeductions.setUndecorated(true);

        tableDeductions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableDeductions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDeductionsMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tableDeductionsMouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(tableDeductions);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonResetDeduction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reset.png"))); // NOI18N
        buttonResetDeduction.setText("Reset");
        buttonResetDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetDeductionActionPerformed(evt);
            }
        });

        buttonexitDeduction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonexitDeduction.setText("Exit");
        buttonexitDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonexitDeductionActionPerformed(evt);
            }
        });

        buttonSaveDeduction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/save.png"))); // NOI18N
        buttonSaveDeduction.setText("Save");
        buttonSaveDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveDeductionActionPerformed(evt);
            }
        });

        buttonUpdateDeduction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/update.png"))); // NOI18N
        buttonUpdateDeduction.setText("Update");
        buttonUpdateDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateDeductionActionPerformed(evt);
            }
        });

        buttonDeleteDeduction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/trash.png"))); // NOI18N
        buttonDeleteDeduction.setText("Delete");
        buttonDeleteDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteDeductionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonResetDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonSaveDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUpdateDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(buttonDeleteDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonexitDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonResetDeduction)
                    .addComponent(buttonexitDeduction)
                    .addComponent(buttonSaveDeduction)
                    .addComponent(buttonUpdateDeduction)
                    .addComponent(buttonDeleteDeduction))
                .addContainerGap())
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jLabel26.setForeground(new java.awt.Color(0, 0, 204));
        jLabel26.setText("Deduction:");

        jLabel29.setForeground(new java.awt.Color(0, 0, 204));
        jLabel29.setText("Amount:");

        txtDeduction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDeductionKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeductionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDeductionKeyReleased(evt);
            }
        });

        txtDeductionAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDeductionAmountKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeductionAmountKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDeductionAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtDeduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtDeductionAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DialogDeductionsLayout = new javax.swing.GroupLayout(DialogDeductions.getContentPane());
        DialogDeductions.getContentPane().setLayout(DialogDeductionsLayout);
        DialogDeductionsLayout.setHorizontalGroup(
            DialogDeductionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogDeductionsLayout.createSequentialGroup()
                .addGroup(DialogDeductionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(DialogDeductionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(DialogDeductionsLayout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(DialogDeductionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        DialogDeductionsLayout.setVerticalGroup(
            DialogDeductionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DialogDeductionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        tableEmployees.setBackground(new java.awt.Color(255, 255, 204));
        tableEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeesMouseClicked(evt);
            }
        });
        tableEmployees.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableEmployeesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableEmployees);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Employees", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));

        tablePayroll.setBackground(new java.awt.Color(255, 255, 204));
        tablePayroll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablePayroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePayrollMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablePayroll);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        buttonExitPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/exit.png"))); // NOI18N
        buttonExitPayroll.setText("Exit");
        buttonExitPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitPayrollActionPerformed(evt);
            }
        });

        buttonAddPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/edit.png"))); // NOI18N
        buttonAddPayroll.setText("Add/Edit");
        buttonAddPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddPayrollActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(buttonAddPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 856, Short.MAX_VALUE)
                .addComponent(buttonExitPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonExitPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAddPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        comboMonth.setBackground(new java.awt.Color(255, 255, 255));
        comboMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        comboMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        comboMonth.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboMonthPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        comboYear.setBackground(new java.awt.Color(255, 255, 255));
        comboYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Year", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        comboYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        comboYear.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                comboYearPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel15.setForeground(new java.awt.Color(0, 0, 153));
        jLabel15.setText("Year:");

        jLabel16.setForeground(new java.awt.Color(0, 0, 153));
        jLabel16.setText("Month:");

        chooserPayroll.setDateFormatString("yyyy-MM-dd");

        jLabel17.setForeground(new java.awt.Color(0, 0, 153));
        jLabel17.setText("Date:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 468, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooserPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboYear, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(comboYear)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboMonth)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chooserPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Payroll", jPanel2);

        panelpayslip.setBackground(new java.awt.Color(255, 255, 255));
        panelpayslip.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));

        javax.swing.GroupLayout panelpayslipLayout = new javax.swing.GroupLayout(panelpayslip);
        panelpayslip.setLayout(panelpayslipLayout);
        panelpayslipLayout.setHorizontalGroup(
            panelpayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1131, Short.MAX_VALUE)
        );
        panelpayslipLayout.setVerticalGroup(
            panelpayslipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 454, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Load Payslip", panelpayslip);

        lbl_exception.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbl_exception.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(547, 547, 547)
                        .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_exception, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonResetEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetEmployeeActionPerformed
        resetemployees();
    }//GEN-LAST:event_buttonResetEmployeeActionPerformed

    private void buttonexitEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitEmployeeActionPerformed
        DialogEmployees.dispose();
        buttonAdd.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonSaveEmployee.setEnabled(true);
        buttonUpdateEmployee.setEnabled(false);
        buttonDeleteEmployee.setEnabled(false);
        lbl_exception.setText("");
    }//GEN-LAST:event_buttonexitEmployeeActionPerformed

    private void buttonSaveEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveEmployeeActionPerformed
        
        try{
            String sql = "SELECT * FROM employeestable WHERE s = '1' AND employee_no = ?";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtEmployeeNo.getText());
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_exception.setText("Employee Already Exists....");
                    resetemployees();
                }else{
                    saveemployee();
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
    }//GEN-LAST:event_buttonSaveEmployeeActionPerformed

    private void buttonUpdateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateEmployeeActionPerformed
        updateemployee();
    }//GEN-LAST:event_buttonUpdateEmployeeActionPerformed

    private void buttonDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteEmployeeActionPerformed
        deleteemployee();
    }//GEN-LAST:event_buttonDeleteEmployeeActionPerformed

    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
        DialogEmployees.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        showdialogemployees();
        resetemployees();
        buttonAdd.setEnabled(false);
        buttonExit.setEnabled(false);
        
    }//GEN-LAST:event_buttonAddActionPerformed

    private void txtBasicSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicSalaryKeyTyped
        char vchar = evt.getKeyChar();
      if((!(Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
      }
    }//GEN-LAST:event_txtBasicSalaryKeyTyped

    private void txtIdNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdNoKeyTyped
        char vchar = evt.getKeyChar();
      if((!(Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
      }
    }//GEN-LAST:event_txtIdNoKeyTyped

    private void txtTelephoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelephoneNoKeyTyped
        char vchar = evt.getKeyChar();
      if((!(Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
      }
    }//GEN-LAST:event_txtTelephoneNoKeyTyped

    private void txtEmployeeNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNameKeyTyped
        char vchar = evt.getKeyChar();
      if(((Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
      }
    }//GEN-LAST:event_txtEmployeeNameKeyTyped

    private void txtCityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyTyped
         char vchar = evt.getKeyChar();
      if(((Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
      }
    }//GEN-LAST:event_txtCityKeyTyped

    private void txtEmployeeNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNoKeyReleased
        txtEmployeeNo.setText(txtEmployeeNo.getText().toUpperCase());
    }//GEN-LAST:event_txtEmployeeNoKeyReleased

    private void txtEmployeeNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNameKeyReleased
        txtEmployeeName.setText(txtEmployeeName.getText().toUpperCase());
    }//GEN-LAST:event_txtEmployeeNameKeyReleased

    private void txtPinNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinNoKeyReleased
        txtPinNo.setText(txtPinNo.getText().toUpperCase());
    }//GEN-LAST:event_txtPinNoKeyReleased

    private void txtEmailAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyReleased
        txtEmailAddress.setText(txtEmailAddress.getText().toLowerCase());
    }//GEN-LAST:event_txtEmailAddressKeyReleased

    private void txtAddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyReleased
        txtAddress.setText(txtAddress.getText().toUpperCase());
    }//GEN-LAST:event_txtAddressKeyReleased

    private void txtCityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyReleased
        txtCity.setText(txtCity.getText().toUpperCase());
    }//GEN-LAST:event_txtCityKeyReleased

    private void txtNhifNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNhifNoKeyReleased
        txtNhifNo.setText(txtNhifNo.getText().toUpperCase());
    }//GEN-LAST:event_txtNhifNoKeyReleased

    private void txtNssfNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNssfNoKeyReleased
        txtNssfNo.setText(txtNssfNo.getText().toUpperCase());
    }//GEN-LAST:event_txtNssfNoKeyReleased

    private void txtAccountNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNoKeyReleased
        txtAccountNo.setText(txtAccountNo.getText().toUpperCase());
    }//GEN-LAST:event_txtAccountNoKeyReleased

    private void txtEmployeeNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmployeeNoKeyPressed

    private void txtEmployeeNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNameKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmployeeNameKeyPressed

    private void txtPinNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPinNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtPinNoKeyPressed

    private void txtEmailAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtEmailAddressKeyPressed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtCityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCityKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtCityKeyPressed

    private void txtTelephoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelephoneNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtTelephoneNoKeyPressed

    private void txtIdNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtIdNoKeyPressed

    private void txtNhifNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNhifNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtNhifNoKeyPressed

    private void txtNssfNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNssfNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtNssfNoKeyPressed

    private void txtAccountNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountNoKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtAccountNoKeyPressed

    private void txtBasicSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicSalaryKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtBasicSalaryKeyPressed

    private void tableEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeesMouseClicked
        showdialogemployees();
        selectedrowemployees();
    }//GEN-LAST:event_tableEmployeesMouseClicked

    private void tableEmployeesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableEmployeesKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrowemployees();
        }
    }//GEN-LAST:event_tableEmployeesKeyReleased

    private void buttonExitPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitPayrollActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonExitPayrollActionPerformed

    private void buttonAddPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddPayrollActionPerformed
        showdialogpayroll();
        resetbenefits();
        resetdeductions();
        resetpayroll();
    }//GEN-LAST:event_buttonAddPayrollActionPerformed

    private void comboMonthPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboMonthPopupMenuWillBecomeInvisible
        generatepayment_no();
    }//GEN-LAST:event_comboMonthPopupMenuWillBecomeInvisible

    private void comboYearPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comboYearPopupMenuWillBecomeInvisible
       
    }//GEN-LAST:event_comboYearPopupMenuWillBecomeInvisible

    private void txtEmpEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpEmailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpEmailKeyPressed

    private void txtEmpEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpEmailKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpEmailKeyReleased

    private void txtEmpNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpNameKeyTyped

    private void txtEmpNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpNameKeyPressed

    private void txtEmpNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNameKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpNameKeyReleased

    private void txtEmpNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNoKeyPressed
       lbl_exception.setText("");
    }//GEN-LAST:event_txtEmpNoKeyPressed

    private void txtEmpNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNoKeyReleased
        txtEmpNo.setText(txtEmpNo.getText().toUpperCase());
        try{
            String sql = "SELECT * FROM employeestable WHERE  employee_no = ? AND s = '1'";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtEmpNo.getText());
            rs = pst.executeQuery();
                if(rs.next()){
                    employeeid = rs.getInt("employee_id");
                    txtEmpName.setText(rs.getString("name"));
                    txtEmpEmail.setText(rs.getString("email"));
                    txtEmpBasic.setText(rs.getString("basic_salary"));
                    generatepayment_no();
                    buttonTotalDeductions.setEnabled(true);
                    buttonTotalBenefits.setEnabled(true);
                    
                    totaldeductions();
                    totalbenefits();
                    //calculation();
                    
                }else{
                    employeeid = 0;
                    txtEmpName.setText("");
                    txtEmpEmail.setText("");
                    txtEmpBasic.setText("");
                    txtTotalBenefits.setText("");
                    txtGrossSalary.setText("");
                    txtNssf.setText("");
                    txtTaxableAmount.setText("");
                    txtNhif.setText("");
                    txtPaye.setText("");
                    txtTotalDeductions.setText("");
                    txtNetSalary.setText("");
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
    }//GEN-LAST:event_txtEmpNoKeyReleased

    private void txtEmpBasicKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpBasicKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpBasicKeyPressed

    private void txtEmpBasicKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpBasicKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpBasicKeyReleased

    private void txtTotalDeductionsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalDeductionsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalDeductionsKeyPressed

    private void txtTotalDeductionsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalDeductionsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalDeductionsKeyReleased

    private void txtNetSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetSalaryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNetSalaryKeyPressed

    private void txtNetSalaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNetSalaryKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNetSalaryKeyReleased

    private void txtPayeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPayeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayeKeyPressed

    private void txtPayeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPayeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPayeKeyReleased

    private void txtTaxableAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxableAmountKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxableAmountKeyTyped

    private void txtTaxableAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxableAmountKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaxableAmountKeyPressed

    private void txtGrossSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossSalaryKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGrossSalaryKeyTyped

    private void txtGrossSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossSalaryKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGrossSalaryKeyPressed

    private void txtGrossSalaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrossSalaryKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGrossSalaryKeyReleased

    private void txtTotalBenefitsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalBenefitsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBenefitsKeyPressed

    private void txtTotalBenefitsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalBenefitsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBenefitsKeyReleased

    private void txtNssfKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNssfKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNssfKeyTyped

    private void txtNssfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNssfKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNssfKeyPressed

    private void buttonResetPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetPayrollActionPerformed
        resetpayroll();
    }//GEN-LAST:event_buttonResetPayrollActionPerformed

    private void buttonexitPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitPayrollActionPerformed
        DialogPayroll.dispose();
        resetpayroll();
        buttonAddPayroll.setEnabled(true);
        buttonExitPayroll.setEnabled(true);
        lbl_exception.setText("");
        resetpayroll();
    }//GEN-LAST:event_buttonexitPayrollActionPerformed

    private void buttonSavePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSavePayrollActionPerformed
       
      try{
          String sql = "SELECT payroll_id FROM payroll_table WHERE s = '1' AND payroll_id = ?";
          pst = conn.prepareStatement(sql);
          pst.setString(1, paymentid);
          rs = pst.executeQuery();
            if(rs.next()){
                lbl_exception.setText("Error! This Payment has already been done...");
                resetpayroll();
            }else{
                savepayroll();
            }
      }catch(Exception e){
          JOptionPane.showMessageDialog(null, e);
      }finally{
          try{
              
          }catch(Exception e){
              
          }
      }
       
    }//GEN-LAST:event_buttonSavePayrollActionPerformed

    private void buttonUpdatePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdatePayrollActionPerformed
        if(txtEmpNo.getText().equals("")){
                 lbl_exception.setText("Please Enter Employee #...");
                 txtEmpNo.requestFocus();
             }else if(txtEmpBasic.getText().equals("")){
                 lbl_exception.setText("Please Enter the correct Employee #...");
                 txtEmpNo.requestFocus();
             }else{
                 updatepayroll();
             }
    }//GEN-LAST:event_buttonUpdatePayrollActionPerformed

    private void buttonDeletePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeletePayrollActionPerformed
        deletepayroll();
    }//GEN-LAST:event_buttonDeletePayrollActionPerformed

    private void buttonTotalBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTotalBenefitsActionPerformed
        showdialogbenefits();
        loadBenefits();
        generatepayment_no();
        DialogPayroll.dispose();
        resetbenefits();
    }//GEN-LAST:event_buttonTotalBenefitsActionPerformed

    private void buttonTotalDeductionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTotalDeductionsActionPerformed
        showdialogdeductions();
        loadDeductions();
        generatepayment_no();
        DialogPayroll.dispose();
        resetdeductions();
    }//GEN-LAST:event_buttonTotalDeductionsActionPerformed

    private void txtNhifKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNhifKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNhifKeyTyped

    private void txtNhifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNhifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNhifKeyPressed

    private void buttonResetBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetBenefitsActionPerformed
        resetbenefits();
    }//GEN-LAST:event_buttonResetBenefitsActionPerformed

    private void buttonexitBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitBenefitsActionPerformed
        DialogBenefits.dispose();
        showdialogpayroll();
        buttonAdd.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonTotalDeductions.setEnabled(true);
        buttonTotalBenefits.setEnabled(true);
        lbl_exception.setText("");
        totaldeductions();
        totalbenefits();
    }//GEN-LAST:event_buttonexitBenefitsActionPerformed

    private void buttonSaveBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveBenefitsActionPerformed
        
        try{
            String benefit = txtBenefit.getText();
            String sql = "SELECT name FROM benefits_table WHERE s = '1' AND name = ? AND payroll_id = '"+paymentid+"'";
            pst = conn.prepareStatement(sql);
            pst.setString(1, benefit);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_exception.setText("Benefit Already Entered...");
                    resetbenefits();
                }else{
                    savebenefits();
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
    }//GEN-LAST:event_buttonSaveBenefitsActionPerformed

    private void buttonUpdateBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateBenefitsActionPerformed
        updatebenefit();
    }//GEN-LAST:event_buttonUpdateBenefitsActionPerformed

    private void buttonDeleteBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteBenefitsActionPerformed
        DialogBenefits.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                showdialogbenefits();
            }else if (response == JOptionPane.YES_OPTION){
                deletebenefit();
                showdialogbenefits();
            }else if(response == JOptionPane.CLOSED_OPTION){
                showdialogbenefits();     
            }       
               
    }//GEN-LAST:event_buttonDeleteBenefitsActionPerformed

    private void txtBenefitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitKeyReleased
        txtBenefit.setText(txtBenefit.getText().toUpperCase());
    }//GEN-LAST:event_txtBenefitKeyReleased

    private void tableBenefitsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBenefitsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tableBenefitsMouseEntered

    private void tableBenefitsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBenefitsMouseClicked
       selectedrowbenefits();
    }//GEN-LAST:event_tableBenefitsMouseClicked

    private void txtBenefitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitKeyTyped
      char vchar = evt.getKeyChar();
        if(((Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
          evt.consume();
        }
    }//GEN-LAST:event_txtBenefitKeyTyped

    private void txtBenefitAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitAmountKeyTyped
      char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
            ||(vchar == KeyEvent.VK_BACK_SPACE)

            || (vchar == KeyEvent.VK_DELETE)){
          evt.consume();
        }
    }//GEN-LAST:event_txtBenefitAmountKeyTyped

    private void txtBenefitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtBenefitKeyPressed

    private void txtBenefitAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBenefitAmountKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtBenefitAmountKeyPressed

    private void tableDeductionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDeductionsMouseClicked
        selectedrowdeductions();
    }//GEN-LAST:event_tableDeductionsMouseClicked

    private void tableDeductionsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDeductionsMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tableDeductionsMouseEntered

    private void buttonResetDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonResetDeductionActionPerformed

    private void buttonexitDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitDeductionActionPerformed
        DialogDeductions.dispose();
        showdialogpayroll();
        buttonAdd.setEnabled(true);
        buttonExit.setEnabled(true);
        buttonTotalDeductions.setEnabled(true);
        buttonTotalBenefits.setEnabled(true);
        lbl_exception.setText("");
        totaldeductions();
        totalbenefits();
    }//GEN-LAST:event_buttonexitDeductionActionPerformed

    private void buttonSaveDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveDeductionActionPerformed
        try{
            String deduction = txtDeduction.getText();
            String sql = "SELECT name FROM deductions_table WHERE s = '1' AND name = ? AND payroll_id = '"+paymentid+"'";
            pst = conn.prepareStatement(sql);
            pst.setString(1, deduction);
            rs = pst.executeQuery();
                if(rs.next()){
                    lbl_exception.setText("Deduction Already Entered...");
                    resetdeductions();
                }else{
                    savedeductions();
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
    }//GEN-LAST:event_buttonSaveDeductionActionPerformed

    private void buttonUpdateDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateDeductionActionPerformed
        updatedeductions();
    }//GEN-LAST:event_buttonUpdateDeductionActionPerformed

    private void buttonDeleteDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteDeductionActionPerformed
        
        DialogDeductions.dispose();
        JDialog.setDefaultLookAndFeelDecorated(true);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to continue?", "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION){
                showdialogdeductions();
            }else if (response == JOptionPane.YES_OPTION){
                deletedeductions();
                showdialogdeductions();
            }else if(response == JOptionPane.CLOSED_OPTION){
                showdialogdeductions();     
            }      
    }//GEN-LAST:event_buttonDeleteDeductionActionPerformed

    private void txtDeductionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeductionKeyTyped
       char vchar = evt.getKeyChar();
      if(((Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
    }
    }//GEN-LAST:event_txtDeductionKeyTyped

    private void txtDeductionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeductionKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtDeductionKeyPressed

    private void txtDeductionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeductionKeyReleased
        txtDeduction.setText(txtDeduction.getText().toUpperCase());
    }//GEN-LAST:event_txtDeductionKeyReleased

    private void txtDeductionAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeductionAmountKeyTyped
        char vchar = evt.getKeyChar();
      if((!(Character.isDigit(vchar)))
          ||(vchar == KeyEvent.VK_BACK_SPACE)
          
          || (vchar == KeyEvent.VK_DELETE)){
        evt.consume();
    }
    }//GEN-LAST:event_txtDeductionAmountKeyTyped

    private void txtDeductionAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeductionAmountKeyPressed
        lbl_exception.setText("");
    }//GEN-LAST:event_txtDeductionAmountKeyPressed

    private void txtGrossSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGrossSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGrossSalaryActionPerformed

    private void buttonPayslipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPayslipsActionPerformed
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
           @Override
           protected Void doInBackground() throws Exception {
        manage = new Manage();
        String sql = "SELECT companytable.company_name,companytable.location,companytable.address,companytable.city,companytable.phone_no,companytable.email,"
                + "employeestable.employee_no,employeestable.name AS 'emp_name',employeestable.address AS 'emp_address',employeestable.city AS 'emp_city',"
                + "employeestable.nssf_no,employeestable.nhif_no,payroll_table.payroll_id,payroll_table.nssf,payroll_table.nhif,payroll_table.total_deductions"
                + ",payroll_table.total_benefits,payroll_table.paye,payroll_table.basic_salary,payroll_table.gross_salary,payroll_table.taxable_amount,"
                + "payroll_table.net_salary,payroll_table.month,payroll_table.year FROM companytable,employeestable,payroll_table WHERE companytable.id = "
                + "employeestable.company_id AND employeestable.employee_id = payroll_table.employee_id AND employeestable.s = '1' AND payroll_table.payroll_id"
                + " = '"+paymentid+"'";
        String path = "Reports/payslip.jrxml";
        manage.report(sql, path, panelpayslip);
        DialogPayroll.dispose();
           return null;
           }
           
       };
       worker.execute();       
    }//GEN-LAST:event_buttonPayslipsActionPerformed

    private void tablePayrollMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePayrollMouseClicked
        showdialogpayroll();
        selectedrowpayroll();
    }//GEN-LAST:event_tablePayrollMouseClicked

    private void txtEmpNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpNoActionPerformed

    private void txtKeenNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeenNoKeyTyped
        char vchar = evt.getKeyChar();
        if((!(Character.isDigit(vchar)))
              ||(vchar == KeyEvent.VK_BACK_SPACE)

              || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txtKeenNoKeyTyped

    private void txtKeenNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeenNoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeenNoKeyPressed

    private void txtNextKeenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNextKeenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNextKeenKeyTyped

    private void txtNextKeenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNextKeenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNextKeenKeyPressed

    private void txtNextKeenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNextKeenKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNextKeenKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDialog DialogBenefits;
    public javax.swing.JDialog DialogDeductions;
    public javax.swing.JDialog DialogEmployees;
    public javax.swing.JDialog DialogPayroll;
    public javax.swing.JButton buttonAdd;
    public javax.swing.JButton buttonAddPayroll;
    private javax.swing.JButton buttonDeleteBenefits;
    private javax.swing.JButton buttonDeleteDeduction;
    private javax.swing.JButton buttonDeleteEmployee;
    private javax.swing.JButton buttonDeletePayroll;
    public javax.swing.JButton buttonExit;
    public javax.swing.JButton buttonExitPayroll;
    public javax.swing.JButton buttonPayslips;
    private javax.swing.JButton buttonResetBenefits;
    private javax.swing.JButton buttonResetDeduction;
    private javax.swing.JButton buttonResetEmployee;
    private javax.swing.JButton buttonResetPayroll;
    private javax.swing.JButton buttonSaveBenefits;
    private javax.swing.JButton buttonSaveDeduction;
    private javax.swing.JButton buttonSaveEmployee;
    private javax.swing.JButton buttonSavePayroll;
    private javax.swing.JButton buttonTotalBenefits;
    private javax.swing.JButton buttonTotalDeductions;
    private javax.swing.JButton buttonUpdateBenefits;
    private javax.swing.JButton buttonUpdateDeduction;
    private javax.swing.JButton buttonUpdateEmployee;
    private javax.swing.JButton buttonUpdatePayroll;
    private javax.swing.JButton buttonexitBenefits;
    private javax.swing.JButton buttonexitDeduction;
    private javax.swing.JButton buttonexitEmployee;
    private javax.swing.JButton buttonexitPayroll;
    private com.toedter.calendar.JDateChooser chooserPayroll;
    public javax.swing.JComboBox comboMonth;
    public javax.swing.JComboBox comboYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
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
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel lbl_exception;
    private javax.swing.JPanel panelpayslip;
    private javax.swing.JTable tableBenefits;
    private javax.swing.JTable tableDeductions;
    private javax.swing.JTable tableEmployees;
    private javax.swing.JTable tablePayroll;
    private javax.swing.JTextField txtAccountNo;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBasicSalary;
    private javax.swing.JTextField txtBenefit;
    private javax.swing.JTextField txtBenefitAmount;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtDeduction;
    private javax.swing.JTextField txtDeductionAmount;
    private javax.swing.JTextField txtEmailAddress;
    private javax.swing.JTextField txtEmpBasic;
    private javax.swing.JTextField txtEmpEmail;
    private javax.swing.JTextField txtEmpName;
    private javax.swing.JTextField txtEmpNo;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeeNo;
    private javax.swing.JTextField txtGrossSalary;
    private javax.swing.JTextField txtIdNo;
    private javax.swing.JTextField txtKeenNo;
    private javax.swing.JTextField txtNetSalary;
    private javax.swing.JTextField txtNextKeen;
    private javax.swing.JTextField txtNhif;
    private javax.swing.JTextField txtNhifNo;
    private javax.swing.JTextField txtNssf;
    private javax.swing.JTextField txtNssfNo;
    private javax.swing.JTextField txtPaye;
    private javax.swing.JTextField txtPinNo;
    private javax.swing.JTextField txtTaxableAmount;
    private javax.swing.JTextField txtTelephoneNo;
    private javax.swing.JTextField txtTotalBenefits;
    private javax.swing.JTextField txtTotalDeductions;
    // End of variables declaration//GEN-END:variables
}
