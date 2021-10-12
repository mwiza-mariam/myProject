package LanguageDao;

import encryptionClass.Bcrypt;



public class PasswordProtection {
	private static String salt = "$2a$10$QPtfISJUaoBeXGJNDPB8he";
	
	public static String hashPassword(String password) {
		String passwordHashed=Bcrypt.hashpw(password, salt);
		return passwordHashed;
	}
	
	public static boolean verifyPassword(String password, String hash) {
		boolean f = Bcrypt.checkpw(password, hash);
		return f;
	}
}
