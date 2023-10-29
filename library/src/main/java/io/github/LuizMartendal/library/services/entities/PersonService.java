package io.github.LuizMartendal.library.services.entities;

import io.github.LuizMartendal.library.enuns.Roles;
import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.NotFoundException;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.repositories.PersonRepository;
import io.github.LuizMartendal.library.services.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonService extends ServiceImpl<Person> implements UserDetailsService {

    @Autowired
    private PersonRepository repository;

    @Override
    public JpaRepository<Person, UUID> getRepository() {
        return repository;
    }

    @Override
    public Person create(Person entity) {
        if (findByUsername(entity.getUsername()) != null) {
            throw new BadRequestException("There´s already a user with this username");
        }
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        entity.setRole(Roles.USER);
        return super.create(entity);
    }

    public Person findByUsername(String username) {
        Optional<Person> personOptional = repository.findByUsername(username);
        if (personOptional.isEmpty()) {
            throw new NotFoundException("Username " + username + " not found");
        }
        return personOptional.get();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person person = findByUsername(s);
        return User
                .builder()
                    .username(person.getUsername())
                    .password(person.getPassword())
                    .roles(person.getRole().toString())
                .build();
    }

    public static String getLoggedUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            return user.getUsername();
        }
        return "Not Authenticated";
    }
}
