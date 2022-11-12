package roey.com.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roey.com.configuration.ConfigProperties;
import roey.com.exceptions.CarCrashException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListLaneTest {

    ListLane lane;
    ConfigProperties configProperties = new ConfigProperties();

    @BeforeEach
    void setup() {
        lane = new ListLane(1000D, 100D, 0.7);
        configProperties.setTimeUnitCycle(Duration.ofMillis(1));
    }

    @Test
    void insertCarAtStart() {

        var firstCar = new RegularCar(lane, 4D, configProperties);
        lane.insertCarAtStart(firstCar);

        assertThat(lane.isCarExist(firstCar)).isTrue();
        assertThat(lane.getDistToNextCar(firstCar)).isNull();

        var secondCar = new RegularCar(lane, 4D, configProperties);
        assertThatThrownBy(() -> lane.insertCarAtStart(secondCar))
                .isInstanceOf(CarCrashException.class)
                .hasMessage("Another car at starting point");

    }

    @Test
    void advanceCar() {

        var firstCar = new RegularCar(lane, 4D, configProperties);
        lane.insertCarAtStart(firstCar);
        lane.advanceCar(firstCar, 1005D);

        assertThat(lane.isCarExist(firstCar)).isFalse();

        lane.insertCarAtStart(firstCar);
        lane.advanceCar(firstCar, 100D);

        var secondCar = new RegularCar(lane, 3D, configProperties);
        lane.insertCarAtStart(secondCar);

        assertThat(lane.isCarExist(secondCar)).isTrue();
        assertThat(lane.getDistToNextCar(secondCar)).isEqualTo(100 - 4);

        lane.advanceCar(firstCar, 905D);

        assertThat(lane.isCarExist(firstCar)).isFalse();
        assertThat(lane.getDistToNextCar(secondCar)).isNull();

        var thirdCar = new RegularCar(lane, 5D, configProperties);
        lane.advanceCar(secondCar, 100D);
        lane.insertCarAtStart(thirdCar);

        assertThat(lane.getDistToNextCar(thirdCar)).isEqualTo(100 - 3);
        assertThatThrownBy(() -> lane.advanceCar(thirdCar, 100 - 3 + 1D))
                .isInstanceOf(CarCrashException.class)
                .hasMessage("Car crashed the next car");

    }
}