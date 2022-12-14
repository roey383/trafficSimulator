package roey.com.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import roey.com.exceptions.CarCrashException;
import roey.com.exceptions.CarNotInLaneException;

import java.util.LinkedHashMap;

/**
 * Implemented by Lists
 */
public class ListLane extends OneLaneRoad {

    @Getter
    private final Double length; // Meter
    private final LinkedHashMap<Integer, CarInfo> carIdToCarInfo;
    private CarInfo firstCar; // closest to end of lane
    // closest to start of lane
    private final Double speedLimit; // in KM
    private final Double friction;

    public ListLane(Double length, Double speedLimit, Double friction) {
        this.length = length;
        this.carIdToCarInfo = new LinkedHashMap<>();
        this.firstCar = null;
        this.speedLimit = speedLimit;
        this.friction = friction;
    }

    @Override
    public void insertCar(Car car, Double segment) {
    }

    @Override
    public boolean isAvailable() {
        if (firstCar == null){
            return true;
        }
        if (firstCar.getLaneCord() - firstCar.getCar().getLength() <= 0D){
            return false;
        }
        return true;
    }

    @Override
    public void insertCarAtStart(Car car) throws CarCrashException {

        CarInfo carInfo;
        if (firstCar == null) {
            carInfo = CarInfo.builder().car(car).laneCord(0D).build();
            this.firstCar = carInfo;
        } else {
            var distToNext = firstCar.getLaneCord() - firstCar.getCar().getLength();
            if (distToNext <= 0D) {
                throw new CarCrashException("Another car at starting point");
            }
            carInfo = CarInfo.builder()
                    .car(car)
                    .laneCord(0D)
                    .nextCar(firstCar)
                    .build();
            firstCar.setPrevCar(carInfo);
            firstCar = carInfo;
        }

        carIdToCarInfo.put(car.getId(), carInfo);
    }

    @Override
    public Double getDistToNextCar(Car car) {
        if (!carIdToCarInfo.containsKey(car.getId())) {
            throw new CarNotInLaneException("Car is not in lane");
        }
        return carIdToCarInfo.get(car.getId()).getDistToNext();
    }

    @Override
    public Double getDistToNextCar(Double segment) {
        return null;
    }

    @Override
    public void advanceCar(Car car, Double distance) throws CarCrashException {

        var carInfo = carIdToCarInfo.get(car.getId());
        // if collusion with next
        if (carInfo.hasNext() && distance > carInfo.getDistToNext()) {
            throw new CarCrashException("Car crashed the next car");
        }
        // if last car and off road
        else if (carInfo.getLaneCord() + distance - carInfo.getCar().getLength() > length) {
            if (carInfo.hasPrev()) {
                carInfo.getPrevCar().setNextCar(null);
            } else {
                firstCar = null;
            }
            carIdToCarInfo.remove(carInfo.getCar().getId());
            return;
        }

        carInfo.setLaneCord(carInfo.getLaneCord() + distance);
    }

    @Override
    public boolean isCarExist(Car car) {
        return carIdToCarInfo.containsKey(car.getId());
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
        if (carIdToCarInfo.containsKey(car.getId())) {
            return carIdToCarInfo.get(car.getId()).getLaneCord();
        }
        return null;
    }

    @Override
    public int getCarLaneInd(Car car) {
        return 0;
    }

    @Override
    public int getCarsCount() {
        return carIdToCarInfo.size();
    }

    @Override
    public void removeCar(Car car) {
        // TBD
    }

    @Override
    public boolean isFreeSegment(double x, double y) {
        return true; // TBD
    }

    @Builder
    @Data
    private static class CarInfo {

        private Car car;
        private Double laneCord;
        private CarInfo nextCar;
        private CarInfo prevCar;

        boolean hasNext() {
            return nextCar != null;
        }

        boolean hasPrev() {
            return prevCar != null;
        }

        public Double getDistToNext() {
            if (!hasNext()) {
                return null;
            }
            var nextCar = getNextCar();
            return nextCar.getLaneCord() - nextCar.getCar().getLength() - getLaneCord();
        }
    }
}
