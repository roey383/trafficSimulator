package roey.com.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

@ConfigurationProperties
@Data
public class ConfigProperties {

    private Duration timeUnitCycle;
    private Double laneLength;
    private Double carLength;
    private Double laneSpeedLimit;
    private Double laneFriction;
    private Double maxAcc;
    private Double minAcc;
    private Double responseTime;
    @Value("#{'${lane_lengths}'.split(',')}")
    private List<Double> lengths;
    @Value("#{'${lane_speeds}'.split(',')}")
    private List<Double> speeds;



    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("images/carTopView.png");
        var image = ImageIO.read(is);
        System.out.println(image);
    }
}
