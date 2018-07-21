package com.zb.botoken.adapter;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zb.botoken.R;
import com.zb.botoken.model.ETHWallet;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder>{

    private List<ETHWallet> walletList;

    public WalletAdapter(List<ETHWallet> walletList) {
        this.walletList = walletList;
    }

    private static DrawerLayout drawerLayout;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View walletView;
        TextView walletName;
        ImageView walletImage;

        public ViewHolder(View view) {
            super(view);
            walletView = view;
            walletName = view.findViewById(R.id.wallet_text);
            walletImage = view.findViewById(R.id.wallet_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.walletView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ETHWallet wallet = walletList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + wallet.getName(), Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ETHWallet wallet = walletList.get(position);
        holder.walletName.setText(wallet.getName());
        holder.walletImage.setImageResource(wallet.getImageId());
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public static void setDrawerLayout(DrawerLayout layout){
        drawerLayout = layout;
    }
}