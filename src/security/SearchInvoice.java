
package security;

public class SearchInvoice {
    private String invoiceno;
    private String clientname;
    private String invoicedate;
    private double total;
    private String servedby;

    public SearchInvoice(String invoiceno, String clientname, String invoicedate, double total, String servedby) {
        this.invoiceno = invoiceno;
        this.clientname = clientname;
        this.invoicedate = invoicedate;
        this.total = total;
        this.servedby = servedby;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public String getClientname() {
        return clientname;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public double getTotal() {
        return total;
    }

    public String getServedby() {
        return servedby;
    }
    
    
}
