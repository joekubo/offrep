
package offrep;
import com.tolclin.manage.Manage;
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

public class ReminderPanel extends javax.swing.JPanel {
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    Calendar c = Calendar.getInstance();
    
    private int id;
    public String loggeduserid;
    Manage manage = new Manage();
    public ReminderPanel() {
        initComponents();
        conn = Manage.ConnecrDb();
    }

    private void load_table(){
        String sql = "SELECT id,final_date AS 'Reminder Date',details AS 'Details' FROM reminder_table WHERE s = '1'";
        manage.update_table(sql, tableReminder);
    }
    public void reset(){
        c.add(Calendar.YEAR, 0);
        chooserDate.getDateEditor().setDate(c.getTime());
        txtReminder.setText("");
        txtReminder.requestFocus();
        load_table();
        buttonsave.setEnabled(true);
        buttonupdate.setEnabled(false);
        buttondelete.setEnabled(false);
    }
    private void save(){
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE,0);
            String initial_date = df.format(cal.getTime());
            
            String sql = "INSERT INTO reminder_table(date,details,final_date,s,user_id)VALUES(?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, initial_date);
            pst.setString(2, txtReminder.getText());
            pst.setString(3, ((JTextField)chooserDate.getDateEditor().getUiComponent()).getText());
            pst.setString(4, "1");
            pst.setString(5, loggeduserid);
            
            pst.execute();
            JOptionPane.showMessageDialog(null, "Reminder Saved Successfully...");
            reset();
            
        }catch(Exception e){
            System.out.println(e+" reminderpanel.save");
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
            int row = tableReminder.getSelectedRow();
            String table_click = tableReminder.getValueAt(row, 0).toString();
            String sql = "SELECT * FROM reminder_table WHERE s = '1' AND id = '"+table_click+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                if(rs.next()){
                    buttonsave.setEnabled(false);
                    buttonupdate.setEnabled(true);
                    buttondelete.setEnabled(true);
                    id = rs.getInt("id");
                    ((JTextField)chooserDate.getDateEditor().getUiComponent()).setText(rs.getString("final_date"));
                    txtReminder.setText(rs.getString("details"));
                }
        }catch(Exception e){
            System.out.println(e+" reminderPanel.selectedrow");
        }finally{
            try{
               rs.close();
               pst.close();
            }catch(Exception e){
                
            }
        }
    }
    private void update(){
        try{
            String sql = "UPDATE reminder_table SET details = ?, final_date = ? WHERE id = '"+id+"' AND s = '1'";
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtReminder.getText());
            pst.setString(2, ((JTextField)chooserDate.getDateEditor().getUiComponent()).getText());
            
            pst.execute();
            String name = "Reminder "+txtReminder.getText()+"";
            JOptionPane.showMessageDialog(null, "Reminder Updated Successfully...");
            reset();
            
        }catch(Exception e){
            System.out.println(e+" reminderpanel.update");
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtReminder = new javax.swing.JTextArea();
        chooserDate = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        buttonreset = new javax.swing.JButton();
        buttonexit = new javax.swing.JButton();
        buttonsave = new javax.swing.JButton();
        buttondelete = new javax.swing.JButton();
        buttonupdate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableReminder = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Date to be Prapared:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Details of Reminder:");

        txtReminder.setColumns(20);
        txtReminder.setRows(5);
        jScrollPane1.setViewportView(txtReminder);

        chooserDate.setDateFormatString("yyyy-MM-dd");

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

        buttonupdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/save.png"))); // NOI18N
        buttonupdate.setText("Update");
        buttonupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonupdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonreset, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonsave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttondelete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonexit, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonreset)
                    .addComponent(buttonexit)
                    .addComponent(buttonsave)
                    .addComponent(buttondelete)
                    .addComponent(buttonupdate))
                .addContainerGap())
        );

        tableReminder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableReminder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableReminderMouseClicked(evt);
            }
        });
        tableReminder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableReminderKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tableReminder);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chooserDate, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chooserDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonresetActionPerformed
        reset();
    }//GEN-LAST:event_buttonresetActionPerformed

    private void buttonexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonexitActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_buttonexitActionPerformed

    private void buttonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveActionPerformed
        save();
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void buttondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteActionPerformed
        String sql = "DELETE FROM reminder_table WHERE id = '"+id+"'";
        manage.delete(sql);
        
        String name = "Reminder "+txtReminder.getText()+"";
        
        JOptionPane.showMessageDialog(null, "Reminder Deleted Successfully...");
        reset();
    }//GEN-LAST:event_buttondeleteActionPerformed

    private void buttonupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonupdateActionPerformed
        update();
    }//GEN-LAST:event_buttonupdateActionPerformed

    private void tableReminderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableReminderMouseClicked
        selectedrow();
    }//GEN-LAST:event_tableReminderMouseClicked

    private void tableReminderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableReminderKeyReleased
        if (evt.getKeyCode()==KeyEvent.VK_DOWN || evt.getKeyCode()==KeyEvent.VK_UP){
            selectedrow();
        }
    }//GEN-LAST:event_tableReminderKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttondelete;
    private javax.swing.JButton buttonexit;
    private javax.swing.JButton buttonreset;
    private javax.swing.JButton buttonsave;
    private javax.swing.JButton buttonupdate;
    private com.toedter.calendar.JDateChooser chooserDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableReminder;
    private javax.swing.JTextArea txtReminder;
    // End of variables declaration//GEN-END:variables
}
