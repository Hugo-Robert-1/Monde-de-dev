package com.orion.mdd.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.orion.mdd.mddapi.dtos.CommentCreateDTO;
import com.orion.mdd.mddapi.dtos.CommentDTO;
import com.orion.mdd.mddapi.dtos.UserLightDTO;
import com.orion.mdd.mddapi.models.Comment;
import com.orion.mdd.mddapi.models.Post;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CommentMapper {

	@Mapping(source = "user", target = "user", qualifiedByName = "toLightUserDto")
	CommentDTO toDto(Comment comment);

	@Mapping(source = "dto.content", target = "content")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(source = "post", target = "post")
	Comment toEntity(CommentCreateDTO dto, Post post);

	@Named("toLightUserDto")
	default UserLightDTO toLightUserDto(com.orion.mdd.mddapi.models.User user) {
		return new UserLightDTO(user.getId(), user.getUsername());
	}
}