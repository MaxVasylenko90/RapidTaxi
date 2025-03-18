package dev.mvasylenko.rapidtaxi.mapper;

import dev.mvasylenko.rapidtaxi.dto.UserDto;
import dev.mvasylenko.rapidtaxi.dto.UserRegistrationDto;
import dev.mvasylenko.rapidtaxi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);
    User userRegistrationDtoToUser(UserRegistrationDto userDto);
}
