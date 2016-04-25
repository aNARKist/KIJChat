/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

  static String IV = "AAAAAAAAAAAAAAAA";
  
  public static byte[] encryption(String plaintext, String encryptionKey){
      byte[] cipher; 
      int o=0;
      
      while(plaintext.length()%16 !=0){
          plaintext += "\0";
      }
      byte[] combined=new byte[plaintext.length()];
      
      while((o*16)<plaintext.length()){
          try {
              String plain=plaintext.substring(o*16,(o+1)*16);
              cipher=encrypt(plain, encryptionKey);
              System.arraycopy(cipher, 0, combined, (o*16), cipher.length);
              o++;
          } catch (Exception ex) {
              Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      return combined;
  }
  
  public static String decryption(byte[] combined, String encryptionKey){
       int m=0;
      String decrypted="";
      while(m*16<combined.length){
           try {
               byte[] decryp=Arrays.copyOfRange(combined, m*16, ((m+1)*16));
               decrypted+=decrypt(decryp,encryptionKey);
               m++;
           } catch (Exception ex) {
               Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
           }
      }
      return decrypted;
  }
  
  
  public static void AES(String plaintext, String encryptionKey) {
    try{
      byte[] combined=encryption(plaintext,encryptionKey);
      System.out.print("cipher:  ");
      for (int i=0; i<combined.length; i++)
        System.out.print(new Integer(combined[i])+" ");
      System.out.println("");
      
      String decrypted=decryption(combined,encryptionKey);
      System.out.println("decrypt: " + decrypted);

    } catch (Exception e) {
      e.printStackTrace();
    } 
  }

  public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(plainText.getBytes("UTF-8"));
  }

  public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return new String(cipher.doFinal(cipherText),"UTF-8");
  }
}