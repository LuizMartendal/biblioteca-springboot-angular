package io.github.LuizMartendal.library.services;

import io.github.LuizMartendal.library.enuns.Roles;
import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.github.LuizMartendal.library.mocks.MockPerson;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.repositories.person.PersonRepository;
import io.github.LuizMartendal.library.services.entities.PersonService;
import io.github.LuizMartendal.library.utils.FilterImpl;
import io.github.LuizMartendal.library.utils.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testList() {
        Page<Person> page = new Page<>();
        page.setContent(input.mockManyCreatedPerson(10));
        page.setTotalPages(2L);
        page.setTotalElements(20L);

        when(repository.list(FilterImpl.parse("", 10, 0, "asc"))).thenReturn(page);

        Page<Person> pageResult = service.list(FilterImpl.parse("", 10, 0, "asc"));

        assertNotNull(pageResult);
        assertNotNull(pageResult.getContent());
        assertEquals(20L, pageResult.getTotalElements());
        assertEquals(2L, pageResult.getTotalPages());
    }

    @Test
    public void testGetById() {
        Person person = input.mockCreatedPersonUser(1);
        UUID uuid = UUID.randomUUID();
        person.setId(uuid);

        when(repository.getById(uuid)).thenReturn(person);
        Person personResult = service.getById(uuid);

        assertNotNull(personResult);
        assertNotNull(personResult.getId());
        assertEquals("Not authenticated", personResult.getCreatedBy());
        assertNotNull(personResult.getCreatedIn());
        assertEquals("Not authenticated", personResult.getUpdatedBy());
        assertNotNull(personResult.getUpdatedIn());
        assertEquals("User 1", personResult.getUsername());
        assertEquals("First name 1", personResult.getFirstName());
        assertEquals("Last name 1", personResult.getLastName());
        assertEquals("Street 1", personResult.getAddress());
        assertEquals("Male", personResult.getGender());
        assertEquals(Roles.USER, personResult.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("1234", personResult.getPassword()));
    }

    @Test
    public void testGetByIdWithNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.getById(uuid)).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> {
           service.getById(uuid);
        });

        assertNotNull(exception);
        assertEquals("Entity not found", exception.getMessage());
    }

    @Test
    public void testGetByIdWithNullId() {
        Exception exception = assertThrows(BadRequestException.class, () -> {
            service.getById(null);
        });

        assertNotNull(exception);
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    public void testCreate() {
        Person person = input.mockPersonUser(1);
        Person persisted = input.mockCreatedPersonUser(1);

        when(repository.save(person)).thenReturn(persisted);

        Person personResult = service.create(person);
        personResult.setPassword(person.getPassword());
        assertNotNull(personResult);
        assertNotNull(personResult.getId());
        assertEquals("Not authenticated", personResult.getCreatedBy());
        assertNotNull(personResult.getCreatedIn());
        assertEquals("Not authenticated", personResult.getUpdatedBy());
        assertNotNull(personResult.getUpdatedIn());
        assertEquals("User 1", personResult.getUsername());
        assertEquals("First name 1", personResult.getFirstName());
        assertEquals("Last name 1", personResult.getLastName());
        assertEquals("Street 1", personResult.getAddress());
        assertEquals("Male", personResult.getGender());
        assertEquals(Roles.USER, personResult.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("1234", personResult.getPassword()));
    }

    @Test
    public void testCreateWithExistentUsername() {
        Person person = input.mockPersonUser(1);

        when(repository.findByUsername(person.getUsername())).thenReturn(Optional.of(person));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            service.create(person);
        });

        assertNotNull(exception);
        assertEquals("There´s already a user with this username", exception.getMessage());
    }

    @Test
    public void testUpdate() {
        Person person = input.mockCreatedPersonUser(1);
        Person updatedPerson = input.mockCreatedPersonUser(2);
        updatedPerson.setId(person.getId());

        assertNotEquals(updatedPerson.getUsername(), person.getUsername());
        assertNotEquals(updatedPerson.getFirstName(), person.getFirstName());
        assertNotEquals(updatedPerson.getLastName(), person.getLastName());
        assertNotEquals(updatedPerson.getAddress(), person.getAddress());

        when(repository.getById(person.getId())).thenReturn(person);
        when(repository.save(person)).thenReturn(person);

        Person personResult = service.update(updatedPerson.getId(), updatedPerson);
        assertNotNull(personResult);
        assertNotNull(personResult.getId());
        assertEquals(updatedPerson.getCreatedBy(), personResult.getCreatedBy());
        assertNotNull(personResult.getCreatedIn());
        assertEquals(updatedPerson.getUpdatedBy(), personResult.getUpdatedBy());
        assertNotNull(personResult.getUpdatedIn());
        assertEquals(updatedPerson.getUsername(), personResult.getUsername());
        assertEquals(updatedPerson.getFirstName(), personResult.getFirstName());
        assertEquals(updatedPerson.getLastName(), personResult.getLastName());
        assertEquals(updatedPerson.getAddress(), personResult.getAddress());
        assertEquals(updatedPerson.getGender(), personResult.getGender());
        assertEquals(updatedPerson.getRole(), personResult.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("1234", personResult.getPassword()));
    }

    @Test
    public void testDelete() {
        Person person = input.mockCreatedPersonUser(1);

        when(repository.getById(person.getId())).thenReturn(person);

        service.delete(person.getId());
    }

    @Test
    public void testDeleteWithNotFound() {
        Person person = input.mockCreatedPersonUser(1);

        when(repository.getById(person.getId())).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            service.delete(person.getId());
        });

        assertNotNull(exception);
        assertEquals("Entity not found", exception.getMessage());
    }

    @Test
    public void testFindByUsername() {
        Person person = input.mockCreatedPersonUser(1);

        when(repository.findByUsername(person.getUsername())).thenReturn(Optional.of(person));

        Person personResult = service.findByUsername(person.getUsername());

        assertNotNull(personResult);
        assertNotNull(personResult.getId());
        assertEquals("Not authenticated", personResult.getCreatedBy());
        assertNotNull(personResult.getCreatedIn());
        assertEquals("Not authenticated", personResult.getUpdatedBy());
        assertNotNull(personResult.getUpdatedIn());
        assertEquals("User 1", personResult.getUsername());
        assertEquals("First name 1", personResult.getFirstName());
        assertEquals("Last name 1", personResult.getLastName());
        assertEquals("Street 1", personResult.getAddress());
        assertEquals("Male", personResult.getGender());
        assertEquals(Roles.USER, personResult.getRole());
        assertTrue(new BCryptPasswordEncoder().matches("1234", personResult.getPassword()));
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(repository.findByUsername("User")).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            service.findByUsername("User");
        });

        assertNotNull(exception);
        assertEquals("Username User not found", exception.getMessage());
    }
}
