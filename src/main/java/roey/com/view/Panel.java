package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.road.Road;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Panel extends JPanel implements ActionListener {

    private final java.util.List<Integer> LANES = java.util.List.of(273, 172, 65);
    Image carImage;
    AddCarButton addCarButton;
    java.util.Queue<Driver> drivers;
    ConfigProperties configProperties;

    public Panel(ConfigProperties configProperties, java.util.Queue<Driver> drivers, Road road) throws IOException {
        this.drivers = drivers;
        this.configProperties = configProperties;
        this.addCarButton = new AddCarButton(configProperties, drivers, road);
        this.add(addCarButton);
        var laneLength =
                configProperties.getLengths().stream().reduce(Double::sum).get().intValue() -
                        configProperties.getCarLength().intValue();
        this.setPreferredSize(new Dimension(laneLength, 400));
        this.setBackground(Color.CYAN);
        this.carImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/carTopView.png"));
        JLabel picLabel = new JLabel(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream(
                "images/3lane.png"))));
        picLabel.setPreferredSize(new Dimension(laneLength, 300));
        this.add(picLabel);
        new Timer(10, this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        var carLength = configProperties.getCarLength().intValue();
        drivers.forEach(driver -> {
            if (driver.getCar().getLocation() != null) {
                graphics2D.drawImage(carImage,
                        driver.getCar().getLocation().intValue() - carLength,
                        LANES.get(driver.getCar().getLaneInd()),
                        carLength,
                        (int) (carLength / 2.3), null);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
