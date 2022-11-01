package roey.com.domain;

import lombok.Getter;
import roey.com.application.ConfigProperties;

import java.util.concurrent.atomic.AtomicInteger;

public class RegularCar implements Car {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    @Getter
    private final int id;
    private final Double timeUnit;
    @Getter
    private Double currentSpeed; // m/sec
    private Lane lane;
    @Getter
    private final Double length; // meter
    private final Double maxAcc; // meter/sec^2
    private final Double minAcc;

    public RegularCar(Lane lane, Double length, ConfigProperties configProperties) {
        this(lane, length, configProperties.getMaxAcc(), configProperties.getMinAcc(),
                configProperties.getTimeUnitCycle().toMillis());
    }

    public RegularCar(Lane lane, Double length, Double maxAcc, Double minAcc, long timeUnitMillis) {
        this.id = generateId();
        this.currentSpeed = 0D;
        this.lane = lane;
        this.length = length;
        this.timeUnit = timeUnitMillis / 1000D;
        this.maxAcc = maxAcc;
        this.minAcc = minAcc;
    }

    private static Integer generateId() {
        return idGenerator.incrementAndGet();
    }

    @Override
    public void accelerate(Double acc) {

        var dist = currentSpeed * timeUnit + 0.5 * acc * Math.pow(timeUnit, 2);
        currentSpeed = currentSpeed + acc * timeUnit;
        lane.advanceCar(this, dist);
    }

    @Override
    public Double getMaxAcc() {
        return maxAcc;
    }

    @Override
    public Double getMinAcc() {
        return minAcc;
    }

    @Override
    public Double getLocation() {
        return lane.getCarLocation(this);
    }

}
