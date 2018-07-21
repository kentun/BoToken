package com.zb.botoken.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zb.botoken.R;

public class NewWalletActivity extends BaseActivity {

    private EditText newWalletName;

    private EditText newWalletPass;

    private EditText newWalletRePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wallet);

        Toolbar toolbar = findViewById(R.id.newWallet_toolbar);
        setSupportActionBar(toolbar);

        ImageButton imageButton = findViewById(R.id.new_wallet_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newWalletName = findViewById(R.id.new_wallet_name);
        newWalletPass = findViewById(R.id.new_wallet_pass);
        newWalletRePass = findViewById(R.id.new_wallet_repass);
        Button newWalletButton = findViewById(R.id.new_wallet_button);
        newWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = newWalletName.getText().toString();
                String pass = newWalletPass.getText().toString();
                String repass = newWalletRePass.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(NewWalletActivity.this, "名称不为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()){
                    Toast.makeText(NewWalletActivity.this, "密码不为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repass.isEmpty()){
                    Toast.makeText(NewWalletActivity.this, "密码不为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.equals(repass)){

                }else {
                    Toast.makeText(NewWalletActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
