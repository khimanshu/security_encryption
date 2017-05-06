/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aesmain;

/**
 *
 * @author Akash
 */
public class AESMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        String password = "Our Hero";
        int start=(int) System.currentTimeMillis();
        String passwordEnc = AESencrp.encrypt(password);
          int stop=(int) System.currentTimeMillis();
        System.out.println("Time for encryption: "+ (stop-start));
        start=(int) System.currentTimeMillis();
        String passwordDec = AESencrp.decrypt(passwordEnc);
        stop=(int) System.currentTimeMillis();
        System.out.println("Time for decryption: "+ (stop-start));
        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
    }
    
}
