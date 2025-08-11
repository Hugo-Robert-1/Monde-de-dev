package com.orion.mdd.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.orion.mdd.mddapi.dtos.CommentCreateDTO;
import com.orion.mdd.mddapi.dtos.CommentDTO;
import com.orion.mdd.mddapi.dtos.UserLightDTO;
import com.orion.mdd.mddapi.models.Comment;
import com.orion.mdd.mddapi.models.Post;

/**
 * Mapper MapStruct permettant de convertir entre entités {@link Comment} et
 * leurs différentes représentations DTO.
 * <p>
 * Utilise {@link UserMapper} pour mapper les informations utilisateur
 * associées.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CommentMapper {

	/**
	 * Convertit une entité {@link Comment} en {@link CommentDTO}.
	 *
	 * @param comment entité commentaire à convertir
	 * @return DTO correspondant
	 */
	@Mapping(source = "user", target = "user", qualifiedByName = "toLightUserDto")
	CommentDTO toDto(Comment comment);

	/**
	 * Convertit un {@link CommentCreateDTO} et un {@link Post} associé en entité
	 * {@link Comment}.
	 * <p>
	 * L'ID, la date de création et l'utilisateur sont ignorés car gérés ailleurs.
	 *
	 * @param dto  données de création du commentaire
	 * @param post post auquel le commentaire est rattaché
	 * @return entité {@link Comment} correspondante
	 */
	@Mapping(source = "dto.content", target = "content")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(source = "post", target = "post")
	Comment toEntity(CommentCreateDTO dto, Post post);

	/**
	 * Convertit une entité {@link com.orion.mdd.mddapi.models.User} en
	 * {@link UserLightDTO}. Méthode utilisée par MapStruct via l'annotation
	 * {@code @Named}.
	 *
	 * @param user entité utilisateur
	 * @return DTO utilisateur léger
	 */
	@Named("toLightUserDto")
	default UserLightDTO toLightUserDto(com.orion.mdd.mddapi.models.User user) {
		return new UserLightDTO(user.getId(), user.getUsername());
	}
}