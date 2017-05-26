package com.example.sydney.psov3;

/**
 * Created by sydney on 5/25/2017.
 */

public class Order {
    private String o_name;
    private int o_id;
    private Double o_price;

    public Order(){}
    public Order(String o_name, int o_id, double o_price){
        this.o_name = o_name;
        this.o_id = o_id;
        this.o_price = o_price;
    }

    public String getO_name() {
        return o_name;
    }

    public int getO_id() {
        return o_id;
    }

    public Double getO_price() {
        return o_price;
    }

    public void setO_name(String o_name) {
        this.o_name = o_name;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public void setO_price(Double o_price) {
        this.o_price = o_price;
    }
}