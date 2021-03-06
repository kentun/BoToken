package com.zb.botoken.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.web3j.crypto.WalletUtils.generateWalletFile;

public class Web3jUtil {
    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(Web3jUtil.class);

    public static final String URL = "https://rinkeby.infura.io/c0oGHqQQlq6XJU2kz5DL";

    /**
     * 返回与url的连接对象
     * @param url
     * @return
     */
    public static Web3j getWeb3j(String url){
        return Web3j.build(new HttpService(url));
    }

    /**
     * 查询指定地址的余额，返回余额
     * @param address
     * @return
     * @throws IOException
     */
    public static String ethGetBalance(String address) throws IOException {
        Web3j web3 = Web3jUtil.getWeb3j(URL);

        //查询指定地址的余额
        EthGetBalance ethGetBalance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        BigInteger balance = null;
        if (ethGetBalance.hasError()){
            logger.error(ethGetBalance.getError().getMessage());
        }else {
            balance = ethGetBalance.getBalance();
        }
        //转换为Ether格式
        BigDecimal balanceEther = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        logger.info(balanceEther.toString());
        return balanceEther.toString();
    }

    public static String newWallet(String password, File destinationDirectory)
            throws CipherException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        DeterministicSeed deterministicSeed = Web3jUtil.generatorSeed(password);
        String address = Web3jUtil.newWalletBySeed(deterministicSeed, password, destinationDirectory);
        return address;
    }

    /**
     * 生成助记词
     * @param password
     * @return
     */
    public static DeterministicSeed generatorSeed(String password){
        SecureRandom secureRandom = new SecureRandom();
        long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed deterministicSeed = new DeterministicSeed(secureRandom, 128, password, creationTimeSeconds);
        List<String> mnemonicCode = deterministicSeed.getMnemonicCode();
        logger.info(String.join(" ", mnemonicCode));
        return deterministicSeed;
    }

    /**
     * 根据助记词生成钱包
     * @param seed
     * @return
     */
    public static String newWalletBySeed(DeterministicSeed seed, String password, File destinationDirectory) throws CipherException, IOException {
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

        // Web3j
        Credentials credentials = Credentials.create(privKey.toString(16));
        String address = credentials.getAddress();
        String privateKey = privKey.toString(16);
        String path = generatorKeyStore(credentials, password, destinationDirectory);

        logger.info(address);
        logger.info(privateKey);
        logger.info(path);
        return  address;
    }

    /**
     * 生成keystore文件
     * @param credentials
     * @param password
     * @param destinationDirectory
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public static String generatorKeyStore( Credentials credentials, String password, File destinationDirectory) throws CipherException, IOException {
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory, true);
    }

    /**
     * 根据私钥得到钱包
     * @param privateKey
     * @return
     * @throws IOException
     */
    public static String privateKeyToWallet(String privateKey) throws IOException {
        Credentials credentials = Credentials.create(privateKey);
        return credentials.getAddress();
        /*Web3j web3 = Web3jUtil.getWeb3j(URL);
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Web3Sha3 web3Sha3 = web3.web3Sha3("0x" + ecKeyPair.getPublicKey().toString(16)).send();
        String addr = null;
        if (web3Sha3.hasError()){
            logger.error(web3Sha3.getError().getMessage());
        }else {
            addr = web3Sha3.getResult().substring(26);
        }
        return "0x" + addr;*/
    }

    /**
     * 根据keystore得到钱包
     * @param keyStore
     * @param pwd
     * @return
     * @throws IOException
     */
    public static String keyStoreToWallet(String keyStore, String pwd) throws IOException, CipherException {
        WalletFile walletFile = null;
        walletFile = objectMapper.readValue(keyStore, WalletFile.class);
        Credentials credentials = Credentials.create(Wallet.decrypt(pwd, walletFile));
        return credentials.getAddress();
    }

    /**
     * 通过助记词得到钱包
     * @param seedCode
     * @return
     * @throws IOException
     * @throws UnreadableWalletException
     */
    public static String seedToWallet(String seedCode) throws IOException, UnreadableWalletException {
        String phrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed seed = new DeterministicSeed(seedCode, null, phrase, creationTimeSeconds);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

        // Web3j
        Credentials credentials = Credentials.create(privKey.toString(16));
        String address = credentials.getAddress();
        String privateKey = privKey.toString(16);
        logger.info(address);
        logger.info(privateKey);
        return address;
    }

    /**
     * 得到最近交易的gas均值
     * @return
     * @throws IOException
     */
    public static BigInteger getGasPrice() throws IOException {
        Web3j web3 = Web3jUtil.getWeb3j(URL);
        EthGasPrice ethGasPrice = web3.ethGasPrice().send();
        BigInteger gasPrice = null;
        if (ethGasPrice.hasError()){
            logger.error(ethGasPrice.getError().getMessage());
        }else {
            gasPrice = ethGasPrice.getGasPrice();
        }
        return gasPrice;
    }

    /**
     * 获取交易估算的gas用量
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    public static BigInteger getEstimateGas(String from, String to) throws IOException {
        Web3j web3 = Web3jUtil.getWeb3j(URL);
        Transaction transaction = Transaction.createEthCallTransaction(from, to, null);
        EthEstimateGas ethEstimateGas = web3.ethEstimateGas(transaction).send();
        BigInteger estimateGas = null;
        if (ethEstimateGas.hasError()){
            logger.error(ethEstimateGas.getError().getMessage());
        }else {
            estimateGas = ethEstimateGas.getAmountUsed();
        }
        return estimateGas;
    }

    /**
     * 转账操作
     * @param from
     * @param to
     * @param privateKey
     * @param gasPrice
     * @param gasLimit
     * @param value
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String ethTransaction(String from, String to, String privateKey, BigInteger gasPrice, BigInteger gasLimit, String value) throws IOException, ExecutionException, InterruptedException {
        Web3j web3 = Web3jUtil.getWeb3j(URL);
        //证书
        Credentials credentials = Credentials.create(privateKey);
        //交易证书
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = null;
        if (ethGetTransactionCount.hasError()){
            logger.error(ethGetTransactionCount.getError().getMessage());
        }else {
            nonce = ethGetTransactionCount.getTransactionCount();
        }
        //创建交易
        BigInteger ether = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, gasLimit, to, ether);
        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = null;
        if (ethSendTransaction.hasError()){
            logger.error(ethSendTransaction.getError().getMessage());
        }else {
            transactionHash = ethSendTransaction.getTransactionHash();
        }
        return transactionHash;
    }

    /**
     * 得到交易详情
     * @param txHash
     * @return
     * @throws IOException
     */
    public static EthTransaction getTransaction(String txHash) throws IOException {
        Web3j web3 = Web3jUtil.getWeb3j(URL);
        EthTransaction transaction = web3.ethGetTransactionByHash(txHash).send();
        return transaction;
    }
}
