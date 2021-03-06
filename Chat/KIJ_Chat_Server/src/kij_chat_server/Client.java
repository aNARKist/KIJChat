package kij_chat_server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import static kij_chat_server.AES.decrypt;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
        private String username;
        private boolean login = false;
        private String key= "0123456789abcdef";
        
        private ArrayList<Pair<Socket,String>> _loginlist;
        private ArrayList<Pair<String,String>> _userlist;
        private ArrayList<Pair<String,String>> _grouplist;
        public ArrayList<Pair<String,String>> _keylist;
	
	public Client(Socket s, ArrayList<Pair<Socket,String>> _loginlist, ArrayList<Pair<String,String>> _userlist, ArrayList<Pair<String,String>> _grouplist, ArrayList<Pair<String,String>> _keylist)
	{
		socket = s;//INSTANTIATE THE SOCKET)
                this._loginlist = _loginlist;
                this._userlist = _userlist;
                this._grouplist = _grouplist;
                this._keylist = _keylist;
	}
	
	@Override
	public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
	{
		try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
		{
			Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
			
                        InputStream is=socket.getInputStream();
                        OutputStream os=socket.getOutputStream();
                        
                        int k=0;
                        while (true)//WHILE THE PROGRAM IS RUNNING
			{		
				if (is.available()!=0)
				{
                                    System.out.println("\n");
                                     String input="";
                                     while(is.available()!=0){
                                         byte[] buff=new byte[16];
                                         is.read(buff);
                                         input+=AES.decryption(buff, key);
                                     }
                                     input=input.trim();
                                     //System.out.println("Inputan : "+input);
					//String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//					System.out.println("Client Said: " + input);//PRINT IT OUT TO THE SCREEN
//					out.println("You Said: " + input);//RESEND IT TO THE CLIENT
//					out.flush();//FLUSH THE STREAM
                                        
                                       /* if (input.split(" ")[0].toLowerCase().toLowerCase().equals("firstkey") == true) {
                                            System.out.println("masuk firstkey");
                                            String plaintext = keyGen();
                                            byte[] b = AES.encryption(plaintext, firstkey);
                                            String str = new String(b, StandardCharsets.UTF_8);
                                            System.out.println(str);
                                            out.println(str);
                                            out.flush();
                                            String decrypted = decrypt(b, firstkey);
                                            System.out.println(decrypted);
                                        }*/
                                       
                                        // param LOGIN <userName> <pass>
                                        if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                            String[] vals = input.split(" ");

                                            if (this._userlist.contains(new Pair(vals[1], vals[2])) == true) {
                                                if (this.login == false) {
                                                    this._loginlist.add(new Pair(this.socket, vals[1]));
                                                    this.username = vals[1];
                                                    this.login = true;
                                                    System.out.println("Users count: " + this._loginlist.size());
                                                    String key2=keyGen();
                                                    k=1;
                                                    byte[] outing=AES.encryption("SUCCESS login "+key2, key);
                                                    //out.println("SUCCESS login");
                                                    //out.flush();
                                                    os.write(outing);
                                                    os.flush();
                                                    key=key2;
                                                    _keylist.add(new Pair(username,key));
                                                } else {
                                                    byte[] outing=AES.encryption("FAIL login", key);
                                                    //out.println("FAIL login");
                                                    //out.flush();
                                                    os.write(outing);
                                                    os.flush();
                                                }
                                            } else {
                                                byte[] outing=AES.encryption("FAIL login", key);
                                                //out.println("FAIL login");
                                                //out.flush();
                                                os.write(outing);
                                                os.flush();
                                            }
                                        }

                                        // param LOGOUT
                                        if (input.split(" ")[0].toLowerCase().equals("logout") == true) {
                                            String[] vals = input.split(" ");

                                            if (this._loginlist.contains(new Pair(this.socket, this.username)) == true) {
                                                this._loginlist.remove(new Pair(this.socket, this.username));
                                                System.out.println(this._loginlist.size());
                                                byte[] outing=AES.encryption("FAIL logout", key);
                                                //out.println("FAIL login");
                                                //out.flush();
                                                os.write(outing);
                                                os.flush();
                                                this.socket.close();
                                                break;
                                            } else {
                                                byte[] outing=AES.encryption("FAIL logout", key);
                                                //out.println("FAIL login");
                                                //out.flush();
                                                os.write(outing);
                                                os.flush();
                                            }
                                        }

                                        // param PM <userName dst> <message>
                                        if (input.split(" ")[0].toLowerCase().equals("pm") == true) {
                                            String[] vals = input.split(" ");

                                            boolean exist = false;
                                            OutputStream osDest=null;
                                            for(Pair<Socket, String> cur : _loginlist) {
                                                if (cur.getSecond().equals(vals[1])) {
                                                    //PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                    osDest = cur.getFirst().getOutputStream();
                                                    String messageOut = "";
                                                    for (int j = 2; j<vals.length; j++) {
                                                        messageOut += vals[j] + " ";
                                                    }
                                                    System.out.println(this.username + " to " + vals[1] + " : " + messageOut);
                                                    //outDest.println(this.username + ": " + messageOut);
                                                    //outDest.flush();
                                                    String pesan=this.username + " : " + messageOut;

                                                    for(Pair<String,String> cue : _keylist){
                                                        if(cue.getFirst().equals(cur.getSecond())){
                                                            String keyout=cue.getSecond();
                                                            //System.out.println("panjang yg mau dikirim : "+pesan.length());
                                                            byte[] outing=AES.encryption(pesan, keyout);
                                                            osDest.write(outing);
                                                            osDest.flush();
                                                        }
                                                    }
                                                    exist = true;
                                                }
                                            }
                                            if (exist == false) {
                                                System.out.println("pm to " + vals[1] + " by " + this.username + " failed.");
                                                //out.println("FAIL pm");
                                                //out.flush();
                                                byte[] outing=AES.encryption("FAILPM", key);
                                                osDest.write(outing);
                                                osDest.flush();
                                            }
                                        }

                                        // param CG <groupName>
                                        if (input.split(" ")[0].toLowerCase().equals("cg") == true) {
                                            String[] vals = input.split(" ");

                                            boolean exist = false;

                                            for(Pair<String, String> selGroup : _grouplist) {
                                                if (selGroup.getFirst().equals(vals[1])) {
                                                    exist = true;
                                                }
                                            }

                                            if(exist == false) {
                                                Group group = new Group();
                                                int total = group.updateGroup(vals[1], this.username, _grouplist);
                                                System.out.println("total group: " + total);
                                                System.out.println("cg " + vals[1] + " by " + this.username + " successed.");
                                                //out.println("SUCCESS cg");
                                                //out.flush();
                                                byte[] outing=AES.encryption("SUCCESS cg", key);
                                                os.write(outing);
                                                os.flush();
                                            } else {
                                                System.out.println("cg " + vals[1] + " by " + this.username + " failed.");
                                                //out.println("FAIL cg");
                                                //out.flush();
                                                byte[] outing=AES.encryption("FAIL cg", key);
                                                os.write(outing);
                                                os.flush();
                                            }
                                        }

                                        // param GM <groupName> <message>
                                        if (input.split(" ")[0].toLowerCase().equals("gm") == true) {
                                            String[] vals = input.split(" ");

                                            boolean exist = false;

                                            for(Pair<String, String> selGroup : _grouplist) {
                                                if (selGroup.getSecond().equals(this.username)) {
                                                    exist = true;
                                                }
                                            }

                                            if (exist == true) {
                                                for(Pair<String, String> selGroup : _grouplist) {
                                                    if (selGroup.getFirst().equals(vals[1])) {
                                                        for(Pair<Socket, String> cur : _loginlist) {
                                                            if (cur.getSecond().equals(selGroup.getSecond()) && !cur.getFirst().equals(socket)) {
                                                                //PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                                OutputStream osDest=cur.getFirst().getOutputStream();
                                                                String messageOut = "";
                                                                for (int j = 2; j<vals.length; j++) {
                                                                    messageOut += vals[j] + " ";
                                                                }
                                                                System.out.println(this.username + " to " + vals[1] + " group: " + messageOut);
                                                                String pesan=this.username + " @ " + vals[1] + " group: " + messageOut;
                                                                //outDest.println();
                                                                //outDest.flush();
                                                                for(Pair<String,String> cue : _keylist){
                                                                    if(cue.getFirst().equals(cur.getSecond())){
                                                                        String keyout=cue.getSecond();
                                                                        byte[] outing=AES.encryption(pesan, keyout);
                                                                        osDest.write(outing);
                                                                        osDest.flush();
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            } else {
                                                System.out.println("gm to " + vals[1] + " by " + this.username + " failed.");
                                                //out.println("FAIL gm");
                                                //out.flush();
                                                byte[] outing=AES.encryption("FAIL gm", key);
                                                os.write(outing);
                                                os.flush();
                                            }
                                        }

                                        // param BM <message>
                                        if (input.split(" ")[0].toLowerCase().equals("bm") == true) {
                                            String[] vals = input.split(" ");

                                            for(Pair<Socket, String> cur : _loginlist) {
                                                if (!cur.getFirst().equals(socket)) {
                                                    OutputStream osDest=cur.getFirst().getOutputStream();
                                                    String messageOut = "";
                                                    for (int j = 1; j<vals.length; j++) {
                                                        messageOut += vals[j] + " ";
                                                    }
                                                    String pesan=this.username + " <BROADCAST>: " + messageOut;
                                                    System.out.println(this.username + " to alls: " + messageOut);
                                                    //outDest.println(this.username + " <BROADCAST>: " + messageOut);
                                                    //outDest.flush();
                                                    for(Pair<String,String> cue : _keylist){
                                                        if(cue.getFirst().equals(cur.getSecond())){
                                                            String keyout=cue.getSecond();
                                                            byte[] outing=AES.encryption(pesan, keyout);
                                                            osDest.write(outing);
                                                            osDest.flush();
                                                        }
                                                    }
                                                }
                                            }
                                        }

				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
		}	
	}
        
    private static String keyGen() {
        char[] chars = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String tese= sb.toString();
        //System.out.println(tese);
        return tese;
    }
}


