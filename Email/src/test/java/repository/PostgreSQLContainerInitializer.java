package repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer sqlContainer = new PostgreSQLContainer("postgres:10.7");

    static {

        sqlContainer.start();
    }

    public void initialize (ConfigurableApplicationContext configurableApplicationContext){
        TestPropertyValues.of(
                "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                "spring.datasource.username=" + sqlContainer.getUsername(),
                "spring.datasource.password=" + sqlContainer.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }

}