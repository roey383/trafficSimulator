package roey.com.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import roey.com.domain.Driver;
import roey.com.domain.Lane;
import roey.com.domain.RegularLane;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@ComponentScan({"roey.com.controller", "roey.com.scheduler"})
@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class Application {

    @Autowired
    ConfigProperties configProperties;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Spring app started");
    }

    @Bean
    public Lane getLane() {
        return new RegularLane(configProperties.getLaneLength(),
                configProperties.getLaneSpeedLimit(),
                configProperties.getLaneFriction());
    }

    @Bean
    @Qualifier("drivers")
    public Queue<Driver> getActiveDrivers() {
        return new ConcurrentLinkedQueue<>();
    }

}
