package com.orion.mdd.mddapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.dtos.UserWithSubjectsDTO;
import com.orion.mdd.mddapi.models.User;

@Mapper(componentModel = "spring", uses = SubjectMapper.class)
public interface UserMapper {

	User toEntity(UserDTO dto);

	UserDTO toDto(User user);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateUserFromDto(UserDTO dto, @MappingTarget User user);

	// Mapping pour récupérer la liste des thèmes auquels l'utilisateur est abonné
	@Mapping(source = "subscribedSubjects", target = "subscribedSubjects")
	UserWithSubjectsDTO toUserWithSubjectsDTO(User user);
}
