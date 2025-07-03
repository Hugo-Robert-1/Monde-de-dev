package com.orion.mdd.mddapi.services;

import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.models.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class DtoMapperService {
    private final ModelMapper modelMapper;

    public DtoMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                map().setCreated_at(source.getCreatedAt());
                map().setUpdated_at(source.getUpdatedAt());
            }
        });
    }

    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}