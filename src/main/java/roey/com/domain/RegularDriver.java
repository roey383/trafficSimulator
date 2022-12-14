package roey.com.domain;

import lombok.Getter;
import lombok.Setter;
import roey.com.domain.road.Road;

import java.util.concurrent.atomic.AtomicInteger;

public class RegularDriver implements Driver {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final Integer id;
    @Getter
    @Setter
    private Road road;
    private final Car car;
    private final Double responseTime;

    public RegularDriver(Road road, Car car, Double responseTime) {
        this.id = generateId();
        this.road = road;
        this.car = car;
        this.responseTime = responseTime;
    }

    private static Integer generateId() {
        return idGenerator.incrementAndGet();
    }

    @Override
    public void takeAction() {

        var dist = road.getDistToNextCar(car);
        var acc = calculateAcceleration(dist);
        car.accelerate(acc);
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public Integer getId() {
        return id;
    }

    private Double calculateAcceleration(Double distToCar) {

        var stoppingDist = calcStoppingDist();
        if ((distToCar == null || stoppingDist < distToCar) && car.getCurrentSpeed() < road.getCarSpeedLimit(car)) {
            return car.getMaxAcc();
        }
        if ((distToCar != null && stoppingDist > distToCar) || car.getCurrentSpeed() > road.getCarSpeedLimit(car)) {
            return car.getMinAcc();
        }
        // if equals
        return 0D;
    }

    private Double calcStoppingDist() {
        var responseDist = responseTime * car.getCurrentSpeed();
        var breakingDist = Math.pow(car.getCurrentSpeed() * 3.6, 2) / (254 * road.getCarFriction(car));
        return responseDist + breakingDist;
    }
}
