package application;

import java.io.*;
import java.util.Scanner;

import java.io.*;
import java.util.Scanner;

/*把input中文文字加密，导出加密文件，并把加密文件解密到解密文件
 * *、
 */
public class CaesarCipher {

	// 加密方法
    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char encryptedChar = (char) (base + (ch - base + key) % 26);
                result.append(encryptedChar);
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    // 解密方法
    public static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char decryptedChar = (char) (base + (ch - base - key + 26) % 26);
                result.append(decryptedChar);
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

//    public static void main(String[] args) {
//        String text = "Hello, I am Jia Guo! 123"; // 要加密的文本
//        int key = 3; // 密钥
//
//        // 加密文本
//        String encryptedText = encrypt(text, key);
//        System.out.println("Encrypted: " + encryptedText);
//
//        // 解密文本
//        String decryptedText = decrypt(encryptedText, key);
//        System.out.println("Decrypted: " + decryptedText);
//    }
}
