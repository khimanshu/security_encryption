/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client1;

import java.net.*;
import java.io.*;
import java.security.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

class Rsa
{
  private BigInteger n, d, e;

  public Rsa(int bitlen) throws NoSuchAlgorithmException
  {
    //SecureRandom r = SecureRandom.getInstance("76ce8e44");
    BigInteger p = new BigInteger("7436443145513102759346082091504489018965649844427578539182610598215255526295861138810419680153488731851639356319558363771772272791601047612382871111278903"
);
    BigInteger q = new BigInteger("11715268389605343181133862358547399940348888325990729438639194032209526141405821547230226288689002408289433770695761015700029788589235283111801996922104979");
    n = p.multiply(q);
	//System.out.println("Value of r is "+r);
    BigInteger m = (p.subtract(BigInteger.ONE))
                   .multiply(q.subtract(BigInteger.ONE));
    e = new BigInteger("3");
    while(m.gcd(e).intValue() > 1) e = e.add(new BigInteger("2"));
    d = e.modInverse(m);
  }

public String encrypt(String message) {
    return (new BigInteger(message.getBytes())).modPow(e, n).toString();
  }
public String decrypt(String message) {
    return new String((new BigInteger(message)).modPow(d, n).toByteArray());
  }
	 
  public BigInteger encrypt(BigInteger message)
  {
    return message.modPow(e, n);
  }
  public BigInteger decrypt(BigInteger message)
  {
    return message.modPow(d, n);
  }
}


public class Client1
{
 public static String MD5(String text) throws NoSuchAlgorithmException{
       
       MessageDigest m=MessageDigest.getInstance("MD5");
       m.update(text.getBytes(),0,text.length());
       
       return (new BigInteger(1,m.digest()).toString(16));
        
    }

   public static void main(String [] args) throws NoSuchAlgorithmException, Exception
   {
	Rsa a = new Rsa(1024);
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      try
      {
         System.out.println("Connecting to " + serverName +
		 " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to " 
		 + client.getRemoteSocketAddress());
	 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         OutputStream os = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(os);
	 String s;
	 System.out.println("Enter # to exit\n\n");
		do{
	 System.out.print("Client: ");
	 s = br.readLine();
         int start=(int) System.currentTimeMillis();
         String myMD5 = MD5(s);
         System.out.println("MD5: "+myMD5);
         s=s+"+"+myMD5;
         int stop=(int) System.currentTimeMillis();
         System.out.println("Time for hashing: "+ (stop-start));
         
         start=(int) System.currentTimeMillis();
          s = AESencrp.encrypt(s);
          System.out.println("Encrypted using AES: "+ s);
	 String enc = a.encrypt(s);
         System.out.println("Encrypted using RSA: "+ enc);
         stop=(int) System.currentTimeMillis();
         System.out.println("Time for encryption: "+ (stop-start));
	 out.writeUTF(enc);
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                        new DataInputStream(inFromServer);
	 String t = in.readUTF();
         start=(int) System.currentTimeMillis();
	 String dec = a.decrypt(t);
         System.out.println("Decrypted using RSA: "+ dec);
         dec = AESencrp.decrypt(dec);
         System.out.println("Decrypted using AES: "+ dec);
         stop=(int) System.currentTimeMillis();
           System.out.println("Time for decryption: "+ (stop-start));
         System.out.println("Server: " + dec);
	 }while(!s.equals('#'));
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
