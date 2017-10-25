package com.example.sydney.psov3;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
    private TransactionAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener;

    public TransactionAdapter(Context mContext, List<Transactions> transactionList, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mContext = mContext;
        this.transactionList = transactionList;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView,onRecyclerItemClickListener);
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

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(View childView, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView transNumber, transType, transDateTime;
        public CardView cv;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;


        public MyViewHolder(View view, OnRecyclerItemClickListener onRecyclerItemClickListener) {
            super(view);
            cv = (CardView) view.findViewById(R.id.card_view);
            transNumber = (TextView) view.findViewById(R.id.txtTransNumber);
            transType = (TextView) view.findViewById(R.id.txtTransType);
            transDateTime = (TextView) view.findViewById(R.id.txtTransDateTime);

            this.onRecyclerItemClickListener = onRecyclerItemClickListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.onRecyclerItemClick(view, getAdapterPosition());
            } else {
                Snackbar.make(view, "Null OnRecItemListener!", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}