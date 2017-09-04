package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 8/3/2017.
 */

class JoinTable {
    String barcode;
    String name;
    int quantity;
    int solditem;
    int newQuantity;

    public JoinTable(String barcode, String name, int quantity, int solditem, int newQuantity) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.solditem = solditem;
        this.newQuantity = newQuantity;
    }

    public JoinTable(){

    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSolditem() {
        return solditem;
    }

    public void setSolditem(int solditem) {
        this.solditem = solditem;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
