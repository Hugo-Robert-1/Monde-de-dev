package com.orion.mdd.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.SubjectDTO;
import com.orion.mdd.mddapi.mapper.SubjectMapper;
import com.orion.mdd.mddapi.models.Subject;
import com.orion.mdd.mddapi.services.SubjectService;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private SubjectMapper subjectMapper;

	/**
	 * @param id
	 * @return ResponseEntity<SubjectDTO>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
		Subject subject = subjectService.findById(id);
		SubjectDTO subjectDTO = subjectMapper.toDto(subject);
		return ResponseEntity.ok(subjectDTO);
	}

	/**
	 * 
	 * @return ResponseEntity List<SubjectDTO>
	 */
	@GetMapping
	public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
		List<Subject> subjects = subjectService.findAll();
		return ResponseEntity.ok().body(subjectMapper.toDtoList(subjects));
	}

}
