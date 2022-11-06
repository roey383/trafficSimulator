package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class Panel extends JPanel implements ActionListener {

    Image carImage;
    AddCarButton addCarButton;
    java.util.Queue<Driver> drivers;
    ConfigProperties configProperties;

    public Panel(ConfigProperties configProperties, java.util.Queue<Driver> drivers, Lane lane) throws IOException {
        this.drivers = drivers;
        this.configProperties = configProperties;
        this.addCarButton = new AddCarButton(configProperties, drivers, lane);
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
                        273,
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
