package com.zb.botoken.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Web3jUtil {

    private static final String URL = "https://rinkeby.infura.io/c0oGHqQQlq6XJU2kz5DL";

    /**
     * 得到Web3j的连接对象
     * @return
     */
    public static Web3j getWeb3j(){
        return Web3j.build(new HttpService(URL));
    }
}
