package com.example.sydney.psov3;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by PROGRAMMER2 on 6/9/2017.
 */

class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList = Collections.emptyList();

    ProductAdapter(Context mContext, List<Product> productList){
        this.mContext = mContext;
        this.productList = productList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView prodId,prodName,prodDes,prodPrice,prodQuantity;
        CardView cv;
        MyViewHolder(View view){
            super(view);
            cv = (CardView) view.findViewById(R.id.card_view);
            prodId = (TextView) view.findViewById(R.id.txtProdId);
            prodName = (TextView) view.findViewById(R.id.txtProdName);
            prodDes = (TextView) view.findViewById(R.id.txtProdDes);
            prodPrice = (TextView) view.findViewById(R.id.txtProdPrice);
            prodQuantity = (TextView) view.findViewById(R.id.txtProdQuantity);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
//        Transactions transactions = transactionList.get(position);
//        holder.transNumber.setText(transactions.getTransId());
//        holder.transType.setText(transactions.getTransactionType());
        holder.prodId.setText(productList.get(position).getP_id());
        holder.prodName.setText(productList.get(position).getP_name());
        holder.prodDes.setText(productList.get(position).getP_desc());
        holder.prodPrice.setText(Double.toString(productList.get(position).getP_price()));
        holder.prodQuantity.setText(Integer.toString(productList.get(position).getP_quan()));
    }

    @Override
    public int getItemCount(){
        return productList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Product product){
        productList.add(position,product);
        notifyItemInserted(position);
    }
}
