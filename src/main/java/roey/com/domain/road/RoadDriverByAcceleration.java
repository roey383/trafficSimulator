package roey.com.domain.road;

import lombok.Getter;
import lombok.Setter;
import roey.com.domain.Car;
import roey.com.domain.Driver;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Character:
 * Driver chooses lane by acceleration and not avoiding crashes intentionally
 * Rather accelerate on righter lanes
 * Don't care about back cars SD
 */
public class RoadDriverByAcceleration implements Driver {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final Integer id;
    @Getter
    @Setter
    private Road road;
    private final Car car;
    private final Double responseTime;
    private final StateSwitched stateSwitched;

    public RoadDriverByAcceleration(Road road, Car car, Double responseTime) {
        this.id = generateId();
        this.road = road;
        this.car = car;
        this.responseTime = responseTime;
        this.stateSwitched = new StateSwitched(responseTime);
    }

    private static Integer generateId() {
        return idGenerator.incrementAndGet();
    }

    @Override
    public void takeAction() {

        Double maxAcc, rightAcc, stayAcc, leftAcc;
        stayAcc = calculateAcceleration(road.getDistToNextCar(car));
        maxAcc = stayAcc;

        if (!stateSwitched.isSwitched()) {
            rightAcc = road.hasRightLane(car) ? calculateAcceleration(road.getDistToRightCar(car)) :
                    Double.MAX_VALUE * (-1);
            leftAcc = road.hasLeftLane(car) ? calculateAcceleration(road.getDistToLeftCar(car)) :
                    Double.MAX_VALUE * (-1);
            maxAcc = Math.max(rightAcc, Math.max(stayAcc, leftAcc));

            if (maxAcc.equals(rightAcc)) {
                road.switchToRightLane(car);
                stateSwitched.setSwitched();
            } else if (!maxAcc.equals(stayAcc)) {
                road.switchToLeftLane(car);
                stateSwitched.setSwitched();
            }
        }

        car.accelerate(maxAcc);
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

        // if crashed
        if (distToCar != null && distToCar <= 0){
            return Double.MAX_VALUE*(-1);
        }

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

    private static class StateSwitched {

        private final int SWITCH_DELAY;
        private long switchedTime = System.currentTimeMillis();
        private boolean switched = false;

        public StateSwitched(Double responseTimeSec) {
            SWITCH_DELAY = (int) (responseTimeSec*1000);
        }

        public boolean isSwitched() {
            if (switched && System.currentTimeMillis() - switchedTime > SWITCH_DELAY){
                switched = false;
            }
            return switched;
        }

        public void setSwitched() {
            switched = true;
            switchedTime = System.currentTimeMillis();
        }
    }

}
