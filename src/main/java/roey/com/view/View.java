package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.Lane;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class View extends JFrame {

    Panel panel;

    public View(ConfigProperties configProperties, Queue<Driver> drivers, Lane lane) throws HeadlessException {

        this.panel = new Panel(configProperties, drivers, lane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
