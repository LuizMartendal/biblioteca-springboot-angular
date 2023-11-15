package io.github.LuizMartendal.library.configurations;

import io.github.LuizMartendal.library.exceptions.CustomBearerTokenAccessDeniedHandler;
import io.github.LuizMartendal.library.exceptions.CustomBearerTokenAuthenticationEntryPoint;
import io.github.LuizMartendal.library.security.JwtTokenFilter;
import io.github.LuizMartendal.library.security.JwtTokenProvider;
import io.github.LuizMartendal.library.services.entities.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PersonService personService;

    @Autowired
    private CustomBearerTokenAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomBearerTokenAuthenticationEntryPoint authenticationEntryPointException;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private static final String[] PUBLIC_MATCHER = {"/swagger-ui/**", "/swagger-ui.html", "/v3/**",
            "/login", "swagger-ui/index.html", "/*.js", "/*.js.map", "/*.html", "/", "/*.css", "/*.json",
            "/*.woff2", "/*.woff", "/*.png", "/assets/**", "/svg/**", "/actuator/**"};

    private static final String[] PUBLIC_MATCHER_POST = {"/person"};

    private static final String[] PUBLIC_MATCHER_GET = {"/book", "/book/**"};

    private static final String[] PRIVATE_MATCHER_POST = {"/book"};

    private static final String[] PRIVATE_MATCHER_PUT = {"/person/**", "/book/**"};

    private static final String[] PRIVATE_MATCHER_GET = {"/person/**", "/person/logged"};

    private static final String[] PRIVATE_MATCHER_GET_ADMIN = {"/person"};

    private static final String[] PRIVATE_MATCHER_DELETE = {"/book/**", "/person/**"};

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPointException)
                    .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers(PUBLIC_MATCHER).permitAll()
                                .antMatchers(HttpMethod.GET, PUBLIC_MATCHER_GET).permitAll()
                                .antMatchers(HttpMethod.POST, PUBLIC_MATCHER_POST).permitAll()
                                .antMatchers(HttpMethod.POST, PRIVATE_MATCHER_POST).authenticated()
                                .antMatchers(HttpMethod.PUT, PRIVATE_MATCHER_PUT).authenticated()
                                .antMatchers(HttpMethod.GET, PRIVATE_MATCHER_GET_ADMIN).hasRole("ADMIN")
                                .antMatchers(HttpMethod.GET, PRIVATE_MATCHER_GET).authenticated()
                                .antMatchers(HttpMethod.DELETE, PRIVATE_MATCHER_DELETE).authenticated()
                )
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(new JwtTokenFilter(resolver, tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personService).passwordEncoder(passwordEncoder());
    }

    @Bean
    AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationManager();
    }

}
