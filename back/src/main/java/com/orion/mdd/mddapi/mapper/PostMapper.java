package com.orion.mdd.mddapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.orion.mdd.mddapi.dtos.PostDTO;
import com.orion.mdd.mddapi.dtos.PostWithCommentsDTO;
import com.orion.mdd.mddapi.dtos.SubjectLightDTO;
import com.orion.mdd.mddapi.dtos.UserLightDTO;
import com.orion.mdd.mddapi.models.Post;

/**
 * Mapper MapStruct permettant de convertir entre entités {@link Post} et leurs
 * représentations DTO, avec ou sans commentaires.
 * <p>
 * Utilise {@link UserMapper} et {@link SubjectMapper} pour mapper les champs
 * liés.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubjectMapper.class })
public interface PostMapper {

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);
	SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

	/**
	 * Convertit une entité {@link Post} en {@link PostDTO}.
	 *
	 * @param post entité post
	 * @return DTO post correspondant
	 */
	@Mapping(source = "author", target = "author", qualifiedByName = "toLightUserDto")
	@Mapping(source = "subject", target = "subject", qualifiedByName = "toLightSubjectDto")
	PostDTO toDto(Post post);

	/**
	 * Convertit un {@link PostDTO} en entité {@link Post}.
	 *
	 * @param postDTO DTO post
	 * @return entité {@link Post} correspondante
	 */
	Post toEntity(PostDTO postDTO);

	/**
	 * Convertit une liste d'entités {@link Post} en liste de {@link PostDTO}.
	 */
	List<PostDTO> toDtoList(List<Post> posts);

	/**
	 * Convertit une liste de {@link PostDTO} en liste d'entités {@link Post}.
	 */
	List<Post> toEntityList(List<PostDTO> posts);

	/**
	 * Conversion d'un utilisateur vers un {@link UserLightDTO}.
	 */
	@Named("toLightUserDto")
	default UserLightDTO toLightUserDto(com.orion.mdd.mddapi.models.User user) {
		return userMapper.toLightDto(user);
	}

	/**
	 * Conversion d'un sujet vers un {@link SubjectLightDTO}.
	 */
	@Named("toLightSubjectDto")
	default SubjectLightDTO toLightSubjectDto(com.orion.mdd.mddapi.models.Subject subject) {
		return subjectMapper.toLightDto(subject);
	}

	/**
	 * Convertit un {@link Post} en {@link PostWithCommentsDTO} incluant ses
	 * commentaires.
	 *
	 * @param post          entité post
	 * @param commentMapper mapper pour convertir les commentaires
	 * @return DTO avec commentaires
	 */
	default PostWithCommentsDTO toDtoWithComments(Post post, CommentMapper commentMapper) {
		return new PostWithCommentsDTO(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				toLightSubjectDto(post.getSubject()),
				post.getCreatedAt(),
				toLightUserDto(post.getAuthor()),
				post.getComments().stream()
						.map(commentMapper::toDto)
						.collect(Collectors.toList()));
	}
}
