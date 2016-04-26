/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.util.Random;

/**
 *
 * @author NARK
 */
public class RC444 {
    
    public static String encryptPRNG(String k, String Key){
        String seed=k;
        int i=0;
        int[] keys=PRNG(Strtolong(Key),seed.length());
        char[] seedarray=seed.toCharArray();
        for(;i<seed.length();i++){
            char a=seedarray[i];
            char b=seedarray[keys[i]];
            seedarray[i]=b;
            seedarray[keys[i]]=a;    
        }
        String encrypseed= new String(seedarray);
        
        return encrypseed;
    }
    
    public static String decryptPRNG(String k, String Key){
        String seed=k;
        
        int i;
        int[] keys=PRNG(Strtolong(Key),seed.length());
        char[] seedarray=seed.toCharArray();
        for(i=seed.length()-1;i>-1;i--){
            char a=seedarray[i];
            char b=seedarray[keys[i]];
            seedarray[i]=b;
            seedarray[keys[i]]=a;    
        }
        String encrypseed= new String(seedarray);
        return encrypseed;
    }
    
    public static int[] PRNG(long seed, int length){
        int[] rng= new int[length];
        Random random = new Random(seed);
        for (int i = 0; i < length; i++) {
            rng[i] = random.nextInt(length);
        }
        return rng;
    }
    
    public static long Strtolong(String asal){
        int i=0;
        String kumpul="";
        char[] chars = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        while (i<asal.length()){
            for(int j=0;j<chars.length;j++){
                if(chars[j]==asal.charAt(i)){
                   kumpul+=j;
                }
            }
            i++;
        }
       long l=Long.parseLong(kumpul.substring(0,4)+kumpul.substring(8,12));
       return l;
    }
}
