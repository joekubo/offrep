
package offrep;

import com.tolclin.manage.Manage;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


public class InternalFrame extends javax.swing.JPanel {
     public String loggedusertype;
     GridBagLayout layout = new GridBagLayout();
     CompanyPanel company;
     UsersPanel users;
     ClientsPanel clients;
     QuotationPanel quotation;
     InvoicesPanel invoices;
     PaymentsPanel payments;
     ExpensesPanel expenses;
     ReportPanel report;
     ReminderPanel reminder;
     AboutPanel about;
     Manage manage = new Manage();
    public InternalFrame() {
        
        initComponents();
        allbuttonsdefaultcolor();
        
        company = new CompanyPanel();
        users = new UsersPanel();
        clients = new ClientsPanel();
        quotation = new QuotationPanel();
        invoices = new InvoicesPanel();
        payments = new PaymentsPanel();
        expenses = new ExpensesPanel();
        report = new ReportPanel();
        reminder = new ReminderPanel();
        about = new AboutPanel();
        DynamicPanel.setLayout(layout);
        GridBagConstraints c = new  GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        
        DynamicPanel.add(company,c);
        DynamicPanel.add(users,c);
        DynamicPanel.add(clients,c);
        DynamicPanel.add(quotation,c);
        DynamicPanel.add(invoices,c);
        DynamicPanel.add(payments,c);
        DynamicPanel.add(expenses,c);
        DynamicPanel.add(report,c);
        DynamicPanel.add(reminder, c);
        DynamicPanel.add(about, c);
        
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
       
    }

    public void closealldialogs(){
        company.DialogCompany.dispose();
        users.DialogUsers.dispose();
        clients.DialogClients.dispose();
        quotation.DialogQuotations.dispose();
        invoices.DialogInvoices.dispose();
        expenses.DialogExpenses.dispose();
        payments.DialogPayments.dispose();
        expenses.DialogExpenses.dispose();
        invoices.DialogEditInvoice.dispose();
    }
    private void allbuttonsdefaultcolor(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
    }
    public void company(){
        buttonCompany.setBackground(Color.lightGray);
        buttonCompany.setForeground(Color.RED);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(true);
        company.setVisible(true);
        company.reset();
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    public void users(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.lightGray);
        buttonUsers.setForeground(Color.RED);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(true);
        users.reset();
        users.buttonAdd.setEnabled(true);
        users.buttonexit.setEnabled(true);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    public void clients(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.lightGray);
        buttonClients.setForeground(Color.RED);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(true);
        clients.buttonAdd.setEnabled(true);
        clients.buttonexit.setEnabled(true);
        clients.reset();
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        
        closealldialogs();
    }
    public void quotation(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.lightGray);
        buttonQuotation.setForeground(Color.RED);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(true);
        quotation.setVisible(true);
        quotation.generateQuotation_no();
        quotation.reset();
        invoices.resetInfo();
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    
    public void invoices(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.lightGray);
        buttonInvoices.setForeground(Color.RED);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(true);
        invoices.generateInvoiceno();
        invoices.reset();
        invoices.resetInfo();
        invoices.jTabbedPane2.setEnabled(true);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    public void payments(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.lightGray);
        buttonPayments.setForeground(Color.RED);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(true);
        payments.reset();
        payments.buttonAdd.setEnabled(true);
        payments.buttonExit.setEnabled(true);
        payments.priviledge();
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    public void expenses(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.lightGray);
        buttonExpenses.setForeground(Color.RED);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(true);
        expenses.buttonAdd.setEnabled(true);
        expenses.buttonexit.setEnabled(true);
        expenses.loadtable();
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(false);
        closealldialogs();
    }
    public void reports(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.lightGray);
        buttonReports.setForeground(Color.RED);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(true);
        reminder.setVisible(false);
        report.loadExpenses();
        report.reset();
        about.setVisible(false);
        closealldialogs();
    }
    public void reminder(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.lightGray);
        buttonReminder.setForeground(Color.RED);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(true);
        reminder.reset();
        about.setVisible(false);
        closealldialogs();
    }
    private void about(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonReminder.setBackground(Color.WHITE);
        buttonReminder.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        clients.setVisible(false);
        quotation.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        reminder.setVisible(false);
        about.setVisible(true);
    }
    public void priviledges(){
        if(loggedusertype.equals("User")){
            buttonCompany.setVisible(false);
            buttonUsers.setVisible(false);
            buttonReports.setVisible(false);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        buttonCompany = new javax.swing.JButton();
        buttonUsers = new javax.swing.JButton();
        buttonInvoices = new javax.swing.JButton();
        buttonPayments = new javax.swing.JButton();
        buttonExpenses = new javax.swing.JButton();
        buttonReports = new javax.swing.JButton();
        buttonQuotation = new javax.swing.JButton();
        buttonLogs1 = new javax.swing.JButton();
        buttonReminder = new javax.swing.JButton();
        buttonClients = new javax.swing.JButton();
        DynamicPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1366, 780));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        buttonCompany.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/company.png"))); // NOI18N
        buttonCompany.setText("Company");
        buttonCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCompanyActionPerformed(evt);
            }
        });

        buttonUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/users.png"))); // NOI18N
        buttonUsers.setText("    Users");
        buttonUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUsersActionPerformed(evt);
            }
        });

        buttonInvoices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/invoice.png"))); // NOI18N
        buttonInvoices.setText(" Invoices");
        buttonInvoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInvoicesActionPerformed(evt);
            }
        });

        buttonPayments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/payments.png"))); // NOI18N
        buttonPayments.setText("Payments");
        buttonPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPaymentsActionPerformed(evt);
            }
        });

        buttonExpenses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/expenses.png"))); // NOI18N
        buttonExpenses.setText("Expenses");
        buttonExpenses.setToolTipText("");
        buttonExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExpensesActionPerformed(evt);
            }
        });

        buttonReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reports.png"))); // NOI18N
        buttonReports.setText("  Reports");
        buttonReports.setToolTipText("");
        buttonReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReportsActionPerformed(evt);
            }
        });

        buttonQuotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/quotation.png"))); // NOI18N
        buttonQuotation.setText("   Quote");
        buttonQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuotationActionPerformed(evt);
            }
        });

        buttonLogs1.setBackground(new java.awt.Color(51, 51, 255));
        buttonLogs1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        buttonLogs1.setForeground(new java.awt.Color(255, 255, 255));
        buttonLogs1.setText("About Us");
        buttonLogs1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLogs1ActionPerformed(evt);
            }
        });

        buttonReminder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/reminder.png"))); // NOI18N
        buttonReminder.setText("  Reminder");
        buttonReminder.setToolTipText("");
        buttonReminder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReminderActionPerformed(evt);
            }
        });

        buttonClients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/offrep/icons/client.png"))); // NOI18N
        buttonClients.setText("  Clients");
        buttonClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClientsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPayments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonInvoices, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonCompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonExpenses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonReports, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonQuotation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonLogs1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(buttonReminder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonClients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(buttonCompany)
                .addGap(2, 2, 2)
                .addComponent(buttonUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonQuotation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonInvoices, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonReports, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonReminder, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonLogs1)
                .addContainerGap(300, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        DynamicPanel.setBackground(new java.awt.Color(214, 193, 240));
        DynamicPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        javax.swing.GroupLayout DynamicPanelLayout = new javax.swing.GroupLayout(DynamicPanel);
        DynamicPanel.setLayout(DynamicPanelLayout);
        DynamicPanelLayout.setHorizontalGroup(
            DynamicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1210, Short.MAX_VALUE)
        );
        DynamicPanelLayout.setVerticalGroup(
            DynamicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        jPanel1.add(DynamicPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonInvoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInvoicesActionPerformed
        invoices();
    }//GEN-LAST:event_buttonInvoicesActionPerformed

    private void buttonPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPaymentsActionPerformed
        payments();
    }//GEN-LAST:event_buttonPaymentsActionPerformed

    private void buttonUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUsersActionPerformed
        users();
    }//GEN-LAST:event_buttonUsersActionPerformed

    private void buttonCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCompanyActionPerformed
        company();
    }//GEN-LAST:event_buttonCompanyActionPerformed

    private void buttonExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExpensesActionPerformed
       expenses();
    }//GEN-LAST:event_buttonExpensesActionPerformed

    private void buttonReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportsActionPerformed
        reports();
    }//GEN-LAST:event_buttonReportsActionPerformed

    private void buttonQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuotationActionPerformed
        quotation();
    }//GEN-LAST:event_buttonQuotationActionPerformed

    private void buttonLogs1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLogs1ActionPerformed
        about();
    }//GEN-LAST:event_buttonLogs1ActionPerformed

    private void buttonReminderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReminderActionPerformed
        reminder();
    }//GEN-LAST:event_buttonReminderActionPerformed

    private void buttonClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClientsActionPerformed
        clients();
    }//GEN-LAST:event_buttonClientsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DynamicPanel;
    public javax.swing.JButton buttonClients;
    public javax.swing.JButton buttonCompany;
    public javax.swing.JButton buttonExpenses;
    public javax.swing.JButton buttonInvoices;
    public javax.swing.JButton buttonLogs1;
    public javax.swing.JButton buttonPayments;
    public javax.swing.JButton buttonQuotation;
    public javax.swing.JButton buttonReminder;
    public javax.swing.JButton buttonReports;
    public javax.swing.JButton buttonUsers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
