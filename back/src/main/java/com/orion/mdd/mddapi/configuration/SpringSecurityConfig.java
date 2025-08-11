package com.orion.mdd.mddapi.configuration;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.orion.mdd.mddapi.security.JWTFilter;
import com.orion.mdd.mddapi.security.KeyUtils;
import com.orion.mdd.mddapi.services.CustomUserDetailsService;

import jakarta.annotation.PostConstruct;

/**
 * Configuration Spring Security pour l'application.
 * <p>
 * Cette classe configure :
 * <ul>
 * <li>Le chargement des clés RSA privées et publiques pour JWT</li>
 * <li>Le provider d'authentification DAO avec UserDetailsService et BCrypt</li>
 * <li>Le gestionnaire d'authentification</li>
 * <li>La chaîne de filtres de sécurité HTTP, incluant la gestion des CORS, la
 * désactivation du CSRF, la session stateless, et les règles
 * d'autorisation</li>
 * <li>La configuration CORS autorisant uniquement l'origine
 * http://localhost:4200</li>
 * <li>Le bean JwtDecoder et JwtEncoder utilisant les clés RSA</li>
 * </ul>
 * </p>
 */
@Configuration
public class SpringSecurityConfig {

	private PublicKey rsaPublicKey;
	private PrivateKey rsaPrivateKey;

	@Autowired
	private JWTFilter jwtFilter;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * Charge les clés RSA privées et publiques au démarrage.
	 *
	 * @throws Exception en cas d'erreur de chargement des clés
	 */
	@PostConstruct
	public void loadKeys() throws Exception {
		rsaPublicKey = KeyUtils.loadPublicKey();
		rsaPrivateKey = KeyUtils.loadPrivateKey();
	}

	/**
	 * Configure le provider d'authentification avec UserDetailsService et encodeur
	 * de mot de passe.
	 *
	 * @return DaoAuthenticationProvider configuré
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Expose le gestionnaire d'authentification pour l'application.
	 *
	 * @param config configuration d'authentification Spring
	 * @return AuthenticationManager utilisé par Spring Security
	 * @throws Exception en cas d'erreur
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Configure la chaîne de filtres de sécurité HTTP.
	 * <p>
	 * Désactive le CSRF, configure CORS, définit la gestion de session stateless,
	 * configure les règles d'autorisation des endpoints, et ajoute le filtre JWT.
	 * </p>
	 *
	 * @param http HttpSecurity configuré par Spring
	 * @return SecurityFilterChain configurée
	 * @throws Exception en cas d'erreur
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/auth/login",
								"/api/auth/register",
								"/api/auth/refresh-token")
						.permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	/**
	 * Configure la source des règles CORS pour l'application.
	 * <p>
	 * Autorise uniquement l'origine http://localhost:4200 avec méthodes et headers
	 * standards.
	 * </p>
	 *
	 * @return CorsConfigurationSource configurée
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	/**
	 * Bean pour décoder les JWT en utilisant la clé publique RSA.
	 *
	 * @return JwtDecoder configuré
	 */
	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaPublicKey).build();
	}

	/**
	 * Bean pour encoder (signer) les JWT avec la clé privée RSA.
	 *
	 * @return JwtEncoder configuré
	 */
	@Bean
	public JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaPublicKey)
				.privateKey(rsaPrivateKey)
				.build();

		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwkSource);
	}

	/**
	 * Bean pour encoder les mots de passe avec BCrypt.
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
