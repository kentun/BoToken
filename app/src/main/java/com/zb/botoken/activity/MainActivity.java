package com.zb.botoken.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zb.botoken.R;
import com.zb.botoken.adapter.CurrencyAdapter;
import com.zb.botoken.util.LogUtil;
import com.zb.botoken.util.WalletUtil;
import com.zb.botoken.model.Currency;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";

    private String address = "0x8Ea9E6A390e8Bb9E650EdF5A7DA7cf1c105531e1";

    private List<Currency> currencyList = new ArrayList<>();

    private DrawerLayout drawerLayout;

    CollapsingToolbarLayout collapsingToolbarLayout;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取控件对象
        Toolbar toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycler_currency);

        //RecyclerView操作
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //初始化数据
        Currency currencyETH = new Currency("ETH", "2.0012");
        currencyList.add(currencyETH);
        Currency currencyBTC = new Currency("BTC", "1.0006");
        currencyList.add(currencyBTC);
        CurrencyAdapter adapter = new CurrencyAdapter(currencyList);
        recyclerView.setAdapter(adapter);

        //Toobar代替actionbar
        setSupportActionBar(toolbar);
        //设置默认ETH量
        collapsingToolbarLayout.setTitle("ETH：" + 0);

        //设置菜单按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=  null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //开启网络操作线程
        new Thread(networkTask).start();

        //悬浮按钮点击事件
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "添加币种", Toast.LENGTH_SHORT).show();
            }
        });

        //菜单项点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_scan:
                        Toast.makeText(MainActivity.this, "扫一扫", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_wallet:
                        Toast.makeText(MainActivity.this, "创建钱包", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_import:
                        Toast.makeText(MainActivity.this, "导入钱包", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    //得到线程返回结果显示到UI
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.i(TAG,"handleMessage");
            String ethBalance = msg.getData().getString("ethBalance");
            if (ethBalance != null && ethBalance != ""){
                collapsingToolbarLayout.setTitle("ETH：" + ethBalance);
                /*Currency currencyETH = new Currency("ETH", ethBalance);
                currencyList.add(currencyETH);
                CurrencyAdapter adapter = new CurrencyAdapter(currencyList);
                recyclerView.setAdapter(adapter);*/
            }
        }
    };

    //在线程中进行网络操作
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            try {
                String ethBalance = WalletUtil.getBalance(address);
                data.putString("ethBalance", ethBalance);
                LogUtil.i(TAG, ethBalance);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}
