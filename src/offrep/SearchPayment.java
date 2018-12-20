
package offrep;

public class SearchPayment {
    private String paymentid;
    private String pay_date;
    private String clientname;
    private double amount;

    public SearchPayment(String paymentid, String pay_date, String clientname, double amount) {
        this.paymentid = paymentid;
        this.pay_date = pay_date;
        this.clientname = clientname;
        this.amount = amount;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public String getPay_date() {
        return pay_date;
    }

    public String getClientname() {
        return clientname;
    }

    public double getAmount() {
        return amount;
    }
    
    
}
