package com.orion.mdd.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.SubjectDTO;
import com.orion.mdd.mddapi.dtos.SubjectWithSubscriptionDTO;
import com.orion.mdd.mddapi.mapper.SubjectMapper;
import com.orion.mdd.mddapi.models.Subject;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.SubjectService;

/**
 * Contrôleur REST gérant les opérations de gestion des thèmes
 * <p>
 * Ce contrôleur fournit un point d'entrée pour :
 * <ul>
 * <li>Récupérer de tous les thèmes</li>
 * <li>Récupérer d'un thème grâce à son ID</li>
 * <li>Souscrire à un thème</li>
 * <li>Désabonner à un thème</li>
 * <li>Récupérer tous les thèmes avec l'état de souscription de l'utilisateur
 * connecté</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private SubjectMapper subjectMapper;

	@Autowired
	private AuthService authService;

	/**
	 * Récupère un thème par son identifiant.
	 *
	 * @param id identifiant du thème
	 * @return {@link ResponseEntity} contenant le {@link SubjectDTO} correspondant
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
		Subject subject = subjectService.findById(id);
		SubjectDTO subjectDTO = subjectMapper.toDto(subject);
		return ResponseEntity.ok(subjectDTO);
	}

	/**
	 * Récupère la liste de tous les thèmes.
	 *
	 * @return {@link ResponseEntity} contenant la liste des {@link SubjectDTO}
	 */
	@GetMapping
	public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
		List<Subject> subjects = subjectService.findAll();
		return ResponseEntity.ok().body(subjectMapper.toDtoList(subjects));
	}

	/**
	 * Souscrit l'utilisateur connecté au thème spécifié.
	 *
	 * @param idSubject identifiant du thème
	 * @return code HTTP 200 si la souscription est réussie, 404 si le thème
	 *         n'existe pas, 400 en cas d'erreur
	 */
	@PostMapping("/{idSubject}/subscribe")
	public ResponseEntity<?> subscribe(@PathVariable Long idSubject) {
		try {
			Subject subject = subjectService.findById(idSubject);

			if (subject == null) {
				return ResponseEntity.notFound().build();
			}

			User user = authService
					.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());

			this.subjectService.subscribe(subject, user);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Désabonne l'utilisateur connecté du thème spécifié.
	 *
	 * @param idSubject identifiant du thème
	 * @return code HTTP 200 si le désabonnement est réussi, 404 si le thème
	 *         n'existe pas, 400 en cas d'erreur
	 */
	@DeleteMapping("/{idSubject}/unsubscribe")
	public ResponseEntity<?> unsubscribe(@PathVariable Long idSubject) {
		try {
			Subject subject = subjectService.findById(idSubject);

			if (subject == null) {
				return ResponseEntity.notFound().build();
			}

			User user = authService
					.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());

			this.subjectService.unsubscribe(subject, user);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Récupère la liste des thèmes avec un indicateur de souscription pour
	 * l'utilisateur connecté.
	 *
	 * @return {@link ResponseEntity} contenant la liste des
	 *         {@link SubjectWithSubscriptionDTO}
	 */
	@GetMapping("/with-subscription-status")
	public ResponseEntity<List<SubjectWithSubscriptionDTO>> getSubjectsWithSubscriptionStatus() {
		User user = authService.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());
		List<SubjectWithSubscriptionDTO> subjects = subjectService.getAllSubjectsWithSubscriptionStatus(user);
		return ResponseEntity.ok(subjects);
	}

}
