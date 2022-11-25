package roey.com.domain;

import lombok.Getter;
import roey.com.exceptions.CarCrashException;

import java.util.HashMap;
import java.util.Map;

public class ArrayLane extends OneLaneRoad {

    private final boolean[] occupied;
    @Getter
    private final Double length; // Meter
    private final Map<Car, Double> carToLocation;
    private final Double speedLimit; // in KM
    private final Double friction;

    public ArrayLane(int length, Double speedLimit, Double friction) {
        this.occupied = new boolean[length];
        this.length = (double) length;
        this.speedLimit = speedLimit;
        this.friction = friction;
        this.carToLocation = new HashMap<>();
    }

    @Override
    public void insertCar(Car car, Double segment) {
        var segRound = Math.min(length.intValue() - 1, segment.intValue());
        for (int i = segRound; i > segRound - car.getLength() && i >= 0; i--) {
            if (occupied[i]) {
                throw new CarCrashException("Location is occupied so can't insert car for carId = " + car.getId() +
                        " at i=" + i);
            }
            occupied[i] = true;
        }
        carToLocation.put(car, segment);
    }

    @Override
    public boolean isAvailable() {
        return !occupied[0];
    }

    @Override
    public void insertCarAtStart(Car car) throws CarCrashException {
        if (occupied[0]) {
            throw new CarCrashException("Start is occupied");
        }
        occupied[0] = true;
        carToLocation.put(car, 0D);
    }

    @Override
    public Double getDistToNextCar(Car car) {
        return getDistToNextCar(carToLocation.get(car));
    }

    @Override
    public Double getDistToNextCar(Double segment) {
        double maxDist = calcMaxStoppingDist(segment);
        var currentLocation = segment.intValue();
        for (int i = currentLocation + 1; i <= currentLocation + maxDist && i < length; i++) {
            if (occupied[i]) {
                return (double) i - currentLocation;
            }
        }
        return null;
    }

    Double calcMaxStoppingDist(Double segment) {
        var responseDist = 1.5 * speedLimit;
        var breakingDist = Math.pow(speedLimit * 3.6, 2) / (254 * friction);
        return responseDist + breakingDist;
    }

    @Override
    public void advanceCar(Car car, Double distance) throws CarCrashException {
        var currentLocation = carToLocation.get(car);
        var currentLocationRound = currentLocation.intValue();
        var newLocation = currentLocation + distance;
        var newLocationRound = (int) newLocation;
        for (int i = currentLocationRound; i > currentLocationRound - car.getLength() && i >= 0; i--) {
            if (i < length) {
                occupied[i] = false;
            }
        }
        if (newLocationRound - car.getLength() >= length) {
            carToLocation.remove(car);
            return;
        }
        for (int i = newLocationRound; i > newLocationRound - car.getLength() && i >= 0; i--) {
            if (i < length) {
                if (occupied[i]) {
                    throw new CarCrashException("Location is occupied");
                }
                occupied[i] = true;
            }
        }
        carToLocation.put(car, newLocation);
    }

    @Override
    public boolean isCarExist(Car car) {
        return carToLocation.containsKey(car);
    }

    @Override
    public Double getCarSpeedLimit(Car car) {
        return speedLimit;
    }

    @Override
    public Double getCarFriction(Car car) {
        return friction;
    }

    @Override
    public Double getCarLocation(Car car) {
        return carToLocation.get(car) == null ? null : carToLocation.get(car);
    }

    @Override
    public int getCarLaneInd(Car car) {
        return 0;
    }

    @Override
    public int getCarsCount() {
        return carToLocation.size();
    }

    @Override
    public void removeCar(Car car) {
        var currentLocation = carToLocation.get(car);
        var currentLocationRound = currentLocation.intValue();
        for (int i = currentLocationRound; i > currentLocationRound - car.getLength() && i >= 0; i--) {
            if (i < length) {
                occupied[i] = false;
            }
        }
        carToLocation.remove(car);
    }

    @Override
    public boolean isFreeSegment(double x, double y) {
        for (int i = (int) x; i <= y; i++) {
            if (i >= 0 && i < length && occupied[i]) {
                return true;
            }
        }
        return false;
    }
}
