package com.orion.mdd.mddapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.dtos.UserLightDTO;
import com.orion.mdd.mddapi.dtos.UserUpdatedDTO;
import com.orion.mdd.mddapi.dtos.UserWithSubjectsDTO;
import com.orion.mdd.mddapi.models.User;

/**
 * Mapper MapStruct permettant de convertir entre entités {@link User} et leurs
 * représentations DTO, y compris les variantes légères et enrichies.
 */
@Mapper(componentModel = "spring", uses = SubjectMapper.class)
public interface UserMapper {

	/**
	 * Convertit un {@link UserDTO} en entité {@link User}.
	 */
	User toEntity(UserDTO dto);

	/**
	 * Convertit un {@link User} en {@link UserDTO}.
	 */
	UserDTO toDto(User user);

	/**
	 * Met à jour une entité {@link User} avec les valeurs d'un {@link UserDTO}. Les
	 * propriétés nulles sont ignorées.
	 * 
	 * @param dto
	 * @param user
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateUserFromDto(UserDTO dto, @MappingTarget User user);

	/**
	 * Mapping pour récupérer la liste des thèmes auquels l'utilisateur est abonné
	 * 
	 * @param user
	 * @return {@link UserWithSubjectsDTO}
	 */
	@Mapping(source = "subscribedSubjects", target = "subscribedSubjects")
	UserWithSubjectsDTO toUserWithSubjectsDTO(User user);

	/**
	 * Méthode pour DTO allégé
	 * 
	 * @param user
	 * @return UserLightDTO
	 */
	default UserLightDTO toLightDto(User user) {
		if (user == null) {
			return null;
		}
		return new UserLightDTO(user.getId(), user.getUsername());
	}

	/**
	 * Convertit un {@link UserUpdatedDTO} en entité {@link User}.
	 */
	User toEntity(UserUpdatedDTO userUpdatedDto);
}
