/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Akash
 */
public class Socket_server {
static ObjectInputStream inputStream = null;
	static EncryptionUtil enc;
    public static void main(String[] args) throws Exception
    {
        Socket_server myServer = new Socket_server();
        //callEncryption();
        myServer.run();
    }

    public static void callEncryption()
	{
		enc=new EncryptionUtil();
		 try {

		      // Check if the pair of keys are present else generate those.
		      if (!enc.areKeysPresent()) {
		        // Method generates a pair of keys using the RSA algorithm and stores it
		        // in their respective files
		        enc.generateKey();
		      }

		      final String originalText = "Text to be encrypted ";
		      

		      // Encrypt the string using the public key
		      inputStream = new ObjectInputStream(new FileInputStream(enc.PUBLIC_KEY_FILE));
		      final PublicKey publicKey = (PublicKey) inputStream.readObject();
		      final byte[] cipherText = enc.encrypt(originalText, publicKey);

		      // Decrypt the cipher text using the private key.
		      inputStream = new ObjectInputStream(new FileInputStream(enc.PRIVATE_KEY_FILE));
		      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		      final String plainText = enc.decrypt(cipherText, privateKey);

		      // Printing the Original, Encrypted and Decrypted Text
		      System.out.println("Original: " + originalText);
		      System.out.println("Encrypted: " +cipherText.toString());
		      System.out.println("Decrypted: " + plainText);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
    public void run() throws Exception
    {
    	int port = 10030;
    	File file = new File("C:\\Users\\Akash\\workspace1\\Socket_server\\src\\Host.txt");
    	BufferedReader reader = null;

    	try {
    	    reader = new BufferedReader(new FileReader(file));
    	       	    
    	} catch (IOException e) {
        }
        //Initializes the port the serverSocket will be on
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("The Server is waiting for a client on port "+port);
        //Accepts the connection for the client socket
        Socket socket = serverSocket.accept();

        InputStreamReader ir = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(ir);
        String message;
        //= br.readLine();
        //Confirms that the message was received

 
        PrintStream ps = new PrintStream(socket.getOutputStream());
        while((message =br.readLine())!=null)
         {
       
        	int flag=0;
        	int i=0;
                
        	 /* inputStream = new ObjectInputStream(new FileInputStream(enc.PRIVATE_KEY_FILE));
		      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		      System.out.println(message);
		      byte[] mellow= message.getBytes();
		      
		      System.out.println(mellow);
		      System.out.println(new String(mellow));
		      message= enc.decrypt(message.getBytes(), privateKey);*/
                System.out.println(message);
                message=message.trim();
             
            String passwordDec = AESencrp.decrypt(message);
            

            System.out.println("Encrypted Text : " + message);
            
            System.out.println("Decrypted Text : " + passwordDec);
            message=passwordDec;
             System.out.println("Message from: "+ socket.getRemoteSocketAddress()+":"+socket.getPort()+"-"+message);
           
             String pattern = "\\D+\\.\\D+\\.\\D+";
             String pattern1 = "\\d+\\.\\d+\\.\\d+\\.\\d+";
             String pattern2 = "(\\D+\\.\\D+\\.\\D+)";
             String pattern3 = "(\\d+\\.\\d+\\.\\d+\\.\\d+)";
             // Create a Pattern object
             Pattern r = Pattern.compile(pattern);
             Pattern r1 = Pattern.compile(pattern1);
             Pattern r2 = Pattern.compile(pattern2);
             Pattern r3 = Pattern.compile(pattern3);
             // Now create matcher object.
             Matcher m = r.matcher(message);
             Matcher m1 = r1.matcher(message);
             Matcher m2;
             Matcher m3;
             if(m.matches()==true)
             {      
            	 String Line;
            	 while ((Line = reader.readLine()) != null) {
            		 System.out.println(Line);
 						m2=r2.matcher(Line);
 						m3=r3.matcher(Line);
 						if(m2.find()&&m3.find())
 						{
 							//System.out.println("yo"+message+m2.group(0)+m3.group(0));
 								if(m2.group(0).trim().equals(message)){
 									//System.out.println("yo"+m3.group(0));
 									ps.println("IP Address: "+m3.group(0));
 									flag=1;
 									break;
 									
 								}
 								
 								
 						}
            	 }
            	 if(flag==0)
            	 {
            		 ps.println("Could not find IP Address: ");
            		 flag=0;
            	 }
            	reader.close();
 				reader = new BufferedReader(new FileReader(file));
            	 
             }
             else if(m1.matches()==true)
             {
            	 //ps.println("ip address");
            	 String Line;
                
				while ((Line = reader.readLine()) != null) {
						m2=r2.matcher(Line);
						m3=r3.matcher(Line);
						if(m2.find()&&m3.find())
						{
								if(m3.group(0).trim().equals(message)){
									//System.out.println("yo"+m3.group(0));
									ps.println("Hostname: "+m2.group(0));
									flag=1;
									break;
								}
								
								
						}
					
            	 }
				if(flag==0)
           	 {
           		 ps.println("Could not find IP Address: ");
           		 flag=0;
           	 }
				reader.close();
				reader = new BufferedReader(new FileReader(file));
             }
             else
             {	
                 ps.println(message+ " is an invalid input");
             }  
             
        }
        System.out.println("Connection closed");
        ps.close();
        br.close();
        ir.close();
        serverSocket.close();
    }
    
}
