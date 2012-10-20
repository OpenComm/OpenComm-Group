import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


public class EncryptAsymmetric_Divya {
	private static KeyPair key;
	
	//the constructor call generates a keyPair 
	public EncryptAsymmetric_Divya() throws NoSuchAlgorithmException, NoSuchProviderException{
		KeyPairGenerator keygen= KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keygen.initialize(1024,random);
		key = keygen.generateKeyPair();
	}

	public static byte[] encryptString (String s) throws Exception {
		byte[] encrypted= new byte[2048];
		Cipher cipher;
		PublicKey publickey=key.getPublic();
		cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publickey);
		encrypted=cipher.doFinal(s.getBytes());
		System.out.println(encrypted);
		return encrypted;
	}

	public static String decryptString(byte[] input) throws Exception{
		Cipher cipher;
		byte[] decrypted=input;
		PrivateKey privatekey=key.getPrivate();
		cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privatekey);
		decrypted=cipher.doFinal(input);
		return new String(decrypted,"utf-8");
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	//Checks if both functions are working fine 
	public static void main(String[] args) throws Exception {
		EncryptAsymmetric_Divya e=new EncryptAsymmetric_Divya();
		byte[] b=encryptString("Divya");
		String s=decryptString(b);
		System.out.println( s);
	}

}
