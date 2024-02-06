package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AES {
	private SecretKey secretKey;

    public byte[] encrypt(String strDataToEncrypt, SecretKey secretKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
        byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
        return byteCipherText;
    }

    public String decrypt(byte[] strCipherText, SecretKey secretKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] byteDecryptedText = aesCipher.doFinal(strCipherText);
        return new String(byteDecryptedText);
    }

    public void saveKeyToKeyStore(String fileName, String keyAlias, char[] keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry(keyAlias, secretKey, keystorePassword, null);

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            keyStore.store(fos, keystorePassword);
            System.out.println("The AES key saved in KeyStore!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SecretKey loadKeyFromKeyStore(String fileName, String keyAlias, char[] keystorePassword) throws Exception {
        SecretKey secretKey = null;
        KeyStore keyStore = KeyStore.getInstance("JCEKS");

        try (FileInputStream fis = new FileInputStream(fileName)) {
            keyStore.load(fis, keystorePassword);
            secretKey = (SecretKey) keyStore.getKey(keyAlias, keystorePassword);
            System.out.println("The AES key loaded from KeyStore!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }
   public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    
    
    public static void main(String[] args) {
        try {
            AES aes = new AES();
            String msg = "This is a secret message.";

            // Generate AES key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();

            // Save AES key to KeyStore
            String keyStoreFileName = "AESkeystore.jceks";
            String keyAlias = "myAESKey";
            char[] keystorePassword = "12345678".toCharArray();
           aes.setSecretKey(secretKey);
            aes.saveKeyToKeyStore(keyStoreFileName, keyAlias, keystorePassword);

            // Load AES key from KeyStore
            SecretKey loadedKey = aes.loadKeyFromKeyStore(keyStoreFileName, keyAlias, keystorePassword);

            // Encrypt and decrypt using loaded key
            byte[] encryptedText = aes.encrypt(msg, loadedKey);
            String encodedString = Base64.getEncoder().encodeToString(encryptedText);
            System.out.println("The AES encrypted message (Base64): " + encodedString);

            String decryptedText = aes.decrypt(encryptedText, loadedKey);
            System.out.println("Decrypted text: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
}
