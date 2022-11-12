package roey.com.domain;

import roey.com.domain.road.Road;
import roey.com.exceptions.CarCrashException;
import roey.com.exceptions.CarNotInLaneException;

public abstract class OneLaneRoad implements Road {

    @Override
    public boolean hasLeftLane(Car car) {
        return false;
    }

    @Override
    public boolean hasRightLane(Car car) {
        return false;
    }
    @Override
    public void switchToLeftLane(Car car) throws CarCrashException {
        throw new CarNotInLaneException("No left lane");
    }

    @Override
    public void switchToRightLane(Car car) throws CarCrashException {
        throw new CarNotInLaneException("No left lane");
    }

    @Override
    public Double getDistToLeftCar(Car car) {
        throw new CarNotInLaneException("No left lane");
    }

    @Override
    public Double getDistToRightCar(Car car) {
        throw new CarNotInLaneException("No left lane");
    }

    /**
     * X inclusive, y inclusive
     */
    public abstract boolean isFreeSegment(double x, double y);
}
