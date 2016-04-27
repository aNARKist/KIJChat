/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import kij_chat_client.key;
/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private InputStream is;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
        //Client client;
        key Keys;
        String kunci;
        String username;
	
	public Read(InputStream is, ArrayList<String> log, key Keys)
	{
		this.is = is;
                this.log = log;
                this.Keys=Keys;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{	
                                kunci=Keys.getKey();
                                username=Keys.getUsername();
				if(this.is.available()!=0) {
                                                                   //IF THE SERVER SENT US SOMETHING
                                        String masuk="";
                                        while(this.is.available()!=0){
                                            byte[] buff=new byte[16];
                                            is.read(buff);
                                            masuk+=AES.decryption(buff, kunci);
                                        }
                                        masuk=masuk.trim();
                                        //System.out.println(masuk);
                                        if (masuk.split(" ")[0].toLowerCase().equals("success")) {
                                            if (masuk.split(" ")[1].toLowerCase().equals("logout")) {
                                                keepGoing = false;
                                                System.out.println(masuk);
                                            } else if (masuk.split(" ")[1].toLowerCase().equals("login")) {
                                                log.clear();
                                                log.add("true");
                                                System.out.println(masuk.split(" ")[0]+" "+masuk.split(" ")[1]);
                                                String key2=masuk.split(" ")[2];
                                                Keys.setKey(key2);
                                                System.out.println(kunci + " "+ key2+" "+username);
                                            }
                                        }
                                        else if(masuk.split(" ")[0].toLowerCase().equals("fail")){
                                            if(masuk.split(" ")[1].toLowerCase().equals("login")){
                                                username="";
                                                System.out.println(masuk);
                                            }
                                            else{
                                            System.out.println(masuk);}
                                        }
                                        else{
                                            //System.out.println("Masuk ke message..");
                                            String[] vals=masuk.split(" ");
                                            if(vals[1].equals(":")){
                                                //System.out.println("Masuk ke Pmessage..");
                                                String key2=vals[0]+username;
                                                String pesaaan="";
                                                for(int i=2;i<vals.length;i++){
                                                    pesaaan+=vals[i]+" ";
                                                }
                                                pesaaan=pesaaan.trim();
                                                //System.out.println(key2);
                                                String pesan=RC444.decryptPRNG(pesaaan, key2);
                                                String pes=vals[0]+" "+vals[1]+" "+pesan;
                                                System.out.println(pes);
                                            }
                                            else if(vals[1].equals("@")){
                                               // System.out.println("Masuk ke Gmessage..");
                                                String key2=vals[0]+vals[2];
                                                String pesaaan="";
                                                for(int i=4;i<vals.length;i++){
                                                    pesaaan+=vals[i]+" ";
                                                }
                                                pesaaan=pesaaan.trim();
                                                String pesan=RC444.decryptPRNG(pesaaan, key2);
                                                String pes=vals[0]+" "+vals[1]+" "+vals[2]+" "+vals[3]+" "+pesan;
                                                System.out.println(pes);
                                            }
                                            else if(vals[1].toLowerCase().equals("<broadcast>:")){
                                                //System.out.println("Masuk ke Gmessage..");
                                                String key2=vals[0]+"broadcast";
                                                String pesaaan="";
                                                for(int i=2;i<vals.length;i++){
                                                    pesaaan+=vals[i]+" ";
                                                }
                                                pesaaan=pesaaan.trim();
                                                String pesan=RC444.decryptPRNG(pesaaan, key2);
                                                String pes=vals[0]+" "+vals[1]+" "+pesan;
                                                System.out.println(pes);
                                            }
                                        }
                                        
                                }
                                
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
}
