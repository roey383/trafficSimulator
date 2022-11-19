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
import roey.com.domain.*;
import roey.com.domain.road.MultiLaneArray;
import roey.com.domain.road.Road;
import roey.com.view.PauseApp;
import roey.com.view.View;

import java.io.IOException;
import java.util.List;
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
    PauseApp getPauseApp(){
        return new PauseApp();
    }

    @Bean
    @Qualifier("ListLane")
    public Road getListLaneRoad() {
        return new ListLane(configProperties.getLaneLength(),
                configProperties.getLaneSpeedLimit(),
                configProperties.getLaneFriction());
    }

    @Bean
    @Qualifier("MultiSpeedLane")
    public Road getMultiSpeedLane() {
        return new MultiSpeedLane(configProperties.getLaneFriction(),
                configProperties.getLengths(),
                configProperties.getSpeeds()
        );
    }

    @Bean
    @Qualifier("ArrayLane")
    public Road getArrayLane() {
        return getNewArrayLane();
    }

    @Bean
    @Qualifier("MultiLaneArray")
    public Road getMultiLaneArray() {
        return new MultiLaneArray(List.of(getNewArrayLane(), getNewArrayLane(), getNewArrayLane()));
    }

    @Bean
    @Qualifier("drivers")
    public Queue<Driver> getActiveDrivers() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public View getView(ConfigProperties configProperties, Queue<Driver> drivers,
                        @Qualifier("MultiLaneArray") Road road, PauseApp pauseApp) throws IOException {
        return new View(configProperties, drivers, road, pauseApp);
    }

    private ArrayLane getNewArrayLane() {
        return new ArrayLane(configProperties.getLaneLength().intValue(),
                configProperties.getLaneSpeedLimit(),
                configProperties.getLaneFriction()
        );
    }

}
