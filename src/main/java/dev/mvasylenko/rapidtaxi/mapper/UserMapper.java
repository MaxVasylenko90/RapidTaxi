package dev.mvasylenko.rapidtaxi.mapper;

import dev.mvasylenko.rapidtaxi.dto.UsetDto;
import dev.mvasylenko.rapidtaxi.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UsetDto userToUserDto(User user);
    User userDtoToUser(UsetDto userDto);
}
