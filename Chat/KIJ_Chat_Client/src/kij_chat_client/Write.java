/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

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
        private PrintWriter out;
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        boolean keepGoing = true;
        ArrayList<String> log;
        private String firstkey= "0123456789abcdef";
	
	public Write(Scanner chat, PrintWriter out, Scanner in, ArrayList<String> log)
	{
		this.chat = chat;
                this.out = out;
                this.log = log;
                this.in = in;
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
				if (input.split(" ")[0].toLowerCase().equals("login") == true) {
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
                                    
                                }
                                out.println(input);//SEND IT TO THE SERVER
				out.flush();//FLUSH THE STREAM
                                
                                //if ()
                                if (input.contains("logout")) {
                                    if (log.contains("true"))
                                        keepGoing = false;
                                    
                                }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}
