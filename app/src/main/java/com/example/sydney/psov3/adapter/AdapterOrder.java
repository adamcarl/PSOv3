package com.example.sydney.psov3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sydney.psov3.Order;
import com.example.sydney.psov3.R;

import java.util.ArrayList;

/**
 * Created by sydney on 5/25/2017.
 */

public class AdapterOrder extends BaseAdapter {

    private Context c;
    private int layout;
    private ArrayList<Order> orderList;

    public AdapterOrder(Context c, int layout, ArrayList<Order> orderList) {

        this.c = c;

        this.layout = layout;

        this.orderList = orderList;

    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView txt_orderName,txt_orderId,txt_orderPrice;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterOrder.ViewHolder holder = new AdapterOrder.ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txt_orderName = (TextView) row.findViewById(R.id.txt_order_name);
            holder.txt_orderId = (TextView) row.findViewById(R.id.txt_order_id);
            holder.txt_orderPrice = (TextView) row.findViewById(R.id.txt_order_price);
            row.setTag(holder);
        } else {
            holder = (AdapterOrder.ViewHolder) row.getTag();
        }
        Order order= orderList.get(position);
        holder.txt_orderName.setText(order.getO_name());
        holder.txt_orderId.setText(order.getO_id());
        double mPrice = order.getO_price();
        String strPrice = String.valueOf(mPrice);
        holder.txt_orderPrice.setText(strPrice);
        return row;
    }
}