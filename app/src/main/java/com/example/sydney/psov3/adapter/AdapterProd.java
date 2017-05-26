package com.example.sydney.psov3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sydney.psov3.Product;
import com.example.sydney.psov3.R;

import java.util.ArrayList;

/**
 * Created by sydney on 5/2/2017.
 */

public class AdapterProd extends BaseAdapter {

    private Context c;
    private int layout;
    private ArrayList<Product> prodList;

    public AdapterProd(Context c, int layout, ArrayList<Product> prodList) {

        this.c = c;

        this.layout = layout;

        this.prodList = prodList;

    }

    @Override
    public int getCount() {
        return prodList.size();
    }

    @Override
    public Object getItem(int position) {
        return prodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView txt_prodId,txt_prodName,txt_prodDesc,txt_prodPrice,txt_prodQuan;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txt_prodId = (TextView) row.findViewById(R.id.txt_prod_id);
            holder.txt_prodName = (TextView) row.findViewById(R.id.txt_prod_name);
            holder.txt_prodDesc = (TextView) row.findViewById(R.id.txt_prod_desc);
            holder.txt_prodPrice = (TextView) row.findViewById(R.id.txt_prod_price);
            holder.txt_prodQuan = (TextView) row.findViewById(R.id.txt_prod_quan);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Product product= prodList.get(position);
        holder.txt_prodId.setText(product.getP_id());
        holder.txt_prodName.setText(product.getP_name());
        holder.txt_prodDesc.setText(product.getP_desc());
        double mPrice = product.getP_price();
        String strPrice = String.valueOf(mPrice);
        holder.txt_prodPrice.setText(strPrice);
        //Convert int Vote Count to String in order to pass as String in textview
        int mQuan = product.getP_quan();
        String strQuan = String.valueOf(mQuan);
        holder.txt_prodQuan.setText(strQuan);
        return row;
    }
}