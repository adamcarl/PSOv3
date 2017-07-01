package com.example.sydney.psov3;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by PROGRAMMER2 on 6/24/2017.
 */

class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<InvoiceItem> invoiceList = Collections.emptyList();

    InvoiceAdapter(Context mContext, List<InvoiceItem> invoiceList){
        this.mContext = mContext;
        this.invoiceList = invoiceList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView invoiceProductName,invoiceProductPrice,invoiceProductVattable,invoiceProductQuantity;
        CardView cv;
        MyViewHolder(View view){
            super(view);
            cv = (CardView) view.findViewById(R.id.cv_item);
            invoiceProductName = (TextView) view.findViewById(R.id.txtInvoiceProductName);
            invoiceProductPrice = (TextView) view.findViewById(R.id.txInvoiceProductPrice);
            invoiceProductVattable = (TextView) view.findViewById(R.id.txtInvoiceProductVattable);
            invoiceProductQuantity = (TextView) view.findViewById(R.id.txtInvoiceProductQuantity);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item_card, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
        final InvoiceItem invoiceItem = invoiceList.get(position);
        holder.itemView.setBackgroundColor(invoiceItem.isSelected() ? Color.DKGRAY : Color.GRAY);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoiceItem.setSelected(!invoiceItem.isSelected());
                holder.itemView.setBackgroundColor(invoiceItem.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

        holder.invoiceProductName.setText(invoiceList.get(position).getInvoiceProductDescription());
        holder.invoiceProductPrice.setText(Double.toString(invoiceList.get(position).getInvoiceProductPrice()));
        holder.invoiceProductVattable.setText(invoiceList.get(position).getInvoiceProductVattable());
        holder.invoiceProductQuantity.setText(Integer.toString(invoiceList.get(position).getInvoiceProductQuantity()));
    }

    @Override
    public int getItemCount(){
        return invoiceList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, InvoiceItem invoice){
        invoiceList.add(position,invoice);
        notifyItemInserted(position);
    }
}

