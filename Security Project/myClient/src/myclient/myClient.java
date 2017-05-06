/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
/*
 * Client program
 */
public class myClient extends JFrame{
	public static int port;
	public static String IPAddress;
	public Socket clientSocket;
	public static myClient frame;
	
	static ObjectInputStream inputStream = null;
	static EncryptionUtil enc;
	public myClient(String Name)
	{
		super(Name);
		
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
    public static void main(String[] args) throws Exception
    {
    	//Create an object and call the method run()
        frame = new myClient("Secure DNS");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        //Set up the content pane.
        pane.setLayout(null);
        
        JTextField IPInput = new JTextField("Enter IP address");
        JTextField portInput = new JTextField("Port");
        JButton buttonConnect = new JButton("Connect");
 
        pane.add(IPInput);
        pane.add(portInput);
        pane.add(buttonConnect);
 
        Insets insets = pane.getInsets();
        Dimension size = IPInput.getPreferredSize();
        IPInput.setBounds(25 + insets.left, 5 + insets.top,
                     size.width, size.height);
        size = portInput.getPreferredSize();
        portInput.setBounds(55 + insets.left, 40 + insets.top,
                     size.width, size.height);
        size = buttonConnect.getPreferredSize();
        buttonConnect.setBounds(150 + insets.left, 15 + insets.top,
                     size.width + 50, size.height + 20);
        buttonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                port=Integer.parseInt(portInput.getText());
                IPAddress=IPInput.getText();
                
                
                
                
               try {        
            	  
            	    //callEncryption();
					frame.run();					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }});

			
        //Size and display the window.
        Insets insets1 = frame.getInsets();
        frame.setSize(300 + insets1.left + insets1.right,
                      125 + insets1.top + insets1.bottom);
        frame.setVisible(true);
      
        

    }

    public void run() throws Exception
    {
    	

    	
    	//Create Socket,connect
        clientSocket = new Socket(IPAddress,port);
        //Sends message to the server
        System.out.println("Hello! Enter the IP address or hostname for resolution: ");
        PrintStream ps = new PrintStream(clientSocket.getOutputStream());
        //Scanner class to receive input from user
        Scanner scan = new Scanner(System.in);
        scan.useDelimiter("[\r\n]+");
        String cMessage ="";
        InputStreamReader ir = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader br = new BufferedReader(ir);
        //Terminate program when you send "END"
        while(!(cMessage.trim().equals("END"))){
        	//Receive input from user
        	cMessage = scan.nextLine();
        	  /*inputStream = new ObjectInputStream(new FileInputStream(enc.PUBLIC_KEY_FILE));
		      final PublicKey publicKey = (PublicKey) inputStream.readObject();
		      cMessage = new String((enc.encrypt(cMessage, publicKey)));
                      
		      System.out.println("yo"+cMessage+"yo");*/
		      
                         cMessage = AESencrp.encrypt(cMessage);
                         System.out.println("Encrypted Text: "+ cMessage);
             
        	if(cMessage.trim().equals("END"))
        	{
        		break;
        	}
        	//Prevent from sending empty input to server
        	if (cMessage.trim().isEmpty()) {
        		continue;
        	}
        	//Send data to server
        	ps.println(cMessage);
        	ps.flush();
        	
        	//Read and display response from server
        	String message = br.readLine().trim();
        	System.out.println(clientSocket.getLocalSocketAddress()+message);
        }
        System.out.println("Terminating program");
        //Close sockets
        br.close();
        ir.close();
        scan.close();
        ps.close();
        clientSocket.close();
    }

}