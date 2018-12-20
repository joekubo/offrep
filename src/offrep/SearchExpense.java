
package offrep;

public class SearchExpense {
    private String expenseid;
    private String expensedate;
    private String expensename;
    private String mop;
    private String transactionno;
    private double amount;

    public SearchExpense(String expenseid, String expensedate, String expensename, String mop, String transactionno, double amount) {
        this.expenseid = expenseid;
        this.expensedate = expensedate;
        this.expensename = expensename;
        this.mop = mop;
        this.transactionno = transactionno;
        this.amount = amount;
    }

    public String getExpenseid() {
        return expenseid;
    }

    public String getExpensedate() {
        return expensedate;
    }

    public String getExpensename() {
        return expensename;
    }

    public String getMop() {
        return mop;
    }

    public String getTransactionno() {
        return transactionno;
    }

    public double getAmount() {
        return amount;
    }
    
    
}
