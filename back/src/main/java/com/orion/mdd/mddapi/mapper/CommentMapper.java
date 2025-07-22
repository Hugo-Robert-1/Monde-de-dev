package com.orion.mdd.mddapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.orion.mdd.mddapi.dtos.CommentDTO;
import com.orion.mdd.mddapi.dtos.UserLightDTO;
import com.orion.mdd.mddapi.models.Comment;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CommentMapper {

	@Mapping(source = "user", target = "user", qualifiedByName = "toLightUserDto")
	CommentDTO toDto(Comment comment);

	@Named("toLightUserDto")
	default UserLightDTO toLightUserDto(com.orion.mdd.mddapi.models.User user) {
		return new UserLightDTO(user.getId(), user.getUsername());
	}
}