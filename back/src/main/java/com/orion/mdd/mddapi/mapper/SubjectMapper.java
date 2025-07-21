package com.orion.mdd.mddapi.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.orion.mdd.mddapi.dtos.SubjectDTO;
import com.orion.mdd.mddapi.dtos.SubjectWithSubscriptionDTO;
import com.orion.mdd.mddapi.models.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
	Subject toEntity(SubjectDTO dto);

	SubjectDTO toDto(Subject subject);

	List<Subject> toEntityList(List<SubjectDTO> users);

	List<SubjectDTO> toDtoList(List<Subject> users);

	@Mapping(target = "isSubscribed", expression = "java(subscribedIds.contains(subject.getId()))")
	SubjectWithSubscriptionDTO toSubjectWithSubscriptionDTO(Subject subject, Set<Long> subscribedIds);
}
