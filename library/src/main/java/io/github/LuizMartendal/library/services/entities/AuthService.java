package io.github.LuizMartendal.library.services.entities;

import io.github.LuizMartendal.library.dtos.CredentialsDto;
import io.github.LuizMartendal.library.dtos.TokenDto;
import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PersonService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public TokenDto login(CredentialsDto credentialsDto) {
        try {
            String username = credentialsDto.getUsername();
            String password = credentialsDto.getPassword();

            Person person = service.findByUsername(username);
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return tokenProvider.getToken(username, person.getRole());
        } catch (Exception e) {
            throw new BadRequestException("Username or password incorrect");
        }
    }
}
