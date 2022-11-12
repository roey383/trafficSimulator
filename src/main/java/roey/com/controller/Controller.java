package roey.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.RegularCar;
import roey.com.domain.RegularDriver;
import roey.com.domain.road.Road;

import java.util.Queue;

@RestController
public class Controller {

    @Autowired
    Queue<Driver> drivers;
    @Autowired
    @Qualifier("MultiLaneArray")
    Road road;
    @Autowired
    ConfigProperties configProperties;

    @GetMapping("/status")
    public String getStatus() {
        return "Controller init";
    }

    @GetMapping("/addDriver")
    public String addDriver() {

        var car = new RegularCar(road, 4D, configProperties);
        var driver = new RegularDriver(road, car, configProperties.getResponseTime());
        drivers.add(driver);
        return "Driver added";
    }

}
