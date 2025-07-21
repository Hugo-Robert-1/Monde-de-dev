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

	@PostMapping("/{idSubject}/subscribe")
	public ResponseEntity<?> subscribe(@PathVariable Long idSubject) {
		try {
			Subject subject = subjectService.findById(idSubject);

			if (subject == null) {
				return ResponseEntity.notFound().build();
			}

			User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

			this.subjectService.subscribe(subject, user);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{idSubject}/unsubscribe")
	public ResponseEntity<?> unsubscribe(@PathVariable Long idSubject) {
		try {
			Subject subject = subjectService.findById(idSubject);

			if (subject == null) {
				return ResponseEntity.notFound().build();
			}

			User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

			this.subjectService.unsubscribe(subject, user);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Retrieves the full list of themes and returns it with an isSubscribed
	 * attribute based on the choice of logged-in user
	 * 
	 * @return List<SubjectWithSubscriptionDTO> subjects
	 */
	@GetMapping("/with-subscription-status")
	public ResponseEntity<List<SubjectWithSubscriptionDTO>> getSubjectsWithSubscriptionStatus() {
		User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		List<SubjectWithSubscriptionDTO> subjects = subjectService.getAllSubjectsWithSubscriptionStatus(user);
		return ResponseEntity.ok(subjects);
	}

}
