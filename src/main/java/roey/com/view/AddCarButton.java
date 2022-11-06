package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;
import roey.com.domain.RegularCar;
import roey.com.domain.RegularDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

public class AddCarButton extends JButton implements ActionListener {

    Queue<Driver> drivers;
    Lane lane;
    ConfigProperties configProperties;

    public AddCarButton(ConfigProperties configProperties, Queue<Driver> drivers, Lane lane) {
        this.configProperties = configProperties;
        this.drivers = drivers;
        this.lane = lane;
        this.setBounds(0,0,250,100);
        this.addActionListener(this);
        this.setText("Add car");
        this.setFocusable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var car = new RegularCar(lane, configProperties.getCarLength(), configProperties);
        var driver = new RegularDriver(lane, car, configProperties.getResponseTime());
        drivers.add(driver);
    }
}
