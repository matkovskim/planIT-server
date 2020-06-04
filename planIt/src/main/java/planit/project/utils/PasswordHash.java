package planit.project.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {

	public static String hashPassword(String password, String salt) {
		String hashedPassword = BCrypt.hashpw(password, salt);
		return hashedPassword;
	}

	public static String generateSalt() {
		return BCrypt.gensalt(12);
	}

}
