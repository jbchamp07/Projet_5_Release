package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherMapperTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;


    public TeacherMapperTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTeacherDtoToEntity(){
        LocalDateTime date = LocalDateTime.now();
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId((long)1);
        teacherDto.setFirstName("First");
        teacherDto.setLastName("Last");
        teacherDto.setCreatedAt(date);
        teacherDto.setUpdatedAt(date);

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherDto.getFirstName(),teacher.getFirstName());
        assertEquals(teacherDto.getLastName(),teacher.getLastName());
        assertEquals(teacherDto.getId(),teacher.getId());
        assertEquals(teacherDto.getCreatedAt(),teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(),teacher.getUpdatedAt());
    }

    @Test
    public void testTeacherToDto(){
        LocalDateTime date = LocalDateTime.now();
        Teacher teacher = new Teacher();
        teacher.setId((long)1);
        teacher.setFirstName("First");
        teacher.setLastName("Last");
        teacher.setCreatedAt(date);
        teacher.setUpdatedAt(date);

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacher.getFirstName(),teacherDto.getFirstName());
        assertEquals(teacher.getLastName(),teacherDto.getLastName());
        assertEquals(teacher.getId(),teacherDto.getId());
        assertEquals(teacher.getCreatedAt(),teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(),teacherDto.getUpdatedAt());
    }

    @Test
    public void testListToDto(){
        LocalDateTime date = LocalDateTime.now();

        Teacher teacher1 = new Teacher();
        teacher1.setId((long)1);
        teacher1.setFirstName("First");
        teacher1.setLastName("Last");
        teacher1.setCreatedAt(date);
        teacher1.setUpdatedAt(date);

        Teacher teacher2 = new Teacher();
        teacher2.setId((long)2);
        teacher2.setFirstName("First2");
        teacher2.setLastName("Last2");
        teacher2.setCreatedAt(date);
        teacher2.setUpdatedAt(date);

        List<Teacher> list = new ArrayList<Teacher>();
        list.add(teacher1);
        list.add(teacher2);

        List<TeacherDto> listDto = teacherMapper.toDto(list);
        TeacherDto teacherDto1 = listDto.stream().filter(t -> t.getId() == teacher1.getId()).findFirst().get();
        TeacherDto teacherDto2 = listDto.stream().filter(t -> t.getId() == teacher2.getId()).findFirst().get();

        assertEquals(list.size(),listDto.size());
        assertEquals(teacher1.getId(),teacherDto1.getId());
        assertEquals(teacher2.getId(),teacherDto2.getId());
        assertEquals(teacher1.getFirstName(),teacherDto1.getFirstName());
        assertEquals(teacher2.getFirstName(),teacherDto2.getFirstName());
        assertEquals(teacher1.getLastName(),teacherDto1.getLastName());
        assertEquals(teacher2.getLastName(),teacherDto2.getLastName());
        assertEquals(teacher1.getCreatedAt(),teacherDto1.getCreatedAt());
        assertEquals(teacher2.getCreatedAt(),teacherDto2.getCreatedAt());
        assertEquals(teacher1.getUpdatedAt(),teacherDto1.getUpdatedAt());
        assertEquals(teacher2.getUpdatedAt(),teacherDto2.getUpdatedAt());
    }

    @Test
    public void testListDtoToEntity(){
        LocalDateTime date = LocalDateTime.now();

        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId((long)1);
        teacherDto1.setFirstName("First");
        teacherDto1.setLastName("Last");
        teacherDto1.setCreatedAt(date);
        teacherDto1.setUpdatedAt(date);

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId((long)2);
        teacherDto2.setFirstName("First2");
        teacherDto2.setLastName("Last2");
        teacherDto2.setCreatedAt(date);
        teacherDto2.setUpdatedAt(date);

        List<TeacherDto> listDto = new ArrayList<TeacherDto>();
        listDto.add(teacherDto1);
        listDto.add(teacherDto2);

        List<Teacher> list = teacherMapper.toEntity(listDto);
        Teacher teacher1 = list.stream().filter(t -> t.getId() == teacherDto1.getId()).findFirst().get();
        Teacher teacher2 = list.stream().filter(t -> t.getId() == teacherDto2.getId()).findFirst().get();

        assertEquals(listDto.size(),list.size());
        assertEquals(teacherDto1.getId(),teacher1.getId());
        assertEquals(teacherDto2.getId(),teacher2.getId());
        assertEquals(teacherDto1.getFirstName(),teacher1.getFirstName());
        assertEquals(teacherDto2.getFirstName(),teacher2.getFirstName());
        assertEquals(teacherDto1.getLastName(),teacher1.getLastName());
        assertEquals(teacherDto2.getLastName(),teacher2.getLastName());
        assertEquals(teacherDto1.getCreatedAt(),teacher1.getCreatedAt());
        assertEquals(teacherDto2.getCreatedAt(),teacher2.getCreatedAt());
        assertEquals(teacherDto1.getUpdatedAt(),teacher1.getUpdatedAt());
        assertEquals(teacherDto2.getUpdatedAt(),teacher2.getUpdatedAt());
    }

}
