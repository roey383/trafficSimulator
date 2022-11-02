package roey.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;
import roey.com.domain.RegularCar;
import roey.com.domain.RegularDriver;

import java.util.Queue;

@RestController
public class Controller {

    @Autowired
    Queue<Driver> drivers;
    @Autowired
    Lane lane;
    @Autowired
    ConfigProperties configProperties;

    @GetMapping("/status")
    public String getStatus() {
        return "Controller init";
    }

    @GetMapping("/addDriver")
    public String addDriver() {

        var car = new RegularCar(lane, 4D, configProperties);
        var driver = new RegularDriver(lane, car, configProperties.getResponseTime());
        drivers.add(driver);
        return "Driver added";
    }

}
