package io.github.LuizMartendal.library.services;

import io.github.LuizMartendal.library.mocks.MockPerson;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.repositories.person.PersonRepository;
import io.github.LuizMartendal.library.services.entities.PersonService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() {
        Person person = input.mockPersonUser(1);
        UUID uuid = UUID.randomUUID();
        person.setId(uuid);

        when(repository.getById(uuid)).thenReturn(person);
        Person personResult = service.getById(uuid);

        assertNotNull(personResult);
        assertEquals(person.getUsername(), personResult.getUsername());
        assertEquals(person.getPassword(), personResult.getPassword());
        assertEquals(person.getFirstName(), personResult.getFirstName());
    }
}
