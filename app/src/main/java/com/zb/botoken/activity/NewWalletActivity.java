package com.zb.botoken.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zb.botoken.MyApplication;
import com.zb.botoken.R;
import com.zb.botoken.model.ETHWallet;
import com.zb.botoken.util.LogUtil;
import com.zb.botoken.util.WalletUtil;

import org.web3j.crypto.CipherException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class NewWalletActivity extends BaseActivity {

    private static String TAG = "NewWalletActivity";

    private EditText newWalletName;

    private EditText newWalletPass;

    private EditText newWalletRePass;

    private File filesDir = MyApplication.getContext().getFilesDir();

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
                    LogUtil.i(TAG, filesDir.toString());
                    try {
                        requestAllPower();
                        ETHWallet ethWallet = WalletUtil.newWallet(name, pass, filesDir);
                        System.out.println(ethWallet);
                    } catch (CipherException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    }
                    /*try {
                        System.out.println(cacheDir);
                       *//* ETHWallet ethWallet = WalletUtil.newWallet(name, pass, cacheDir);
                        System.out.println(ethWallet);
                        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("eTHWallet", MODE_PRIVATE).edit();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(ethWallet);
                        String base64Student = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                        editor.putString("student", base64Student);
                        editor.apply();

                        oos.close();
                        baos.close();*//*
                    } catch (CipherException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchProviderException e) {
                        e.printStackTrace();
                    }*/
                }else {
                    Toast.makeText(NewWalletActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    /**
     * 申请权限
     */
    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
