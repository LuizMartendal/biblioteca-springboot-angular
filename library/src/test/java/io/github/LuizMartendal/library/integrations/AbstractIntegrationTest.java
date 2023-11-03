package io.github.LuizMartendal.library.integrations;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(inheritInitializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgresql:15-alpine");

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

        }
    }
}
