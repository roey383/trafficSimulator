package roey.com.domain;

import java.util.Collections;
import java.util.List;

public class MultiSpeedLane extends RegularLane {

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
