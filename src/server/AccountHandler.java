package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AccountHandler {

	private SecureRandom saltGenerator;
	public static final int SALT_BYTE_SIZE = 32;
	public static final int HASH_BYTE_SIZE = 32;
	public static final int PBKDF2_ITERATIONS = 10000;

	// public boolean validateUserInformation(String user, String ){
	//
	//
	// return false;
	// }

	// public boolean createNewUser(String user, String password){
	// return false;
	//
	//
	//
	// }

	public static void main(String arg[]) {
		new AccountHandler();
	}

	public AccountHandler() {
		saltGenerator = new SecureRandom();

		System.out.println(byteToString(hash("password", generateSaltValue())));

	}

	// genererar ett nytt salt värde
	private byte[] generateSaltValue() {
		byte[] salt = new byte[SALT_BYTE_SIZE];
		saltGenerator.nextBytes(salt);
		return salt;
	}

	/**
	 * använder PBKDF för att hasha fram ett hashvärde mha av saltet och
	 * lösenordet. se
	 * https://crackstation.net/hashing-security.htm#javasourcecode för exempel
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
