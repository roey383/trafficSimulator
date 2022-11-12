package roey.com.otel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import roey.com.domain.Driver;
import roey.com.domain.road.Road;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Configuration
@EnableScheduling
public class TrafficLogger {

    private final Long startTime = System.currentTimeMillis();
    private final Map<Integer, Double> driverToSumSpeed = new HashMap<>();
    private final Map<Integer, Integer> driverToCounter = new HashMap<>();

    @Autowired
    Queue<Driver> drivers;
    @Autowired
    @Qualifier("MultiLaneArray")
    Road road;

    @Scheduled(fixedRate = 1000)
    public void logInfoOfTraffic() {
        System.out.println("Time lapsed= " + (System.currentTimeMillis() - startTime) / 1000 + ": Drivers:");
        drivers.forEach(driver -> {
            System.out.printf("Id=%s, cord=%.2f, distToNext=%.2f, speed=%.2f, speedLimit=%.0f, lane=%d%n", driver.getId(),
                    driver.getCar().getLocation(),
                    road.isCarExist(driver.getCar()) ? road.getDistToNextCar(driver.getCar()) : null,
                    driver.getCar().getCurrentSpeed() * 3.6,
                    road.getCarSpeedLimit(driver.getCar()) == null ? null :
                            road.getCarSpeedLimit(driver.getCar()) * 3.6,
                    road.getCarLaneInd(driver.getCar()));
            if (!driverToSumSpeed.containsKey(driver.getId())) {
                driverToSumSpeed.put(driver.getId(), 0D);
                driverToCounter.put(driver.getId(), 0);
            }
            if (driver.getCar().getCurrentSpeed() > 0.1) {
                driverToSumSpeed.put(driver.getId(),
                        driverToSumSpeed.get(driver.getId()) + driver.getCar().getCurrentSpeed());
                driverToCounter.put(driver.getId(), driverToCounter.get(driver.getId()) + 1);
            }
        });
        System.out.println("Total cars:" + road.getCarsCount());
        driverToSumSpeed.keySet().forEach(driverId ->
                System.out.printf("Id=%s, avgSpeed=%.2f%n", driverId,
                        driverToSumSpeed.get(driverId) * 3.6 / driverToCounter.get(driverId)));
    }

}
