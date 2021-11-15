package com.runt.open.mvvm.retrofit.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSAUtils {

    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALBGei0scHoOjTLImPHvASaGqYNrdLie0ckWp74Nkqv7FVeXPOvWEG8_jRJCVjJ1grr8SGd9sVY2sxn5XIz7fUEBfx7Vm8m0DaCNBWJpFLGw9xiaVZ2AUKoNyTD4NgZobbwZbt6ZNB6_fggPrGF18pq6GPyCndX1JW8ZiZKj33VBAgMBAAECgYB0q-EX3y7_CnyYXT8l-mxHhJ_T9R6HR89QimcyGqe2nvRMSjSvX7r29xg3OqL0uORzQKHnpcDncELw8SQ5yAbpENeIsD0dvdFlkoyFYU4ljeUbJ46binwwg20TNARjTbpNos9zbhTPh8qixdblxppXA1WC18HtXhixgca5bNG9lQJBAPQfNdpNdDL9l8Tw4hYVuDMszcFuZYbHbm0S4xcwqj-dXNWBztNf5W_K92-N5GIoHbOypkGzjlBjSZi_oKA0HusCQQC42irhw682CG44mKdP6YRDxy6OaauVX4yE9WnsbO8JFSSc9ZCKMMD0F3NGtytDrVMAJxG1iPWXa4ptEdtgwCmDAkAUW1npR1YuPllekdu4jb0bf1v1ClirAYxiyhVnxKYdweiQ4U827yM5zEoP4lwuFzxK1NXqWqe-alkjxK8HTPFbAkAviQLf_adP2MknSrIzzZQSreTeAHR8PA7xnf54KucpScOZjVh3AOSNoH4nYDEC_U5LysA2E5s8Lg5xz9a_QYsrAkEAwV6gNED7_SYDsYyEWimQ6znUb_QSY-sSChnSCY-ILG1wpynBHw_t1Oi3ljl6gL_cYKG1O3uwOtvZtb-Vr1bNkQ";

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwRnotLHB6Do0yyJjx7wEmhqmDa3S4ntHJFqe-DZKr-xVXlzzr1hBvP40SQlYydYK6_EhnfbFWNrMZ-VyM-31BAX8e1ZvJtA2gjQViaRSxsPcYmlWdgFCqDckw-DYGaG28GW7emTQev34ID6xhdfKauhj8gp3V9SVvGYmSo991QQIDAQAB";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    static final String KEY_RSA = "RSA"; //android标准 “RSA/ECB/PKCS1Padding” 服务端标准 “RSA”

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        byte[] decodedKey = Base64.decode(privateKey.getBytes(), Base64.URL_SAFE);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        byte[] decodedKey = Base64.decode(publicKey.getBytes(), Base64.URL_SAFE);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param key 密钥
     * @return
     */
    public static String encrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeToString(encryptedData,Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param key 密钥
     * @return
     */
    public static String decrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dataBytes = Base64.decode(data.getBytes(), Base64.URL_SAFE);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return  Base64.encodeToString(signature.sign(),Base64.DEFAULT);
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decode(sign.getBytes(),Base64.DEFAULT));
    }

}
