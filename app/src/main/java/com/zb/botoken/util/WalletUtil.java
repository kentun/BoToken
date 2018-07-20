package com.zb.botoken.util;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class WalletUtil extends Web3jUtil{

    private static final String TAG = "WalletUtil";

    public static String getBalance(String address) throws ExecutionException, InterruptedException {
        //查询指定地址的余额
        EthGetBalance ethGetBalance = getWeb3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger balance = null;
        if (ethGetBalance.hasError()){
            LogUtil.e(TAG, ethGetBalance.getError().getMessage());
        }else {
            balance = ethGetBalance.getBalance();
        }

        //转换为Ether格式
        BigDecimal balanceEther = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        LogUtil.i(TAG, balanceEther.toEngineeringString());
        return balanceEther.toString();
    }
}
