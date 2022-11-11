package roey.com.domain;

import lombok.Getter;
import roey.com.exceptions.CarCrashException;

import java.util.HashMap;
import java.util.Map;

public class ArrayLane implements Lane {

    private final boolean[] occupied;
    @Getter
    private final int length; // Meter
    private final Map<Car, Double> carToLocation;
    private final Double speedLimit; // in KM
    private final Double friction;

    public ArrayLane(int length, Double speedLimit, Double friction) {
        this.occupied = new boolean[length];
        this.length = length;
        this.speedLimit = speedLimit;
        this.friction = friction;
        this.carToLocation = new HashMap<>();
    }

    @Override
    public void insertCar(Car car, Double segment) {
        var segRound = segment.intValue();
        for (int i = segRound; i > segRound - car.getLength(); i--) {
            if (occupied[i]) {
                throw new CarCrashException("Location is occupied");
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
        double maxDist = calcMaxStoppingDist();
        var currentLocation = carToLocation.get(car).intValue();
        for (int i = currentLocation + 1; i <= currentLocation + maxDist; i++) {
            if (i < length && occupied[i]) {
                return (double) i;
            }
        }
        return null;
    }

    private Double calcMaxStoppingDist() {
        var responseDist = 1.5 * speedLimit;
        var breakingDist = Math.pow(speedLimit * 3.6, 2) / (254 * friction);
        return responseDist + breakingDist;
    }

    @Override
    public void advanceCar(Car car, Double distance) throws CarCrashException {
        var currentLocation = carToLocation.get(car);
        var currentLocationRound = currentLocation.intValue();
        var newLocation = currentLocation + distance;
        var newLocationRound = (int)newLocation;
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
    public int getCarsCount() {
        return carToLocation.size();
    }
}
