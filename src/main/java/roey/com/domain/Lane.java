package roey.com.domain;

import roey.com.exceptions.CarCrashException;

public interface Lane {

    boolean insertCar(Car car, Double segment);

    void insertCarAtStart(Car car) throws CarCrashException;

    Double getDistToNextCar(Car car);
    Double getSpeedOfNextCar(Car car);

    void advanceCar(Car car, Double distance) throws CarCrashException;

    boolean isCarExist(Car car);
}
