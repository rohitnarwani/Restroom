package com.locatar.restroom.common;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class SecurityHelper {

	public static String createSecurePassword(String key) {
		String hash = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			byte raw[] = messageDigest.digest(key.getBytes());
			messageDigest.update(key.getBytes());
			// messageDigest.update(getNounce.getBytes("UTF-8"));
			hash = new String(Base64.encodeBase64(messageDigest.digest(raw)));
			messageDigest.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;

	}

	public static void main(String[] args) {
		String password = "Pass@123";
		String generatedPassword = createSecurePassword(password);
		if ("Tdcq0CssF2KDTbKA1UJAvgsK47hBbl8esoC2EyBL3ODwo5xIsHr3w8uBUqBDbhxSSJXYUjDmAyYJkRQFqlKQmA=="
				.equals(generatedPassword)) {
		}
	}

}
