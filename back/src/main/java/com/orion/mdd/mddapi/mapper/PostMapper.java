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

@Mapper(componentModel = "spring", uses = { UserMapper.class, SubjectMapper.class })
public interface PostMapper {

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);
	SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

	@Mapping(source = "author", target = "author", qualifiedByName = "toLightUserDto")
	@Mapping(source = "subject", target = "subject", qualifiedByName = "toLightSubjectDto")
	PostDTO toDto(Post post);

	Post toEntity(PostDTO postDTO);

	List<PostDTO> toDtoList(List<Post> posts);

	List<Post> toEntityList(List<PostDTO> posts);

	@Named("toLightUserDto")
	default UserLightDTO toLightUserDto(com.orion.mdd.mddapi.models.User user) {
		return userMapper.toLightDto(user);
	}

	@Named("toLightSubjectDto")
	default SubjectLightDTO toLightSubjectDto(com.orion.mdd.mddapi.models.Subject subject) {
		return subjectMapper.toLightDto(subject);
	}

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
