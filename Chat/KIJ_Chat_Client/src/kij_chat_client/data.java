/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/**
 *
 * @author utomo
 */
public class data {
    private static String data;
    
    public static void isidata(String databaru){
        data = databaru;
    }
    
    public static String getData(){
        String data2 = data;
        return data2;
    }
}
