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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private Context mContext;
    private List<Transactions> transactionList = Collections.emptyList();

    public TransactionAdapter(Context mContext, List<Transactions> transactionList){
        this.mContext = mContext;
        this.transactionList = transactionList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView transNumber,transType,transDateTime;
        public CardView cv;

        public MyViewHolder(View view){
            super(view);
            cv = (CardView) view.findViewById(R.id.card_view);
            transNumber = (TextView) view.findViewById(R.id.txtTransNumber);
            transType = (TextView) view.findViewById(R.id.txtTransType);
            transDateTime = (TextView) view.findViewById(R.id.txtTransDateTime);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
//        Transactions transactions = transactionList.get(position);
//        holder.transNumber.setText(transactions.getTransId());
//        holder.transType.setText(transactions.getTransactionType());
        holder.transNumber.setText(Integer.toString(transactionList.get(position).getTransId()));
        holder.transType.setText(transactionList.get(position).getTransType());
        holder.transDateTime.setText(transactionList.get(position).getTransDateTime());
    }

    @Override
    public int getItemCount(){
        return transactionList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Transactions transactions){
        transactionList.add(position,transactions);
        notifyItemInserted(position);
    }
}