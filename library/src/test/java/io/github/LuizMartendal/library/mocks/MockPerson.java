package io.github.LuizMartendal.library.mocks;

import io.github.LuizMartendal.library.enuns.Roles;
import io.github.LuizMartendal.library.models.entities.Person;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MockPerson {

    public Person mockCreatedPersonUser(int number) {
        Person person = new Person();

        person.setId(UUID.randomUUID());
        person.setCreatedBy("Not authenticated");
        person.setCreatedIn(new Date());
        person.setUpdatedBy("Not authenticated");
        person.setUpdatedIn(new Date());

        person.setFirstName("First name " + number);
        person.setLastName("Last name " + number);
        person.setUsername("User " + number);
        person.setPassword(new BCryptPasswordEncoder().encode("1234"));
        person.setAddress("Street " + number);
        person.setGender("Male");
        person.setRole(Roles.USER);

        return person;
    }

    public Person mockPersonUser(int number) {
        Person person = new Person();

        person.setFirstName("First name " + number);
        person.setLastName("Last name " + number);
        person.setUsername("User " + number);
        person.setPassword("1234");
        person.setAddress("Street " + number);
        person.setGender("Male");
        person.setRole(Roles.USER);

        return person;
    }

    public Person mockCreatedPersonAdmin(int number) {
        Person person = new Person();

        person.setId(UUID.randomUUID());
        person.setCreatedBy("Not authenticated");
        person.setCreatedIn(new Date());
        person.setUpdatedBy("Not authenticated");
        person.setUpdatedIn(new Date());

        person.setFirstName("First name " + number);
        person.setLastName("Last name " + number);
        person.setPassword(new BCryptPasswordEncoder().encode("1234"));
        person.setAddress("Street " + 1);
        person.setGender("Male");
        person.setRole(Roles.ADMIN);

        return person;
    }

    public Person mockPersonAdmin(int number) {
        Person person = new Person();

        person.setFirstName("First name " + number);
        person.setLastName("Last name " + number);
        person.setPassword("1234");
        person.setAddress("Street " + 1);
        person.setGender("Male");
        person.setRole(Roles.ADMIN);

        return person;
    }

    public List<Person> mockManyPerson(int number) {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            personList.add(mockPersonUser(i));
        }
        return personList;
    }

    public List<Person> mockManyCreatedPerson(int number) {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            personList.add(mockCreatedPersonUser(i));
        }
        return personList;
    }
}
