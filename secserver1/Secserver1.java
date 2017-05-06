/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secserver1;

import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.net.*;
import java.io.*;
import java.security.*;
import java.math.BigInteger;
import java.security.SecureRandom;

class Rsa
{
  private BigInteger n, d, e;

  public Rsa(int bitlen) throws NoSuchAlgorithmException
  {
   //SecureRandom r = new SecureRandom();
	//System.out.println("r value is "+r);
    BigInteger p = new BigInteger("7436443145513102759346082091504489018965649844427578539182610598215255526295861138810419680153488731851639356319558363771772272791601047612382871111278903"	);
    BigInteger q = new BigInteger("11715268389605343181133862358547399940348888325990729438639194032209526141405821547230226288689002408289433770695761015700029788589235283111801996922104979");
	//System.out.println("Values of P and Q are "+p+"\n"+q);
	//System.out.println("Value of r is "+r);
    n = p.multiply(q);
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


class Secserver1{
    public static String MD5(String text) throws NoSuchAlgorithmException{
       
       MessageDigest m=MessageDigest.getInstance("MD5");
       m.update(text.getBytes(),0,text.length());
       System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
       return (new BigInteger(1,m.digest()).toString(16));
        
    }
   public static void main(String [] args) throws NoSuchAlgorithmException
   {
	Rsa a = new Rsa(1024);
	try{
      int port = Integer.parseInt(args[0]);
         ServerSocket serverSocket = new ServerSocket(port);
		
                System.out.println(serverSocket.getLocalPort() + "...");
		   Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
	     DataOutputStream sout =
                 new DataOutputStream(server.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		System.out.println("Enter # to quit\n\n");
		String f,g;
	    do
		{
		String t=in.readUTF();
		if(t.length()>1)
                {
                //t=t+"+"+MD5(t);
                  int  start=(int) System.currentTimeMillis();
                f = a.decrypt(t);
                System.out.println("Decrypted using RSA: "+ f);
                f = AESencrp.decrypt(f);
                System.out.println("Decrypted using AES: "+ f);
                int stop=(int) System.currentTimeMillis();
           System.out.println("Time for decryption: "+ (stop-start));
                System.out.println("Client: "+f);
                System.out.print("Server: ");
                }
		s = br.readLine();
                int start=(int) System.currentTimeMillis();
                 s = AESencrp.encrypt(s);
                 System.out.println("Encrypted using AES: "+ s);
                g = a.encrypt(s);
                System.out.println("Encrypted using RSA: "+ g);
        int stop=(int) System.currentTimeMillis();
           System.out.println("Time for encryption: "+ (stop-start));
	    sout.writeUTF(g);
		}while(!s.equals('#'));
            server.close();
	
}
catch(Exception e){
System.out.println(e);
}
}
}
