import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.Security;

public class SymmetricEncrypter {

	private static String algorithm = "AES";
	private static SecretKey key = null;
	private static Cipher cipher = null;

	private static void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());  //bouncycastle provider used here
		key = KeyGenerator.getInstance(algorithm).generateKey();
		cipher = Cipher.getInstance(algorithm);
	}


	public static void main(String[] args) 
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		setUp();
		byte[] encryptionBytes = null;
		KeyGenerator keygen = KeyGenerator.getInstance("AES");//.generateKey();
		keygen.init(128);
		key=keygen.generateKey();
		cipher = Cipher.getInstance("AES");
		String input = "what is your name";
		System.out.println(input);
		encryptionBytes = encrypt(input);
		System.out.println(encryptionBytes);
		System.out.println( decrypt(encryptionBytes));
	}

	private static byte[] encrypt(String input)
			throws InvalidKeyException, 
			BadPaddingException,
			IllegalBlockSizeException {
		Security.addProvider(new BouncyCastleProvider());  //bouncy castle provider
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	private static String decrypt(byte[] encryptionBytes)
			throws InvalidKeyException, 
			BadPaddingException,
			IllegalBlockSizeException {
		Security.addProvider(new BouncyCastleProvider());   //bouncycastle provider
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
		String recovered = new String(recoveredBytes);
		return recovered;
	}
}
