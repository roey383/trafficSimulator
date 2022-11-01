package roey.com.application;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties
@Data
public class ConfigProperties {

    private Duration timeUnitCycle;
}
