package com.orion.mdd.mddapi.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.orion.mdd.mddapi.dtos.SubjectDTO;
import com.orion.mdd.mddapi.dtos.SubjectLightDTO;
import com.orion.mdd.mddapi.dtos.SubjectWithSubscriptionDTO;
import com.orion.mdd.mddapi.models.Subject;

/**
 * Mapper MapStruct permettant de convertir entre entités {@link Subject} et
 * leurs différentes représentations DTO.
 */
@Mapper(componentModel = "spring")
public interface SubjectMapper {

	/**
	 * Convertit un {@link SubjectDTO} en entité {@link Subject}.
	 */
	Subject toEntity(SubjectDTO dto);

	/**
	 * Convertit un {@link Subject} en {@link SubjectDTO}.
	 */
	SubjectDTO toDto(Subject subject);

	/**
	 * Convertit une liste de {@link SubjectDTO} en liste d'entités {@link Subject}.
	 */
	List<Subject> toEntityList(List<SubjectDTO> users);

	/**
	 * Convertit une liste de {@link Subject} en liste de {@link SubjectDTO}.
	 */
	List<SubjectDTO> toDtoList(List<Subject> users);

	/**
	 * Convertit un sujet en DTO {@link SubjectWithSubscriptionDTO} avec l'état
	 * d'abonnement.
	 *
	 * @param subject       sujet à convertir
	 * @param subscribedIds identifiants des sujets auxquels l'utilisateur est
	 *                      abonné
	 */
	@Mapping(target = "isSubscribed", expression = "java(subscribedIds.contains(subject.getId()))")
	SubjectWithSubscriptionDTO toSubjectWithSubscriptionDTO(Subject subject, Set<Long> subscribedIds);

	// méthode pour DTO allégé
	default SubjectLightDTO toLightDto(Subject subject) {
		if (subject == null) {
			return null;
		}
		return new SubjectLightDTO(subject.getId(), subject.getName());
	}
}
