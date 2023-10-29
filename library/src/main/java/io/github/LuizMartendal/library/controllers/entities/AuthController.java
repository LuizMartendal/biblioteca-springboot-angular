package io.github.LuizMartendal.library.controllers.entities;

import io.github.LuizMartendal.library.dtos.CredentialsDto;
import io.github.LuizMartendal.library.dtos.TokenDto;
import io.github.LuizMartendal.library.services.entities.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "This service is responsible for your authentication")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This entity is your credentials, like username and password")
            @RequestBody CredentialsDto credentialsDto) {
        return ResponseEntity.ok().body(authService.login(credentialsDto));
    }
}
