package io.github.LuizMartendal.library.integrations;

import io.github.LuizMartendal.library.configs.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerTest extends AbstractIntegrationTest {

    @Test
    public void shouldDisplaySwaggerUIPage() {
        String swaggerReturn = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        assertTrue(swaggerReturn.contains("Swagger UI"));
    }
}
