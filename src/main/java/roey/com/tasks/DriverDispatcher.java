package roey.com.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import roey.com.domain.Driver;
import roey.com.domain.Lane;

import java.util.Iterator;
import java.util.Queue;

@Configuration
@EnableScheduling
public class DriverDispatcher {

    @Autowired
    Queue<Driver> drivers;
    @Autowired
    Lane lane;

    @Scheduled(fixedRateString = "${time_unit_cycle}")
    public void loopDrivers() {
        Iterator<Driver> driverIterator = drivers.iterator();
        while (driverIterator.hasNext()) {
            var driver = driverIterator.next();
            if (!isDriving(driver)) continue;
            driver.takeAction();
            if (isFinished(driver)) { // out of lane
                driverIterator.remove();
            }
        }
    }

    private static boolean isFinished(Driver driver) {
        return driver.getCar().getLocation() == null;
    }

    private boolean isDriving(Driver driver) {
        if (!lane.isCarExist(driver.getCar())) {
            if (lane.isAvailable()) {
                lane.insertCarAtStart(driver.getCar());
            } else {
                return false;
            }
        }
        return true;
    }

}
