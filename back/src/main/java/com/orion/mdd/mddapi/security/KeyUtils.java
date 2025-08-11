package com.orion.mdd.mddapi.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Utilitaire pour le chargement des clés RSA privées et publiques à partir de
 * fichiers PEM stockés dans les ressources de l'application.
 * <p>
 * Les clés sont décodées et converties dans les formats standards PKCS8
 * (privée) et X509 (publique) pour être utilisées dans les opérations
 * cryptographiques.
 * </p>
 */
public class KeyUtils {

	/**
	 * Charge la clé privée RSA depuis un fichier PEM, puis l'adapte au format
	 * PKCS8.
	 *
	 * @return la clé privée RSA au format {@link PrivateKey}
	 * @throws Exception en cas d'erreur de lecture ou de décodage du fichier
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
	 * Charge la clé publique RSA depuis un fichier PEM, puis l'adapte au format
	 * X509.
	 *
	 * @return la clé publique RSA au format {@link PublicKey}
	 * @throws Exception en cas d'erreur de lecture ou de décodage du fichier
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