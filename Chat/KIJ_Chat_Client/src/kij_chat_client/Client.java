package kij_chat_client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Client implements Runnable {

	private Socket socket;//MAKE SOCKET INSTANCE VARIABLE
        private String key="0123456789abcdef";
        private String username="";
        
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
                        
                        Read reader = new Read(is, log, this);
			
			Thread tr = new Thread(reader);
			tr.start();
                        
                        Write writer = new Write(chat, os, log, this);
			
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

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

}

