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

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectMapper subjectMapper;

	public Subject findById(Long id) {
		return subjectRepository.findById(id).orElse(null);
	}

	public List<Subject> findAll() {
		return subjectRepository.findAll();
	}

	public List<SubjectWithSubscriptionDTO> getAllSubjectsWithSubscriptionStatus(User user) {
		Set<Long> subscribedIds = user.getSubscribedSubjects().stream()
				.map(Subject::getId)
				.collect(Collectors.toSet());

		return subjectRepository.findAll().stream()
				.map(subject -> subjectMapper.toSubjectWithSubscriptionDTO(subject, subscribedIds))
				.toList();
	}

	@Transactional
	public void subscribe(Subject subject, User user) {
		if (user.getSubscribedSubjects().add(subject)) {
			userRepository.save(user);
		}
	}

	@Transactional
	public void unsubscribe(Subject subject, User user) {
		if (user.getSubscribedSubjects().remove(subject)) {
			userRepository.save(user);
		}
	}
}
