package com.orion.mdd.mddapi.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtils {

	/**
	 * Load the private key and adapt it to PKCS8 standard
	 */
	public static PrivateKey loadPrivateKey() throws Exception {
		String key = Files.readAllLines(Paths.get("src/main/resources/keys/private1.pem"))
				.stream()
				.filter(line -> !line.startsWith("-----"))
				.reduce("", (a, b) -> a + b)
				.replaceAll("\\s", "");
		byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(spec);
	}

	/**
	 * Load the public key and adapt it to X509 standard
	 */
	public static PublicKey loadPublicKey() throws Exception {
		String key = Files.readAllLines(Paths.get("src/main/resources/keys/public1.pem"))
				.stream()
				.filter(line -> !line.startsWith("-----"))
				.reduce("", (a, b) -> a + b)
				.replaceAll("\\s", "");
		byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(spec);
	}
}