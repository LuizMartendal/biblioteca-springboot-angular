package io.github.LuizMartendal.library.controllers.person;

import io.github.LuizMartendal.library.configs.TestConfig;
import io.github.LuizMartendal.library.integrations.AbstractIntegrationTest;
import io.github.LuizMartendal.library.mocks.MockPerson;
import io.github.LuizMartendal.library.models.entities.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static MockPerson mockPerson;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mockPerson = new MockPerson();
    }

    @Test
    @Order(0)
    public void testCreate() {
        Person person = mockPerson.mockPersonUser(1);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .setBasePath("/person")
                .setPort(TestConfig.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Person result = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract().as(Person.class);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedBy());
        assertNotNull(result.getCreatedIn());
        assertNotNull(result.getUpdatedBy());
        assertNotNull(result.getUpdatedIn());

        assertEquals(person.getUsername(), result.getUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getRole(), result.getRole());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getGender(), result.getGender());

        assertTrue(new BCryptPasswordEncoder().matches(person.getPassword(), result.getPassword()));
    }

}
