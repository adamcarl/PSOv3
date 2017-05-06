package com.example.sydney.psov3;

/**
 * Created by sydney on 5/2/2017.
 */

public class Product {
    private String p_id;
    private String p_name;
    private String p_desc;
    private double p_price;
    private int p_quan;

    public Product(){
    }
    public Product(String p_id, String p_name, String p_desc, double p_price, int p_quan) {
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_desc = p_desc;
        this.p_price = p_price;
        this.p_quan = p_quan;
    }
    public String getP_id() {
        return p_id;
    }
    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
    public String getP_name() {
        return p_name;
    }
    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
    public String getP_desc() {
        return p_desc;
    }
    public void setP_desc(String p_desc) {this.p_desc = p_desc;
    }
    public double getP_price(){
        return p_price;
    }
    public void setP_price(double p_price) {
        this.p_price = p_price;
    }
    public int getP_quan() {
        return p_quan;
    }
    public void setP_quan(int p_quan) {this.p_quan = p_quan;
    }
}