package com.arg.ccra3.online.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;


public class AesEcbEncryptDecrypt {

    private static final Logger LOG = LogManager.getLogger(AesEcbEncryptDecrypt.class.getName());
    private static SecretKeySpec secretKey;
    private static byte[] key;

    private static String decryptedString;
    private static String encryptedString;
    private static final String UTF_8 = "UTF-8";

    public static void setKey(String myKey) {

        MessageDigest sha = null;
        try {
            key = myKey.getBytes(UTF_8);
            LOG.info(key.length);

            key = Arrays.copyOf(key, 32);
            LOG.info(key.length);
            secretKey = new SecretKeySpec(key, "AES");

        } catch (UnsupportedEncodingException e) {
            LOG.info("Error>>>" );
            LOG.info(e);
        }

    }

    public static String getDecryptedString() {
        return decryptedString;
    }

    public static void setDecryptedString(String decryptedString) {
        AesEcbEncryptDecrypt.decryptedString = decryptedString;
    }

    public static String getEncryptedString() {
        return encryptedString;
    }

    public static void setEncryptedString(String encryptedString) {
        AesEcbEncryptDecrypt.encryptedString = encryptedString;
    }

    public String encrypt(String strToEncrypt) {
        String encrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            encrypted = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes(UTF_8)));

        } catch (Exception e) {

            LOG.info("Error while encrypting: " );
            LOG.info(e.toString());
        }
        return encrypted;
    }

    public String decrypt(String strToDecrypt) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decrypted = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            LOG.info("Key Pass");
            LOG.info("decrypted::");
            LOG.info(decrypted);
        } catch (Exception e) {

            LOG.info("Error while decrypting: ");
            LOG.info(e.toString());
            decrypted = "Error";
        }
        return decrypted;
    }

    public static void main(String args[]) {
        final String strToEncrypt = "{\"data\":{\"email\":\"raatasamut@gmail.com\",\"password\":\"1b01e2c0c85001ef5684bbf3a457f99e\",\"username\":\"fairii\",\"uniqueID\":-1,\"updateDate\":0},\"module\":\"BIZAuthentication\",\"target\":\"Register\"}";
        final String strPssword = "b03bc93bd508ea0e15a6866db8789ed6";
        AesEcbEncryptDecrypt.setKey(strPssword);
        AesEcbEncryptDecrypt encryptObject = new AesEcbEncryptDecrypt();
        encryptObject.encrypt(strToEncrypt.trim());
        String encObjText = encryptObject.encrypt(strToEncrypt.trim());

        LOG.info("String to Encrypt: " );
        LOG.info(strToEncrypt);
        LOG.info("Encrypted: ");
        LOG.info(encObjText);

        final String strToDecrypt = AesEcbEncryptDecrypt.getEncryptedString();
        AesEcbEncryptDecrypt decryptObject = new AesEcbEncryptDecrypt();
        decryptObject.decrypt(strToDecrypt.trim());
        String decObjText = decryptObject.decrypt(strToDecrypt.trim());

        LOG.info("String To Decrypt : ");
        LOG.info(strToDecrypt);
        LOG.info("Decrypted : " );
        LOG.info(decObjText);

    }

}
