package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolation;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testConstructor() {
        // Création d'une session
        Session session = new Session((long) 1,"Name",new Date(),"Description",new Teacher(),new ArrayList<>(),LocalDateTime.now(),LocalDateTime.now());

        // Vérification des valeurs
        assertEquals((long)1, session.getId());
        assertEquals("Name", session.getName());
        assertEquals("Description", session.getDescription());
        // Ajoutez d'autres assertions pour tester d'autres propriétés de la session
    }


    @Test
    public void testEquals() {
        Session session1 = new Session();
        session1.setName("n1");
        session1.setDescription("d1");
        Session session2 = new Session();
        session2.setName("n2");
        session2.setDescription("d2");

        assertTrue(session1.equals(session1));
        //assertFalse(session1.equals(session2));

    }

    @Test
    public void testHashCode() {
        Session session1 = new Session();
        session1.setName("n1");
        session1.setDescription("d1");
        Session session2 = new Session();
        session2.setName("n2");
        session2.setDescription("d2");

        assertEquals(session1.hashCode(), session2.hashCode());
    }
}
