package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/24/2017.
 */

class InvoiceItem {
    private String invoiceProductID;
    private String invoiceProductName;
    private String invoiceProductDescription;
    private double invoiceProductPrice;
    private String invoiceProductVattable;
    private int invoiceProductQuantity;
    private boolean isSelected = false;
    private double invoiceProductTotal;

    public double getInvoiceProductTotal() {
        return invoiceProductTotal;
    }

    public void setInvoiceProductTotal(double invoiceProductTotal) {
        this.invoiceProductTotal = invoiceProductTotal;
    }

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

    public InvoiceItem(String invoiceProductName, String invoiceProductDescription, double invoiceProductPrice, String invoiceProductVattable, int invoiceProductQuantity,String invoiceProductID,double invoiceProductTotal) {
        this.invoiceProductName = invoiceProductName;
        this.invoiceProductDescription = invoiceProductDescription;
        this.invoiceProductPrice = invoiceProductPrice;
        this.invoiceProductVattable = invoiceProductVattable;
        this.invoiceProductQuantity = invoiceProductQuantity;
        this.invoiceProductID = invoiceProductID;
        this.invoiceProductTotal = invoiceProductTotal;
    }

    public InvoiceItem() {
    }


    public String getInvoiceProductName() {
        return invoiceProductName;
    }

    public void setInvoiceProductName(String invoiceProductName) {
        this.invoiceProductName = invoiceProductName;
    }
}
