package com.orion.mdd.mddapi.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orion.mdd.mddapi.services.CustomUserDetailsService;
import com.orion.mdd.mddapi.services.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre de sécurité qui intercepte chaque requête HTTP pour extraire et
 * valider le token JWT envoyé dans l'en-tête "Authorization".
 * <p>
 * Ce filtre vérifie la présence d'un token Bearer, valide ce token, et si
 * valide, authentifie l'utilisateur dans le contexte de sécurité Spring.
 * </p>
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

	private final JWTService jwtService;
	private final CustomUserDetailsService userDetailsService;

	public JWTFilter(JWTService jwtService, CustomUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Méthode appelée à chaque requête HTTP pour filtrer l'authentification via
	 * JWT.
	 * <ul>
	 * <li>Récupère le header "Authorization".</li>
	 * <li>Ignore la requête si le header est absent ou ne commence pas par "Bearer
	 * ".</li>
	 * <li>Extrait le token JWT, récupère l'identifiant utilisateur.</li>
	 * <li>Charge les détails utilisateur et valide le token.</li>
	 * <li>Si valide, met à jour le contexte de sécurité avec
	 * l'authentification.</li>
	 * </ul>
	 *
	 * @param request     requête HTTP
	 * @param response    réponse HTTP
	 * @param filterChain chaîne de filtres à exécuter après ce filtre
	 * @throws ServletException en cas d'erreur liée au servlet
	 * @throws IOException      en cas d'erreur d'entrée/sortie
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		String identifier = jwtService.extractIdentifier(token);

		if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);

			if (jwtService.isTokenValid(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);

			}
		}

		filterChain.doFilter(request, response);
	}
}
