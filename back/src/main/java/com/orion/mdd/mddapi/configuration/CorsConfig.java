package com.orion.mdd.mddapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration globale CORS pour l'application.
 * <p>
 * Permet la configuration des règles CORS pour toutes les routes, en autorisant
 * notamment le frontend Angular à l'origine http://localhost:4200.
 * </p>
 */
@Configuration
public class CorsConfig {

	/**
	 * Configure les mappings CORS.
	 *
	 * @return WebMvcConfigurer configuré avec les règles CORS
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // toutes les routes
						.allowedOrigins("http://localhost:4200") // autorise ce frontend
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}