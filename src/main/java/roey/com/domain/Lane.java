package roey.com.domain;

import roey.com.exceptions.CarCrashException;

public interface Lane {

    boolean insertCar(Car car, Double segment);

    boolean isAvailable();

    void insertCarAtStart(Car car) throws CarCrashException;

    Double getDistToNextCar(Car car);

    void advanceCar(Car car, Double distance) throws CarCrashException;

    boolean isCarExist(Car car);

    Double getSpeedLimit();

    Double getFriction();

    Double getCarLocation(Car car);

    int getCarsCount();
}
