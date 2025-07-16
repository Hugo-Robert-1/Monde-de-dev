package com.orion.mdd.mddapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.Subject;
import com.orion.mdd.mddapi.repositories.SubjectRepository;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	public Subject findById(Long id) {
		return subjectRepository.findById(id).orElse(null);
	}

	public List<Subject> findAll() {
		return subjectRepository.findAll();
	}

}
