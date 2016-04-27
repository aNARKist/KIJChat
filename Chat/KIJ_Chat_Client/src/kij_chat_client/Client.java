package kij_chat_client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


import kij_chat_client.key;
/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Client implements Runnable {

	private Socket socket;//MAKE SOCKET INSTANCE VARIABLE
        private key Keys;
        // use arraylist -> arraylist dapat diparsing as reference
        volatile ArrayList<String> log = new ArrayList<>();
        
	public Client(Socket s)
	{
		socket = s;//INSTANTIATE THE INSTANCE VARIABLE
                log.add(String.valueOf("false"));
	}
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
                        Keys=new key();
			Scanner chat = new Scanner(System.in);//GET THE INPUT FROM THE CMD
			//Scanner in = new Scanner(socket.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
			//PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
			InputStream is=socket.getInputStream();
                        OutputStream os=socket.getOutputStream();
//			while (true)//WHILE THE PROGRAM IS RUNNING
//			{						
//				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
//				out.println(input);//SEND IT TO THE SERVER
//				out.flush();//FLUSH THE STREAM
//				
//				if(in.hasNext())//IF THE SERVER SENT US SOMETHING
//					System.out.println(in.nextLine());//PRINT IT OUT
//			}
                        Keys.setKey("0123456789abcdef");
                        //key="0123456789abcdef";
                        Read reader = new Read(is, log, Keys);
			
			Thread tr = new Thread(reader);
			tr.start();
                        
                        Write writer = new Write(chat, os, log, Keys);
			
			Thread tw = new Thread(writer);
			tw.start();
                        
//                        System.out.println(tr.isAlive());
                        while (tr.isAlive() == true) {
                            if (tr.isAlive() == false && tw.isAlive() == false) {
                                socket.close();
                            }
                        }
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
}

