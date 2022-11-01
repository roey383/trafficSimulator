package roey.com.domain;

import lombok.Getter;
import roey.com.application.ConfigProperties;

import java.util.UUID;

public class RegularCar implements Car {

    @Getter
    private final String id;
    private final Double timeUnit;
    @Getter
    private Double currentSpeed; // m/sec
    private Lane lane;
    @Getter
    private final Double length; // meter

    public RegularCar(Lane lane, Double length, ConfigProperties configProperties) {
        this.id = UUID.randomUUID().toString();
        this.currentSpeed = 0D;
        this.lane = lane;
        this.length = length;
        this.timeUnit = configProperties.getTimeUnitCycle().toMillis() / 1000D;
    }

    @Override
    public void accelerate(float acc) {

        var dist = currentSpeed + currentSpeed * timeUnit + 0.5 * acc * Math.pow(timeUnit, 2);
        currentSpeed = currentSpeed + acc * timeUnit;
        lane.advanceCar(this, dist);
    }

}
