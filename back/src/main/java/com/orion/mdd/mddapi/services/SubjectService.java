package com.orion.mdd.mddapi.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.dtos.SubjectWithSubscriptionDTO;
import com.orion.mdd.mddapi.mapper.SubjectMapper;
import com.orion.mdd.mddapi.models.Subject;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.SubjectRepository;
import com.orion.mdd.mddapi.repositories.UserRepository;

import jakarta.transaction.Transactional;

/**
 * Service gérant la logique métier liée aux thèmes.
 */
@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectMapper subjectMapper;

	/**
	 * Recherche un thème par son identifiant.
	 *
	 * @param id identifiant du thème
	 * @return le {@link Subject} trouvé, ou {@code null} si aucun thème ne
	 *         correspond
	 */
	public Subject findById(Long id) {
		return subjectRepository.findById(id).orElse(null);
	}

	/**
	 * Retourne la liste de tous les thèmes.
	 *
	 * @return liste des {@link Subject}
	 */
	public List<Subject> findAll() {
		return subjectRepository.findAll();
	}

	/**
	 * Retourne tous les thèmes avec un indicateur indiquant si l'utilisateur est
	 * souscrit ou non à chacun.
	 *
	 * @param user utilisateur connecté
	 * @return liste des {@link SubjectWithSubscriptionDTO}
	 */
	public List<SubjectWithSubscriptionDTO> getAllSubjectsWithSubscriptionStatus(User user) {
		Set<Long> subscribedIds = user.getSubscribedSubjects().stream()
				.map(Subject::getId)
				.collect(Collectors.toSet());

		return subjectRepository.findAll().stream()
				.map(subject -> subjectMapper.toSubjectWithSubscriptionDTO(subject, subscribedIds))
				.toList();
	}

	/**
	 * Souscrit l'utilisateur à un thème.
	 *
	 * @param subject thème à souscrire
	 * @param user    utilisateur connecté
	 */
	@Transactional
	public void subscribe(Subject subject, User user) {
		if (user.getSubscribedSubjects().add(subject)) {
			userRepository.save(user);
		}
	}

	/**
	 * Désabonne l'utilisateur d'un thème.
	 *
	 * @param subject thème à désabonner
	 * @param user    utilisateur connecté
	 */
	@Transactional
	public void unsubscribe(Subject subject, User user) {
		if (user.getSubscribedSubjects().remove(subject)) {
			userRepository.save(user);
		}
	}
}
