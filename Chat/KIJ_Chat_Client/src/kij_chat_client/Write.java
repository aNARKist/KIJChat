/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
	private Scanner chat;
        private OutputStream os;
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        boolean keepGoing = true;
        ArrayList<String> log;
        String key;
        String username;
        Client client;
	
	public Write(Scanner chat, OutputStream os, ArrayList<String> log, Client client)
	{
		this.chat = chat;
                this.os = os;
                this.log = log;
                this.in = in;
                this.client=client;
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
                        
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{	
                                key=client.getKey();
				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
				/*if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                    System.out.println("masuk firstkey");
                                    out.println("firstkey");
                                    out.flush();//FLUSH THE STREAM
                                    //input = this.in.nextLine();
                                    sleep(3000);
                                    String enkripsi = data.getData();
                                    System.out.println("input");
                                    byte[] input2 = enkripsi.getBytes(StandardCharsets.UTF_8);;
                                    for (int i=0; i<input2.length; i++){
                                        System.out.print(new Integer(input2[i])+" ");
                                    }
                                    System.out.println("");
                                    String decrypted = AES.decrypt(input2, firstkey);
                                    System.out.println(decrypted);
                                    String[] vals = input.split(" ");
                                    
                                }*/
                                if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                    byte[] outing= AES.encryption(input, key);
                                    username=input.split(" ")[1];
                                    client.setUsername(username);
                                    os.write(outing);
                                    os.flush();
                                }
                                else if(input.split(" ")[0].toLowerCase().equals("pm")==true) {
                                    String[] vals=input.split(" ",3);
                                    String seed=username+vals[1];
                                    String pesan=RC444.encryptPRNG(vals[2], seed);
                                    String pes=vals[0]+" "+vals[1]+" "+pesan;
                                    byte[]outing=AES.encryption(pes, key);
                                    System.out.println(key);
                                    os.write(outing);
                                    os.flush();
                                }
                                else if(input.split(" ")[0].toLowerCase().equals("gm")==true){
                                    String[] vals=input.split(" ",3);
                                    String seed=username+vals[1];
                                    String pesan=RC444.encryptPRNG(vals[2], seed);
                                    String pes=vals[0]+" "+vals[1]+" "+pesan;
                                    byte[]outing=AES.encryption(pes, key);
                                    os.write(outing);
                                    os.flush();
                                }
                                else if(input.split(" ")[0].toLowerCase().equals("bm")==true){
                                    String[] vals=input.split(" ",2);
                                    String seed=username+"broadcast";
                                    String pesan=RC444.encryptPRNG(vals[1], seed);
                                    String pes=vals[0]+" "+vals[1]+" "+pesan;
                                    byte[]outing=AES.encryption(pes, key);
                                    os.write(outing);
                                    os.flush();
                                }
                                else if (input.split(" ")[0].toLowerCase().equals("logout")==true) {
                                    if (log.contains("true"))
                                        keepGoing = false;
                                }
                                else {
                                    byte[] outuing=AES.encryption(input, key);
                                    os.write(outuing);
                                    os.flush();
                                }
                                //out.println(input);//SEND IT TO THE SERVER
				//out.flush();//FLUSH THE STREAM
                                
                                //if ()
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}
