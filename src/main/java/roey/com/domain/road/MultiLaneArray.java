package roey.com.domain.road;

import lombok.Getter;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import roey.com.domain.Car;
import roey.com.domain.OneLaneRoad;
import roey.com.exceptions.CarCrashException;
import roey.com.exceptions.CarNotInLaneException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiLaneArray implements Road {

    private final List<OneLaneRoad> lanes;
    @Getter
    private final Double length;
    private final Map<Car, Integer> carToLane;
    private final int lanesCount;

    public MultiLaneArray(List<OneLaneRoad> lanes) {
        if (lanes.size() < 2) {
            throw new BeanDefinitionValidationException("MultiLane have at least 2 lanes");
        }
        this.lanes = lanes;
        this.length = lanes.get(0).getLength();
        this.carToLane = new HashMap<>();
        this.lanesCount = lanes.size();
    }

    /**
     * To right lane
     */
    @Override
    public void insertCar(Car car, Double segment) {
        lanes.get(0).insertCar(car, segment);
        carToLane.put(car, 0);
    }

    /**
     * Only right lane
     */
    @Override
    public boolean isAvailable() {
        return lanes.get(0).isAvailable();
    }

    @Override
    public void insertCarAtStart(Car car) throws CarCrashException {
        lanes.get(0).insertCarAtStart(car);
        carToLane.put(car, 0);
    }

    /**
     * Only for right lane
     */
    @Override
    public Double getDistToNextCar(Car car) {
        return getCarLane(car).getDistToNextCar(car);
    }

    /**
     * Only for right lane
     * @param segment
     * @return
     */
    @Override
    public Double getDistToNextCar(Double segment) {
        return lanes.get(0).getDistToNextCar(segment);
    }

    @Override
    public void advanceCar(Car car, Double distance) throws CarCrashException {
        getCarLane(car).advanceCar(car, distance);
        if (!getCarLane(car).isCarExist(car)){
            carToLane.remove(car);
        }
    }

    @Override
    public boolean isCarExist(Car car) {
        return carToLane.containsKey(car);
    }

    @Override
    public Double getCarSpeedLimit(Car car) {
        if (!carToLane.containsKey(car)){
            return null;
        }
        return getCarLane(car).getCarSpeedLimit(car);
    }

    @Override
    public Double getCarFriction(Car car) {
        if (!carToLane.containsKey(car)){
            return null;
        }
        return getCarLane(car).getCarFriction(car);
    }

    @Override
    public Double getCarLocation(Car car) {
        if (!carToLane.containsKey(car)){
            return null;
        }
        return getCarLane(car).getCarLocation(car);
    }

    @Override
    public int getCarLaneInd(Car car) {
        if (!carToLane.containsKey(car)){
            return 0;
        }
        return carToLane.get(car);
    }

    @Override
    public int getCarsCount() {
        return carToLane.size();
    }

    @Override
    public void removeCar(Car car) {
        if (!carToLane.containsKey(car)){
            return;
        }
        getCarLane(car).removeCar(car);
        carToLane.remove(car);
    }

    @Override
    public boolean hasLeftLane(Car car) {
        return carToLane.get(car) < lanesCount - 1;
    }

    @Override
    public boolean hasRightLane(Car car) {
        return carToLane.get(car) > 0;
    }

    @Override
    public void switchToLeftLane(Car car) throws CarCrashException {
        if (!hasLeftLane(car)){
            throw new CarNotInLaneException("There is no left lane");
        }
        var currLane = getCarLane(car);
        var leftLane = lanes.get(carToLane.get(car)+1);
        leftLane.insertCar(car, car.getLocation());
        currLane.removeCar(car);
        carToLane.put(car, carToLane.get(car)+1);
    }

    @Override
    public void switchToRightLane(Car car) throws CarCrashException {
        if (!hasRightLane(car)){
            throw new CarNotInLaneException("There is no right lane");
        }
        var currLane = getCarLane(car);
        var rightLane = lanes.get(carToLane.get(car)-1);
        rightLane.insertCar(car, car.getLocation());
        currLane.removeCar(car);
        carToLane.put(car, carToLane.get(car)-1);
    }

    @Override
    public Double getDistToLeftCar(Car car) {
        if (!hasLeftLane(car)){
            throw new CarNotInLaneException("There is no right lane so no dist to left car");
        }
        var leftLane = lanes.get(carToLane.get(car)+1);
        if(leftLane.isFreeSegment(car.getLocation() - car.getLength(), car.getLocation())){
            return -1D;
        }
        return leftLane.getDistToNextCar(car.getLocation());
    }

    @Override
    public Double getDistToRightCar(Car car) {
        if (!hasRightLane(car)){
            throw new CarNotInLaneException("There is no right lane so no dist to right car");
        }
        var rightLane = lanes.get(carToLane.get(car)-1);
        if(rightLane.isFreeSegment(car.getLocation() - car.getLength(), car.getLocation())){
            return Double.MAX_VALUE*(-1);
        }
        return rightLane.getDistToNextCar(car.getLocation());
    }

    private OneLaneRoad getCarLane(Car car) {
        if (carToLane.get(car) == null){
            return null;
        }
        return lanes.get(carToLane.get(car));
    }
}
