package com.zb.botoken.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class Web3jUtil {

    private static final String TAG = "Web3jUtil";

    private static final String URL = "https://rinkeby.infura.io/c0oGHqQQlq6XJU2kz5DL";

    /**
     * 得到Web3j的连接对象
     * @return
     */
    public static Web3j getWeb3j(){
        return Web3j.build(new HttpService(URL));
    }

    /**
     * 得到指定地址的余额ETH
     * @param address
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
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
