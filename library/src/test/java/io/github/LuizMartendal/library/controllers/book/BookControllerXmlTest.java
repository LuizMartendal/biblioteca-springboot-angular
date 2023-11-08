package io.github.LuizMartendal.library.controllers.book;

import io.github.LuizMartendal.library.configs.TestConfig;
import io.github.LuizMartendal.library.dtos.CredentialsDto;
import io.github.LuizMartendal.library.dtos.TokenDto;
import io.github.LuizMartendal.library.exceptions.StandError;
import io.github.LuizMartendal.library.integrations.AbstractIntegrationTest;
import io.github.LuizMartendal.library.mocks.MockBook;
import io.github.LuizMartendal.library.mocks.MockPerson;
import io.github.LuizMartendal.library.models.entities.Book;
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
public class BookControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static MockBook mockBook;
    private static MockPerson mockPerson;
    private static Book book;

    @BeforeAll
    public static void setUp() {
        mockPerson = new MockPerson();
        mockBook = new MockBook();
        book = new Book();
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
        Book bookMock = mockBook.mockBook(1);

        book = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .body(bookMock)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .extract().as(Book.class);

        assertNotNull(book);
        assertNotNull(book.getId());
        assertNotNull(book.getCreatedBy());
        assertNotNull(book.getCreatedIn());
        assertNotNull(book.getUpdatedBy());
        assertNotNull(book.getUpdatedIn());

        assertEquals(bookMock.getTitle(), book.getTitle());
        assertEquals(bookMock.getAuthor(), book.getAuthor());
        assertEquals(bookMock.getPrice(), book.getPrice(), 0.0);
        assertNotNull(book.getLaunchDate());
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Book bookMock = mockBook.mockCreatedBook(1);

        bookMock.setId(book.getId());
        bookMock.setCreatedIn(book.getCreatedIn());
        bookMock.setCreatedBy(book.getCreatedBy());
        bookMock.setUpdatedIn(book.getUpdatedIn());
        bookMock.setUpdatedBy(book.getUpdatedBy());

        Book result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                  .pathParam("id", book.getId())
                  .body(bookMock)
                .when()
                  .put("{id}")
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(Book.class);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedBy());
        assertNotNull(result.getCreatedIn());
        assertNotNull(result.getUpdatedBy());
        assertNotNull(result.getUpdatedIn());

        assertEquals(bookMock.getTitle(), result.getTitle());
        assertEquals(bookMock.getAuthor(), result.getAuthor());
        assertEquals(bookMock.getPrice(), result.getPrice(), 0.0);
        assertNotNull(book.getLaunchDate());
    }

    @Test
    @Order(3)
    public void testUnauthorizedUpdate() {
        Book bookMock = mockBook.mockCreatedBook(1);

        bookMock.setId(book.getId());
        bookMock.setCreatedIn(book.getCreatedIn());
        bookMock.setCreatedBy(book.getCreatedBy());
        bookMock.setUpdatedIn(book.getUpdatedIn());
        bookMock.setUpdatedBy(book.getUpdatedBy());

        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .port(TestConfig.SERVER_PORT)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", book.getId())
                    .body(bookMock)
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
        Book result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", book.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(Book.class);

        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void testFindAll() {
        Page result = given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
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
    @Order(6)
    public void testDelete() {
        given()
                .spec(specification)
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", book.getId())
                .when()
                    .delete("{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(7)
    public void testUnauthorizedDelete() {
        StandError error = given()
                .header(TestConfig.HEADER_PARAM_ORIGIN, "http://localhost:4200")
                .basePath("/book")
                .port(TestConfig.SERVER_PORT)
                    .filter(new RequestLoggingFilter(LogDetail.ALL))
                    .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .contentType(TestConfig.CONTENT_TYPE_XML)
                    .pathParam("id", book.getId())
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
