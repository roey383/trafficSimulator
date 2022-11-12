package roey.com.view;

import roey.com.configuration.ConfigProperties;
import roey.com.domain.Driver;
import roey.com.domain.road.Road;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Queue;

public class View extends JFrame {

    Panel panel;

    public View(ConfigProperties configProperties, Queue<Driver> drivers, Road road) throws HeadlessException, IOException {

        this.panel = new Panel(configProperties, drivers, road);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
