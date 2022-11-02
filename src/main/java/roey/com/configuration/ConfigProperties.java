package roey.com.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties
@Data
public class ConfigProperties {

    private Duration timeUnitCycle;
    private Double laneLength;
    private Double laneSpeedLimit;
    private Double laneFriction;
    private Double maxAcc;
    private Double minAcc;
    private Double responseTime;
}
