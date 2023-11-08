package io.github.LuizMartendal.library.controllers.person;

import io.github.LuizMartendal.library.configs.TestConfig;
import io.github.LuizMartendal.library.dtos.CredentialsDto;
import io.github.LuizMartendal.library.dtos.TokenDto;
import io.github.LuizMartendal.library.exceptions.StandError;
import io.github.LuizMartendal.library.integrations.AbstractIntegrationTest;
import io.github.LuizMartendal.library.mocks.MockPerson;
import io.github.LuizMartendal.library.models.entities.Person;
import io.github.LuizMartendal.library.utils.Page;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static MockPerson mockPerson;
    private static Person person;

    @BeforeAll
    public static void setUp() {
        mockPerson = new MockPerson();
        person = new Person();
    }

    @Test
    @Order(0)
    public void authentication() {
        Person personAdminMock = mockPerson.mockPersonAdmin(0);
        given()
                .basePath("/person")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .body(personAdminMock)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body().as(Person.class);

        CredentialsDto credentialsDto = new CredentialsDto(personAdminMock.getUsername(), personAdminMock.getPassword());
        TokenDto tokenDto = given()
                .basePath("/login")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .body(credentialsDto)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(TokenDto.class);

        assertNotNull(tokenDto);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getToken())
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() {
        Person personMock = mockPerson.mockPersonUser(1);

        person = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .body(personMock)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .extract().as(Person.class);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getCreatedBy());
        assertNotNull(person.getCreatedIn());
        assertNotNull(person.getUpdatedBy());
        assertNotNull(person.getUpdatedIn());

        assertEquals(personMock.getUsername(), person.getUsername());
        assertEquals(personMock.getFirstName(), person.getFirstName());
        assertEquals(personMock.getLastName(), person.getLastName());
        assertEquals(personMock.getRole(), person.getRole());
        assertEquals(personMock.getAddress(), person.getAddress());
        assertEquals(personMock.getGender(), person.getGender());

        assertTrue(new BCryptPasswordEncoder().matches(personMock.getPassword(), person.getPassword()));
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Person personMock = mockPerson.mockCreatedPersonAdmin(1);

        personMock.setId(person.getId());
        personMock.setCreatedIn(person.getCreatedIn());
        personMock.setCreatedBy(person.getCreatedBy());
        personMock.setUpdatedIn(person.getUpdatedIn());
        personMock.setUpdatedBy(person.getUpdatedBy());
        personMock.setPassword(person.getPassword());

        Person result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                  .pathParam("id", person.getId())
                  .body(personMock)
                .when()
                  .put("{id}")
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(Person.class);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedBy());
        assertNotNull(result.getCreatedIn());
        assertNotNull(result.getUpdatedBy());
        assertNotNull(result.getUpdatedIn());

        assertEquals(person.getUsername(), result.getUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getGender(), result.getGender());

        assertNotEquals(person.getRole(), result.getRole());
    }

    @Test
    @Order(3)
    public void testUnauthorizedUpdate() {
        Person personMock = mockPerson.mockCreatedPersonAdmin(1);

        personMock.setId(person.getId());
        personMock.setCreatedIn(person.getCreatedIn());
        personMock.setCreatedBy(person.getCreatedBy());
        personMock.setUpdatedIn(person.getUpdatedIn());
        personMock.setUpdatedBy(person.getUpdatedBy());
        personMock.setPassword(person.getPassword());

        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .port(TestConfig.SERVER_PORT)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                    .body(person)
                .when()
                    .put("{id}")
                .then()
                    .statusCode(401)
                    .extract()
                        .body().as(StandError.class);

        assertNotNull(error);

        assertEquals("UNAUTHORIZED", error.getStatus());
        assertEquals("You cannot access this service", error.getMessage());
        assertEquals(401, error.getCode());
    }

    @Test
    @Order(4)
    public void testFindById() {
        Person result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(Person.class);

        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void testUnauthorizedFindById() {
        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .port(TestConfig.SERVER_PORT)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(401)
                    .extract()
                        .body().as(StandError.class);

        assertNotNull(error);

        assertEquals("UNAUTHORIZED", error.getStatus());
        assertEquals("You cannot access this service", error.getMessage());
        assertEquals(401, error.getCode());
    }

    @Test
    @Order(6)
    public void testFindAll() {
        Page result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().as(Page.class);

        assertNotNull(result);
    }

    @Test
    @Order(7)
    public void testUnauthorizedFindAll() {
        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .port(TestConfig.SERVER_PORT)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(401)
                .extract()
                .body().as(StandError.class);

        assertNotNull(error);

        assertEquals("UNAUTHORIZED", error.getStatus());
        assertEquals("You cannot access this service", error.getMessage());
        assertEquals(401, error.getCode());
    }

    @Test
    @Order(8)
    public void testDelete() {
        given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                .when()
                    .delete("{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(9)
    public void testUnauthorizedDelete() {
        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/person")
                .port(TestConfig.SERVER_PORT)
                    .filter(new RequestLoggingFilter(LogDetail.ALL))
                    .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(401)
                    .extract()
                        .body().as(StandError.class);

        assertNotNull(error);

        assertEquals("UNAUTHORIZED", error.getStatus());
        assertEquals("You cannot access this service", error.getMessage());
        assertEquals(401, error.getCode());
    }

}
