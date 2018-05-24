
package security;

public class SearchQuotation {
    private String quotationno;
    private String clientname;
    private String address;
    private String city;
    private String phoneno;
    private String email;
    private String invoicedate;
    private double total;
    private String servedby;

    public SearchQuotation(String quotationno, String clientname, String address, String city, String phoneno, String email, String invoicedate, double total, String servedby) {
        this.quotationno = quotationno;
        this.clientname = clientname;
        this.address = address;
        this.city = city;
        this.phoneno = phoneno;
        this.email = email;
        this.invoicedate = invoicedate;
        this.total = total;
        this.servedby = servedby;
    }

    public String getQuotationno() {
        return quotationno;
    }

    public String getClientname() {
        return clientname;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getEmail() {
        return email;
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
