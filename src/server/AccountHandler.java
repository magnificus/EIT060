package server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AccountHandler {

	public static final int SALT_BYTE_SIZE = 32;
	public static final int HASH_BYTE_SIZE = 32;
	public static final int PBKDF2_ITERATIONS = 10000;
	
	private SecureRandom saltGenerator;
	
	public AccountHandler() {
		saltGenerator = new SecureRandom();
	}
	
	/**
	 * 
	 * @param password
	 * @param anv�ndarinfosomjagf�rtillf�lletskiteri
	 * @return
	 */
	public boolean createNewAccount(String password, String anvandarinfo){
		
		
		byte[] salt = generateSaltValue();
		byte[] hash = hash(password, salt);
		
		//anv�ndarInfo, salt, hash sparas i databas p� n�got s�tt
		
		return true;
	}
	
	/**
	 * autentiserar anv�ndaren
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean authenticateUser(String userId,String password){
		
		byte[] salt= null;
		byte[] hashStored = null;
		
		//med hj�lp av userId h�mtas salt och hashat l�sen fr�n databas
		
		//hash r�knas ut med hj�lp av salt och password
		byte[] hash = hash(password, salt);
		
		//kollar om de e samma
		if(hash.equals(hashStored)){
			return true;
		}
		return false;
		
	}
	
	
	

	/**
	 * genererar ett nytt salt v�rde
	 * @return
	 */
	private byte[] generateSaltValue() {
		// se http://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html
		byte[] salt = new byte[SALT_BYTE_SIZE];
		saltGenerator.nextBytes(salt);
		return salt;
	}


	/**
	 * anv�nder PBKDF f�r att hasha fram ett hashv�rde mha av saltet och
	 * l�senordet. se
	 * https://crackstation.net/hashing-security.htm#javasourcecode f�r exempel
	 * Har inte sj�lv full koll p� det riktigt
	 * @param password
	 * @param salt
	 * @return
	 */
	private byte[] hash(String password, byte[] salt) {

		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt,
				PBKDF2_ITERATIONS, HASH_BYTE_SIZE * 8);
		SecretKeyFactory skf;

		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * konverterar en byte array till en String
	 * @param array
	 * @return
	 */
	private String byteToString(byte[] array) {
		
		StringBuilder sb = new StringBuilder();
		
		for (byte b : array) {
			sb.append(String.format("%02X", b));
			//System.out.printf("%02X", b);
			// System.out.print(Integer.toHexString(b));
		}
		return sb.toString();
	}

}
