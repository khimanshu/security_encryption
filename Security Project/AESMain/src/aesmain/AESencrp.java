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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class AESencrp {
    
     private static final String ALGO = "AES";
     static byte[] keyValue = 
        new byte[] { 'D', 'R', 'F', 'E', 'N', 'G', 'Z',
'H', 'U', 'A', 'W','E', 'S', 'O', 'M', 'E' };

public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        System.out.println("Our Key is: "+new String(keyValue));
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        int start=(int) System.currentTimeMillis();
        Key key = new SecretKeySpec(keyValue, ALGO);
        int stop=(int) System.currentTimeMillis();
        System.out.println("Time for generation: "+(stop-start));
        return key;
}

}
