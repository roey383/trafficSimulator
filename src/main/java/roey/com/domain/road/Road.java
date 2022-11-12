package roey.com.domain.road;

import roey.com.domain.Car;
import roey.com.exceptions.CarCrashException;

public interface Road {

    void insertCar(Car car, Double segment);

    boolean isAvailable();

    void insertCarAtStart(Car car) throws CarCrashException;

    Double getDistToNextCar(Car currentCar);

    Double getDistToNextCar(Double segment);

    void advanceCar(Car car, Double distance) throws CarCrashException;

    boolean isCarExist(Car car);

    Double getCarSpeedLimit(Car car);

    Double getCarFriction(Car car);

    Double getCarLocation(Car car);

    int getCarLaneInd(Car car);

    int getCarsCount();

    Double getLength();

    void removeCar(Car car);

    boolean hasLeftLane(Car car);

    boolean hasRightLane(Car car);

    void switchToLeftLane(Car car) throws CarCrashException;

    void switchToRightLane(Car car) throws CarCrashException;

    Double getDistToLeftCar(Car car);

    Double getDistToRightCar(Car car);

}
