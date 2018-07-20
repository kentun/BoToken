package com.zb.botoken.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zb.botoken.R;
import com.zb.botoken.model.Currency;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{

    private List<Currency> currencyList;

    public CurrencyAdapter(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View currencyView;
        TextView currencyName;
        TextView currencyCount;

        public ViewHolder(View view) {
            super(view);
            currencyView = view;
            currencyName = view.findViewById(R.id.currency_name);
            currencyCount = view.findViewById(R.id.currency_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.currencyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Currency currency = currencyList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + currency.getCurrencyName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.currencyName.setText(currency.getCurrencyName());
        holder.currencyCount.setText(currency.getCurrencyCount().toString());
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }
}