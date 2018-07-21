package com.zb.botoken.util;

import com.zb.botoken.R;
import com.zb.botoken.model.ETHWallet;
import com.zb.botoken.test.Web3jUtil;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;
import static org.web3j.crypto.WalletUtils.generateWalletFile;

public class WalletUtil{


    /**
     * 生成新的钱包
     * @param name
     * @param password
     * @param destinationDirectory
     * @return
     * @throws CipherException
     * @throws IOException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static ETHWallet newWallet(String name, String password, File destinationDirectory)
            throws CipherException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ETHWallet ethWallet = new ETHWallet();

        DeterministicSeed deterministicSeed = WalletUtil.getDeterministicSeed(password);
        String mnemonic = String.join(" ", deterministicSeed.getMnemonicCode());
        Credentials credentials = WalletUtil.getWalletBySeed(deterministicSeed, password, destinationDirectory);
        String path = getKeyStoreByCredentials(credentials, password, destinationDirectory);

        ethWallet.setName(name);
        ethWallet.setMnemonic(mnemonic);
        ethWallet.setAddress(credentials.getAddress());
        ethWallet.setKeystorePath(path);
        ethWallet.setPassword(MD5Util.md5(password));
        ethWallet.setCurrent(true);
        ethWallet.setBackup(false);
        ethWallet.setImageId(R.drawable.head_rabbit);

        return ethWallet;
    }

    /**
     * 生成助记词
     * @param password
     * @return
     */
    public static DeterministicSeed getDeterministicSeed(String password){
        SecureRandom secureRandom = new SecureRandom();
        long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed deterministicSeed = new DeterministicSeed(secureRandom, 128, password, creationTimeSeconds);
        return deterministicSeed;
    }

    /**
     * 根据助记词生成钱包
     * @param seed
     * @return
     */
    public static Credentials getWalletBySeed(DeterministicSeed seed, String password, File destinationDirectory) throws CipherException, IOException {
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");
        DeterministicKey key = chain.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();
        Credentials credentials = Credentials.create(privKey.toString(16));
        return  credentials;
    }

    /**
     * 根据证书生成keystore文件
     * @param credentials
     * @param password
     * @param destinationDirectory
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public static String getKeyStoreByCredentials( Credentials credentials, String password, File destinationDirectory) throws CipherException, IOException {
        ECKeyPair ecKeyPair = credentials.getEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory, true);
    }
}
