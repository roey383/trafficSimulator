package roey.com.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import roey.com.domain.Driver;
import roey.com.domain.Lane;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

@Configuration
@EnableScheduling
public class DriverScheduler {

    private final Long startTime = System.currentTimeMillis();
    private Map<Integer, Double> driverToSumSpeed = new HashMap<>();
    private Map<Integer, Integer> driverToCounter = new HashMap<>();

    @Autowired
    Queue<Driver> drivers;
    @Autowired
    Lane lane;

    @Scheduled(fixedRateString = "${time_unit_cycle}")
    public void loopDrivers() {
        Iterator<Driver> driverIterator = drivers.iterator();
        while (driverIterator.hasNext()) {
            var driver = driverIterator.next();
            if (!lane.isCarExist(driver.getCar())) {
                if (lane.isAvailable()) {
                    lane.insertCarAtStart(driver.getCar());
                } else {
                    continue;
                }
            }
            driver.takeAction();
            if (driver.getCar().getLocation() == null) { // out of lane
                driverIterator.remove();
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void logInfoOfTraffic() {
        System.out.println("Time lapsed= " + (System.currentTimeMillis() - startTime) / 1000 + ": Drivers:");
        drivers.forEach(driver -> {
            System.out.println(String.format("Id=%s, cord=%.2f, speed=%.2f, distToNext=%.2f", driver.getId(),
                    driver.getCar().getLocation(), driver.getCar().getCurrentSpeed()*3.6,
                    lane.isCarExist(driver.getCar()) ? lane.getDistToNextCar(driver.getCar()) : null));
            if (!driverToSumSpeed.containsKey(driver.getId())){
                driverToSumSpeed.put(driver.getId(), 0D);
                driverToCounter.put(driver.getId(), 0);
            }
            if (driver.getCar().getCurrentSpeed() > 0.1){
                driverToSumSpeed.put(driver.getId(), driverToSumSpeed.get(driver.getId()) + driver.getCar().getCurrentSpeed());
                driverToCounter.put(driver.getId(), driverToCounter.get(driver.getId()) + 1);
            }
        });
        System.out.println("Total cars:" + lane.getCarsCount());
        driverToSumSpeed.keySet().forEach(driverId -> {
            System.out.println(String.format("Id=%s, avgSpeed=%.2f", driverId,
                    driverToSumSpeed.get(driverId)*3.6/driverToCounter.get(driverId)));
        });
    }

}
