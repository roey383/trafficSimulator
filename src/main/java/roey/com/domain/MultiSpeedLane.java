package roey.com.domain;

import java.util.Collections;
import java.util.List;

/**
 * Assuming no big segments of speed per length ~ 10max
 * Using Binary search O(log n) -> O(log 10) = O(1).
 * Otherwise, would have keep speedLimit per CarInfo.
 */
public class MultiSpeedLane extends ListLane {

    List<Double> lengths;
    List<Double> speeds;

    public MultiSpeedLane(Double friction, List<Double> lengths, List<Double> speeds) {
        super(lengths.get(lengths.size()-1), null, friction);
        this.lengths = lengths;
        this.speeds = speeds;
    }

    @Override
    public Double getCarSpeedLimit(Car car) {
        if (!isCarExist(car)) {
            return null;
        }
        var res = Collections.binarySearch(lengths, getCarLocation(car));
        if (res * (-1) - 1 == lengths.size()){ // out of lane
            return Double.MAX_VALUE;
        }
        return speeds.get(res >= 0 ? res : res * (-1) - 1);
    }
}
