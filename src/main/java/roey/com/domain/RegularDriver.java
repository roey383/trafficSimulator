package roey.com.domain;

public class RegularDriver implements Driver {

    Lane lane;
    Car car;

    @Override
    public void takeAction() {

        var dist = lane.getDistToNextCar(car);
        float acc = calculateAccelaration(dist);
        car.accelerate(acc);
    }

    private float calculateAccelaration(Double dist) {
        return 0;
    }
}
