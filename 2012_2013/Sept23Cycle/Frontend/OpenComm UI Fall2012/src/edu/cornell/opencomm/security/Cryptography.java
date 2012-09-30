package edu.cornell.opencomm.security;

/**
 * For Client- and Server- Side.
 * This class will handle all encryption, decryption, and hashing functionality
 *
 */
public class Cryptography {

	/**
	 * TODO: set up keys depending on if it is client or server.
	 */
	public Cryptography(){
	}
	
	/**
	 * TODO: Should run the protocol to 
	 * 		authenticate a user to the server.
	 * @param TODO: we will need to add the necessary parameters
	 * @return return true if successful or false if the process failed at some point
	 */
	public boolean enrollCryptoProtocol(){
		return false;
	}
	
	/**
	 * TODO: Should encrypt the inputed value.  These types may change based on 
	 * 		the packets and encryption algorithm.
	 * @param s the value to be encrypted
	 * @return the encrypted value
	 */
	public byte[] encrypt(String s){
		return new byte[0];
	}
	
	/**
	 * TODO: Should decrypt the inputed value.  These types may change based on
	 * 		the packets and encryption algorithm.
	 * @param b the value to be decrypted
	 * @return the decrypted value
	 */
	public String decrypt(byte[] b){
		return new String("");
	}
	
	/**
	 * TODO: Should apply a hash function to the inputed value. These types will most 
	 * 		likely change when the function is implemented.
	 * @param s the value to be hashed
	 * @return the hashed value
	 */
	public String hash(String s){
		return new String("");
	}
	
}
