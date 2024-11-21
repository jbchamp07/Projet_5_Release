package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    public UserMapperTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUserDtoToEntity(){
        UserDto userDto = new UserDto();
        userDto.setId((long)1);
        userDto.setLastName("Last");
        userDto.setEmail("Email");
        userDto.setAdmin(true);
        userDto.setFirstName("First");
        userDto.setPassword("password");


        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(),user.getId());
        assertEquals(userDto.getLastName(),user.getLastName());
        assertEquals(userDto.getEmail(),user.getEmail());
        assertEquals(userDto.isAdmin(),user.isAdmin());
        assertEquals(userDto.getFirstName(),user.getFirstName());
        assertEquals(userDto.getPassword(),user.getPassword());
    }

    @Test
    public void testUserToDto(){
        User user = new User();
        user.setId((long)1);
        user.setLastName("Last");
        user.setEmail("Email");
        user.setAdmin(true);
        user.setFirstName("First");
        user.setPassword("password");


        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getId(),userDto.getId());
        assertEquals(user.getLastName(),userDto.getLastName());
        assertEquals(user.getEmail(),userDto.getEmail());
        assertEquals(user.isAdmin(),userDto.isAdmin());
        assertEquals(user.getFirstName(),userDto.getFirstName());
        assertEquals(user.getPassword(),userDto.getPassword());
    }

    @Test
    public void testListDtoToEntity(){
        UserDto userDto1 = new UserDto();
        userDto1.setId((long)1);
        userDto1.setLastName("Last");
        userDto1.setEmail("Email");
        userDto1.setAdmin(true);
        userDto1.setFirstName("First");
        userDto1.setPassword("password");

        UserDto userDto2 = new UserDto();
        userDto2.setId((long)2);
        userDto2.setLastName("Last2");
        userDto2.setEmail("Email2");
        userDto2.setAdmin(false);
        userDto2.setFirstName("First2");
        userDto2.setPassword("password2");

        List<UserDto> listDto = new ArrayList<UserDto>();
        listDto.add(userDto1);
        listDto.add(userDto2);


        List<User> list = userMapper.toEntity(listDto);
        User user1 = list.stream().filter(u -> u.getId() == userDto1.getId()).findFirst().get();
        User user2 = list.stream().filter(u -> u.getId() == userDto2.getId()).findFirst().get();

        assertEquals(listDto.size(),list.size());
        assertEquals(userDto1.getFirstName(),user1.getFirstName());
        assertEquals(userDto2.getFirstName(),user2.getFirstName());
        assertEquals(userDto1.getEmail(),user1.getEmail());
        assertEquals(userDto2.getEmail(),user2.getEmail());
        assertEquals(userDto1.getId(),user1.getId());
        assertEquals(userDto2.getId(),user2.getId());
        assertEquals(userDto1.getLastName(),user1.getLastName());
        assertEquals(userDto2.getLastName(),user2.getLastName());
        assertEquals(userDto1.getPassword(),user1.getPassword());
        assertEquals(userDto2.getPassword(),user2.getPassword());
    }

    @Test
    public void testListToDto(){

        User user1 = new User();
        user1.setId((long)1);
        user1.setLastName("Last");
        user1.setEmail("Email");
        user1.setAdmin(true);
        user1.setFirstName("First");
        user1.setPassword("password");

        User user2 = new User();
        user2.setId((long)2);
        user2.setLastName("Last2");
        user2.setEmail("Email2");
        user2.setAdmin(false);
        user2.setFirstName("First2");
        user2.setPassword("password2");

        List<User> list = new ArrayList<User>();
        list.add(user1);
        list.add(user2);


        List<UserDto> listDto = userMapper.toDto(list);
        UserDto userDto1 = listDto.stream().filter(u -> u.getId() == (long) 1).findFirst().get();
        UserDto userDto2 = listDto.stream().filter(u -> u.getId() == (long) 2).findFirst().get();

        assertEquals(list.size(),listDto.size());
        assertEquals(user1.getFirstName(),userDto1.getFirstName());
        assertEquals(user2.getFirstName(),userDto2.getFirstName());
        assertEquals(user1.getEmail(),userDto1.getEmail());
        assertEquals(user2.getEmail(),userDto2.getEmail());
        assertEquals(user1.getId(),userDto1.getId());
        assertEquals(user2.getId(),userDto2.getId());
        assertEquals(user1.getLastName(),userDto1.getLastName());
        assertEquals(user2.getLastName(),userDto2.getLastName());
        assertEquals(user1.getPassword(),userDto1.getPassword());
        assertEquals(user2.getPassword(),userDto2.getPassword());

    }

}
