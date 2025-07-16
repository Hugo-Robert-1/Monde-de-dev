package com.orion.mdd.mddapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.orion.mdd.mddapi.dtos.SubjectDTO;
import com.orion.mdd.mddapi.models.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
	Subject toEntity(SubjectDTO dto);

	SubjectDTO toDto(Subject subject);

	List<Subject> toEntityList(List<SubjectDTO> users);

	List<SubjectDTO> toDtoList(List<Subject> users);
}
