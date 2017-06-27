package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/24/2017.
 */

class InvoiceItem {
    private String invoiceProductDescription;
    private int invoiceProductPrice;
    private String invoiceProductVattable;
    private int invoiceProductQuantity;

    public String getInvoiceProductDescription() {
        return invoiceProductDescription;
    }

    public void setInvoiceProductDescription(String invoiceProductDescription) {
        this.invoiceProductDescription = invoiceProductDescription;
    }

    public int getInvoiceProductPrice() {
        return invoiceProductPrice;
    }

    public void setInvoiceProductPrice(int invoiceProductPrice) {
        this.invoiceProductPrice = invoiceProductPrice;
    }

    public String getInvoiceProductVattable() {
        return invoiceProductVattable;
    }

    public void setInvoiceProductVattable(String invoiceProductVattable) {
        this.invoiceProductVattable = invoiceProductVattable;
    }

    public int getInvoiceProductQuantity() {
        return invoiceProductQuantity;
    }

    public void setInvoiceProductQuantity(int invoiceProductQuantity) {
        this.invoiceProductQuantity = invoiceProductQuantity;
    }

    public InvoiceItem(String invoiceProductDescription, int invoiceProductPrice, String invoiceProductVattable, int invoiceProductQuantity) {
        this.invoiceProductDescription = invoiceProductDescription;
        this.invoiceProductPrice = invoiceProductPrice;
        this.invoiceProductVattable = invoiceProductVattable;
        this.invoiceProductQuantity = invoiceProductQuantity;
    }

    public InvoiceItem() {
    }


}
