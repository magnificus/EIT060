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
	 * @param användarinfosomjagförtillfälletskiteri
	 * @return
	 */
	public boolean createNewAccount(String password, String användarinfo){
		
		
		byte[] salt = generateSaltValue();
		byte[] hash = hash(password, salt);
		
		//användarInfo, salt, hash sparas i databas på något sätt
		
		return true;
	}
	
	/**
	 * autentiserar användaren
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean authenticateUser(String userId,String password){
		
		byte[] salt= null;
		byte[] hashStored = null;
		
		//med hjälp av userId hämtas salt och hashat lösen från databas
		
		//hash räknas ut med hjälp av salt och password
		byte[] hash = hash(password, salt);
		
		//kollar om de e samma
		if(hash.equals(hashStored)){
			return true;
		}
		return false;
		
	}
	
	
	

	/**
	 * genererar ett nytt salt värde
	 * @return
	 */
	private byte[] generateSaltValue() {
		// se http://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html
		byte[] salt = new byte[SALT_BYTE_SIZE];
		saltGenerator.nextBytes(salt);
		return salt;
	}


	/**
	 * använder PBKDF för att hasha fram ett hashvärde mha av saltet och
	 * lösenordet. se
	 * https://crackstation.net/hashing-security.htm#javasourcecode för exempel
	 * Har inte själv full koll på det riktigt
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
