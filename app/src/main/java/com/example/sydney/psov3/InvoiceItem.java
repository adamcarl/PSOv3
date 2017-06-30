package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/24/2017.
 */

class InvoiceItem {
    private String invoiceProductID;
    private String invoiceProductDescription;
    private double invoiceProductPrice;
    private String invoiceProductVattable;
    private int invoiceProductQuantity;

    public String getInvoiceProductID() {
        return invoiceProductID;
    }

    public void setInvoiceProductID(String invoiceProductID) {
        this.invoiceProductID = invoiceProductID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false;

    public String getInvoiceProductDescription() {
        return invoiceProductDescription;
    }

    public void setInvoiceProductDescription(String invoiceProductDescription) {
        this.invoiceProductDescription = invoiceProductDescription;
    }

    public double getInvoiceProductPrice() {
        return invoiceProductPrice;
    }

    public void setInvoiceProductPrice(double invoiceProductPrice) {
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

    public InvoiceItem(String invoiceProductDescription, double invoiceProductPrice, String invoiceProductVattable, int invoiceProductQuantity,String invoiceProductID) {
        this.invoiceProductDescription = invoiceProductDescription;
        this.invoiceProductPrice = invoiceProductPrice;
        this.invoiceProductVattable = invoiceProductVattable;
        this.invoiceProductQuantity = invoiceProductQuantity;
        this.invoiceProductID = invoiceProductID;
    }

    public InvoiceItem() {
    }


}
