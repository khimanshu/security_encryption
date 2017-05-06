/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainclass;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

import sun.misc.BASE64Encoder;
//Reference: http://www.java2s.com/Tutorial/Java/0490__Security/RSASignatureGeneration.htm
public class MainClass {
  public static void main(String[] args) throws Exception {
      //Initialize and generate RSA 1024 key pair
    int start=(int) System.currentTimeMillis();
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    KeyPair keyPair = kpg.genKeyPair();

    //Text to encode using private key
    byte[] data = "Poornima is presenting".getBytes("UTF8");
    int stop=(int) System.currentTimeMillis();
    System.out.println("Time for generation: "+ (stop-start));
    Signature sig = Signature
            .getInstance("MD5WithRSA");
    //Sign using the private key
    start=(int) System.currentTimeMillis();
    sig.initSign(keyPair.getPrivate());
    sig.update(data);
    byte[] signatureBytes = sig.sign();
    stop=(int) System.currentTimeMillis();
    System.out.println("Time for digital signing: "+ (stop-start));
    System.out.println("Signing the message: \""+new String(data)+"\" :" + new BASE64Encoder().encode(signatureBytes));
    
    //Verify the data using public key
    start=(int) System.currentTimeMillis();
    sig.initVerify(keyPair.getPublic());
    sig.update(data);
    stop=(int) System.currentTimeMillis();
    System.out.println("Verify using public key: "+sig.verify(signatureBytes));
    System.out.println("Time for verifying: "+ (stop-start));
  }
}