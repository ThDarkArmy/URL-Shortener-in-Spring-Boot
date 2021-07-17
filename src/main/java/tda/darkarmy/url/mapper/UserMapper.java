package tda.darkarmy.url.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tda.darkarmy.url.dto.UserDto;
import tda.darkarmy.url.model.Role;
import tda.darkarmy.url.model.Roles;
import tda.darkarmy.url.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "updatedAt", source = "")
    @Mapping(target = "roles", source = "userDto", qualifiedByName = "rolesDto")
    @Mapping(target = "id", source = "")
    @Mapping(target = "createdAt", source = "")
    @Mapping(target = "active", source = "")
    User map(UserDto userDto);

    @Named("rolesDto")
    default Set<Roles> rolesToRolesDto(UserDto userDto){
        Set<Roles> roles = new HashSet<>();

        for(String role: userDto.getRoles()){
            Roles roles1 = new Roles();
            roles1.setName(Role.valueOf(role));
            roles.add(roles1);
        }

        return roles;
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "roles", source = "user", qualifiedByName = "userToDto")
    UserDto mapToDto(User user);

    @Named("userToDto")
    default List<String> setRoleToDto(User user){
        List<String> roles = new ArrayList<>();

        for(Roles role: user.getRoles()){
            roles.add(role.getName().name());
        }

        return roles;
    }


}
