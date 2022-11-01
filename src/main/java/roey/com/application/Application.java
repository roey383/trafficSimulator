package roey.com.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("roey.com.controller")
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class Application {

    @Autowired ConfigProperties configProperties;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Spring app started");
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> System.out.println(configProperties.getTimeUnitCycle().toMillis()/3600D);
    }

}
