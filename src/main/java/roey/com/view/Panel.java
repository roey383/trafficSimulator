package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel extends JPanel implements ActionListener {

    Image carImage;
    AddCarButton addCarButton;
    java.util.Queue<Driver> drivers;

    public Panel(ConfigProperties configProperties, java.util.Queue<Driver> drivers, Lane lane) {
        this.drivers = drivers;
        this.addCarButton = new AddCarButton(configProperties, drivers, lane);
        this.add(addCarButton);
        this.setPreferredSize(new Dimension(1000, 400));
        this.carImage = new ImageIcon("car.png").getImage();
        this.setBackground(Color.CYAN);
        new Timer(10, this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        drivers.forEach(driver -> {
            if (driver.getCar().getLocation() != null) {
                graphics2D.drawImage(carImage, driver.getCar().getLocation().intValue()*5-20,
                        150, 20, 20, null);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
