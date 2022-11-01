package roey.com.domain;

import lombok.Builder;
import lombok.Data;
import roey.com.exceptions.CarCrashException;

import java.util.HashMap;
import java.util.Map;

public class RegularLane implements Lane {

    private final Double length;
    private final Map<String, CarInfo> carIdToCarInfo;
    private CarInfo firstCar; // closest to end of lane
    private CarInfo lastCar; // closest to start of lane

    public RegularLane(Double length) {
        this.length = length;
        this.carIdToCarInfo = new HashMap<>();
        this.firstCar = null;
        this.lastCar = null;
    }

    @Override
    public boolean insertCar(Car car, Double segment) {
        return false;
    }

    @Override
    public void insertCarAtStart(Car car) throws CarCrashException {

        CarInfo carInfo;
        if (firstCar == null) {
            carInfo = CarInfo.builder().car(car).laneCord(0D).build();
            this.firstCar = carInfo;
            this.lastCar = carInfo;
        } else {
            var distToNext = firstCar.getLaneCord() - firstCar.getCar().getLength();
            if (distToNext <= 0D){
                throw new CarCrashException("Another car at starting point");
            }
            carInfo = CarInfo.builder()
                    .car(car)
                    .laneCord(0D)
                    .nextCar(firstCar)
                    .distToNext(distToNext)
                    .build();
            firstCar.setPrevCar(carInfo);
            firstCar = carInfo;
        }

        carIdToCarInfo.put(car.getId(), carInfo);
    }

    @Override
    public Double getDistToNextCar(Car car) {
        if (carIdToCarInfo.get(car.getId()).hasNext()){
            return carIdToCarInfo.get(car.getId()).getDistToNext();
        }
        return null;
    }

    @Override
    public Double getSpeedOfNextCar(Car car) {
        if (carIdToCarInfo.get(car.getId()).hasNext()){
            return carIdToCarInfo.get(car.getId()).getCar().getCurrentSpeed();
        }
        return null;
    }

    @Override
    public void advanceCar(Car car, Double distance) throws CarCrashException {

        var carInfo = carIdToCarInfo.get(car.getId());
        // if has next
        if (carInfo.hasNext()) {
            if (distance > carInfo.getDistToNext()) {
                throw new CarCrashException("Car crashed the next car");
            }
            // set distances
            carInfo.setDistToNext(carInfo.getDistToNext() + distance);
        }
        // if last car and off road
        else if (carInfo.getLaneCord() + distance - carInfo.getCar().getLength() > length) {
            if (carInfo.hasPrev()){
                lastCar = carInfo.getPrevCar();
                carInfo.getPrevCar().setNextCar(null);
            }
            else {
                lastCar = null;
                firstCar = null;
            }
            carIdToCarInfo.remove(carInfo.getCar().getId());
            return;
        }

        if (carInfo.hasPrev()){
            // set distances
            var prevCar = carInfo.getPrevCar();
            prevCar.setDistToNext(prevCar.getDistToNext() + distance);
        }

        carInfo.setLaneCord(carInfo.getLaneCord() + distance);
    }

    @Override
    public boolean isCarExist(Car car) {
        return carIdToCarInfo.containsKey(car.getId());
    }

    @Builder
    @Data
    private static class CarInfo {

        private Car car;
        private Double laneCord;
        private CarInfo nextCar;
        private Double distToNext;
        private CarInfo prevCar;

        boolean hasNext() {
            return
                    nextCar != null;
        }

        boolean hasPrev() {
            return prevCar != null;
        }
    }
}
