
package security;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


public class InternalFrame extends javax.swing.JPanel {
     GridBagLayout layout = new GridBagLayout();
     CompanyPanel company;
     UsersPanel users;
     ServicesPanel services;
     QuotationPanel quotation;
     ClientsPanel clients;
     InvoicesPanel invoices;
     PaymentsPanel payments;
     ExpensesPanel expenses;
     ReportPanel report;
     LogsPanel logs;
     Manage manage;
     PayrollPanel payroll;
    public InternalFrame() {
        
        initComponents();
        allbuttonsdefaultcolor();
        
        company = new CompanyPanel();
        users = new UsersPanel();
        services = new ServicesPanel();
        quotation = new QuotationPanel();
        clients = new ClientsPanel();
        invoices = new InvoicesPanel();
        payments = new PaymentsPanel();
        expenses = new ExpensesPanel();
        report = new ReportPanel();
        logs = new LogsPanel();
        manage = new Manage();
        payroll = new PayrollPanel();
        DynamicPanel.setLayout(layout);
        GridBagConstraints c = new  GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        
        DynamicPanel.add(company,c);
        DynamicPanel.add(users,c);
        DynamicPanel.add(services,c);
        DynamicPanel.add(quotation,c);
        DynamicPanel.add(clients,c);
        DynamicPanel.add(invoices,c);
        DynamicPanel.add(payments,c);
        DynamicPanel.add(expenses,c);
        DynamicPanel.add(report,c);
        DynamicPanel.add(payroll, c);
        DynamicPanel.add(logs,c);
        
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
    }

    public void closealldialogs(){
        users.DialogUsers.dispose();
        clients.DialogClients.dispose();
        quotation.DialogQuotations.dispose();
        invoices.DialogInvoices.dispose();
        expenses.DialogExpenses.dispose();
        payments.DialogPayments.dispose();
        expenses.DialogExpenses.dispose();
        payroll.DialogEmployees.dispose();
        payroll.DialogPayroll.dispose();
        payroll.DialogBenefits.dispose();
    }
    public void allbuttonsdefaultcolor(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
    }
    public void company(){
        buttonCompany.setBackground(Color.lightGray);
        buttonCompany.setForeground(Color.RED);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(true);
        company.setVisible(true);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void users(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.lightGray);
        buttonUsers.setForeground(Color.RED);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(true);
        users.reset();
        users.buttonAdd.setEnabled(true);
        users.buttonexit.setEnabled(true);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void services(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.lightGray);
        buttonServices.setForeground(Color.RED);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(true);
        services.reset();
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void quotation(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.lightGray);
        buttonQuotation.setForeground(Color.RED);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(true);
        quotation.setVisible(true);
        quotation.generateQuotation_no();
        quotation.reset();
        invoices.resetInfo();
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void clients(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.lightGray);
        buttonClients.setForeground(Color.RED);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(true);
        clients.reset();
        clients.buttonAdd.setEnabled(true);
        clients.buttonexit.setEnabled(true);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void invoices(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.lightGray);
        buttonInvoices.setForeground(Color.RED);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(true);
        invoices.generateInvoiceno();
        invoices.reset();
        invoices.resetInfo();
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void payments(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.lightGray);
        buttonPayments.setForeground(Color.RED);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(true);
        payments.reset();
        payments.buttonAdd.setEnabled(true);
        payments.buttonExit.setEnabled(true);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void expenses(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.lightGray);
        buttonExpenses.setForeground(Color.RED);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(true);
        expenses.buttonAdd.setEnabled(true);
        expenses.buttonexit.setEnabled(true);
        expenses.loadtable();
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void reports(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.lightGray);
        buttonReports.setForeground(Color.RED);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(true);
        report.loadExpenses();
        report.reset();
        payroll.setVisible(false);
        logs.setVisible(false);
        closealldialogs();
    }
    public void payroll(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.WHITE);
        buttonLogs.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.lightGray);
        buttonPayroll.setForeground(Color.RED);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(true);
        payroll.buttonAdd.setEnabled(true);
        payroll.buttonExit.setEnabled(true);
        payroll.loadEmployees();
        payroll.monthNyear();
        payroll.resetpayroll();
        logs.setVisible(false);
        closealldialogs();
    }
    public void logs(){
        buttonCompany.setBackground(Color.WHITE);
        buttonCompany.setForeground(Color.BLUE);
        buttonUsers.setBackground(Color.WHITE);
        buttonUsers.setForeground(Color.BLUE);
        buttonServices.setBackground(Color.WHITE);
        buttonServices.setForeground(Color.BLUE);
        buttonQuotation.setBackground(Color.WHITE);
        buttonQuotation.setForeground(Color.BLUE);
        buttonClients.setBackground(Color.WHITE);
        buttonClients.setForeground(Color.BLUE);
        buttonInvoices.setBackground(Color.WHITE);
        buttonInvoices.setForeground(Color.BLUE);
        buttonPayments.setBackground(Color.WHITE);
        buttonPayments.setForeground(Color.BLUE);
        buttonExpenses.setBackground(Color.WHITE);
        buttonExpenses.setForeground(Color.BLUE);
        buttonReports.setBackground(Color.WHITE);
        buttonReports.setForeground(Color.BLUE);
        buttonPayroll.setBackground(Color.WHITE);
        buttonPayroll.setForeground(Color.BLUE);
        buttonLogs.setBackground(Color.lightGray);
        buttonLogs.setForeground(Color.RED);
        company.setVisible(false);
        users.setVisible(false);
        services.setVisible(false);
        quotation.setVisible(false);
        clients.setVisible(false);
        invoices.setVisible(false);
        payments.setVisible(false);
        expenses.setVisible(false);
        report.setVisible(false);
        payroll.setVisible(false);
        logs.setVisible(true);
        logs.loadtable();
        closealldialogs();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        buttonCompany = new javax.swing.JButton();
        buttonUsers = new javax.swing.JButton();
        buttonServices = new javax.swing.JButton();
        buttonClients = new javax.swing.JButton();
        buttonInvoices = new javax.swing.JButton();
        buttonPayments = new javax.swing.JButton();
        buttonLogs = new javax.swing.JButton();
        buttonExpenses = new javax.swing.JButton();
        buttonReports = new javax.swing.JButton();
        buttonPayroll = new javax.swing.JButton();
        buttonQuotation = new javax.swing.JButton();
        DynamicPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1366, 780));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        buttonCompany.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/company.png"))); // NOI18N
        buttonCompany.setText("Company");
        buttonCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCompanyActionPerformed(evt);
            }
        });

        buttonUsers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/users.png"))); // NOI18N
        buttonUsers.setText("    Users");
        buttonUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUsersActionPerformed(evt);
            }
        });

        buttonServices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/service.png"))); // NOI18N
        buttonServices.setText("Services");
        buttonServices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonServicesActionPerformed(evt);
            }
        });

        buttonClients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/client.png"))); // NOI18N
        buttonClients.setText("  Clients");
        buttonClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClientsActionPerformed(evt);
            }
        });

        buttonInvoices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/invoice.png"))); // NOI18N
        buttonInvoices.setText(" Invoices");
        buttonInvoices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInvoicesActionPerformed(evt);
            }
        });

        buttonPayments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/payments.png"))); // NOI18N
        buttonPayments.setText("Payments");
        buttonPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPaymentsActionPerformed(evt);
            }
        });

        buttonLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/logs.png"))); // NOI18N
        buttonLogs.setText("       Logs");
        buttonLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLogsActionPerformed(evt);
            }
        });

        buttonExpenses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/expenses.png"))); // NOI18N
        buttonExpenses.setText("Expenses");
        buttonExpenses.setToolTipText("");
        buttonExpenses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExpensesActionPerformed(evt);
            }
        });

        buttonReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/reports.png"))); // NOI18N
        buttonReports.setText("Reports");
        buttonReports.setToolTipText("");
        buttonReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonReportsActionPerformed(evt);
            }
        });

        buttonPayroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/payroll.png"))); // NOI18N
        buttonPayroll.setText("Payroll");
        buttonPayroll.setToolTipText("");
        buttonPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPayrollActionPerformed(evt);
            }
        });

        buttonQuotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/security/icons/quotation.png"))); // NOI18N
        buttonQuotation.setText("   Quote");
        buttonQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuotationActionPerformed(evt);
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
                    .addComponent(buttonClients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonServices, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonCompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonLogs, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(buttonExpenses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonReports, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPayroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonQuotation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(buttonCompany)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonServices)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonQuotation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonInvoices, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonExpenses, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonReports, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(233, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

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

    private void buttonClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClientsActionPerformed
        clients();
    }//GEN-LAST:event_buttonClientsActionPerformed

    private void buttonInvoicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInvoicesActionPerformed
        invoices();
    }//GEN-LAST:event_buttonInvoicesActionPerformed

    private void buttonPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPaymentsActionPerformed
        payments();
    }//GEN-LAST:event_buttonPaymentsActionPerformed

    private void buttonUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUsersActionPerformed
        users();
    }//GEN-LAST:event_buttonUsersActionPerformed

    private void buttonLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLogsActionPerformed
        logs();
    }//GEN-LAST:event_buttonLogsActionPerformed

    private void buttonCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCompanyActionPerformed
        company();
    }//GEN-LAST:event_buttonCompanyActionPerformed

    private void buttonServicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonServicesActionPerformed
        services();
    }//GEN-LAST:event_buttonServicesActionPerformed

    private void buttonExpensesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExpensesActionPerformed
       expenses();
    }//GEN-LAST:event_buttonExpensesActionPerformed

    private void buttonReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonReportsActionPerformed
        reports();
    }//GEN-LAST:event_buttonReportsActionPerformed

    private void buttonPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPayrollActionPerformed
        payroll();
    }//GEN-LAST:event_buttonPayrollActionPerformed

    private void buttonQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuotationActionPerformed
        quotation();
    }//GEN-LAST:event_buttonQuotationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DynamicPanel;
    public javax.swing.JButton buttonClients;
    public javax.swing.JButton buttonCompany;
    public javax.swing.JButton buttonExpenses;
    public javax.swing.JButton buttonInvoices;
    public javax.swing.JButton buttonLogs;
    public javax.swing.JButton buttonPayments;
    public javax.swing.JButton buttonPayroll;
    public javax.swing.JButton buttonQuotation;
    public javax.swing.JButton buttonReports;
    public javax.swing.JButton buttonServices;
    public javax.swing.JButton buttonUsers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
