package roey.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;
import roey.com.domain.MultiSpeedLane;
import roey.com.domain.RegularLane;
import roey.com.view.View;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class Application {

    @Autowired
    ConfigProperties configProperties;

    public static void main(String[] args) {
        ApplicationContext ctx = new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);
        System.out.println("Spring app started");
    }

    @Bean
    @Qualifier("regularLane")
    public Lane getLane() {
        return new RegularLane(configProperties.getLaneLength(),
                configProperties.getLaneSpeedLimit(),
                configProperties.getLaneFriction());
    }

    @Bean
    @Qualifier("multiSpeedLane")
    public Lane getMultiSpeedLane() {
        return new MultiSpeedLane(configProperties.getLaneFriction(),
                configProperties.getLengths(),
                configProperties.getSpeeds()
        );
    }

    @Bean
    @Qualifier("drivers")
    public Queue<Driver> getActiveDrivers() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public View getView(ConfigProperties configProperties, Queue<Driver> drivers,
                        @Qualifier("multiSpeedLane") Lane lane) {
        return new View(configProperties, drivers, lane);
    }

}
