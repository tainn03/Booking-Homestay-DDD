package nnt.com;

import nnt.com.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(StartApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer());
        springApplication.run(args);
    }
}