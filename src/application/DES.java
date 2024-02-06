package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.cert.CertificateException;


/**
 *
 * @author ahmed
 */
public class DES {
    
    private static SecretKey secretkey; 
    
    
    public DES() throws NoSuchAlgorithmException 
    {
        generateKey();
    }
    
    
    public void generateKey() throws NoSuchAlgorithmException 
    {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        this.setSecretkey(keyGen.generateKey());        
    }
    
    //加密方式
    public byte[] encrypt(String strDataToEncrypt, SecretKey secretKey) throws NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
    IllegalBlockSizeException, BadPaddingException {
    	 String keyAlias = "myDESKey";
         char[] keystorePassword = "hello".toCharArray();
    	Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    	desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
    	byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
    	byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);
    	return byteCipherText;
}

    
    public String decrypt(byte[] strCipherText, SecretKey secretKey) throws NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
    IllegalBlockSizeException, BadPaddingException {
    		Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    		desCipher.init(Cipher.DECRYPT_MODE, secretKey);
    		byte[] byteDecryptedText = desCipher.doFinal(strCipherText);
    		return new String(byteDecryptedText);
             }
    /**
     * @return the secretkey
     */
    public static SecretKey getSecretkey() {
        return secretkey;
    }

    /**
     * @param secretkey the secretkey to set
     */
    public void setSecretkey(SecretKey secretkey) {
        this.secretkey = secretkey;
    }
    
//    private static SecretKey generateKey(String secret) throws Exception {
//        byte[] key = secret.getBytes();
//        return new SecretKeySpec(Arrays.copyOf(key, 8), "DES");
//    }
    
    //存储密码到本地
    public void saveKeyToKeyStore(String fileName, String keyAlias, char[] keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry(keyAlias, secretkey, keystorePassword, null);

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            keyStore.store(fos, keystorePassword);
            System.out.println("The DES key saved in KeyStore!");
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    
    //load key
    public SecretKey loadKeyFromKeyStore(String fileName, String keyAlias, char[] keystorePassword) throws Exception {
        SecretKey secretKey = null;
        KeyStore keyStore = KeyStore.getInstance("JCEKS");

        try (FileInputStream fis = new FileInputStream(fileName)) {
            keyStore.load(fis, keystorePassword);
            secretKey = (SecretKey) keyStore.getKey(keyAlias, keystorePassword);
            System.out.println("The DES key loaded from KeyStore!");
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey;
    }
    
   
    
    
//    
//    public static void main(String[] args){
//        // TODO code application logic here
//        try
//        {
//            DES des1 = new DES();
//            String msg = "This is jia :)";        
//            
//            System.out.println("The plain text: "+msg);            
//
//
//         // 保存密钥到 KeyStore 文件
//            String keyStoreFileName = "DESkeystore.jceks";
//            String keyAlias = "myDESKey";
//            char[] keystorePassword = "12345678".toCharArray();
//            des1.saveKeyToKeyStore(keyStoreFileName, keyAlias, keystorePassword);
//
//            // 从 KeyStore 文件中加载密钥
//            SecretKey loadedKey = des1.loadKeyFromKeyStore(keyStoreFileName, keyAlias, keystorePassword);
//
//            // 加载的密钥和原始密钥是否相同
//            boolean keysMatch = loadedKey.equals(des1.getSecretkey());
//            System.out.println("Keys match: " + keysMatch);
//
//            
//            // 使用加载的密钥进行解密等操作
//            //String message = "This is a secret message.";
//            byte[] encryptedText = des1.encrypt(msg, loadedKey);
//            String decryptedText = des1.decrypt(encryptedText, loadedKey);
//            System.out.println("Decrypted text: " + decryptedText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
