package roey.com.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicLong;

public class PauseApp implements KeyListener {

    AtomicLong atomicLong = new AtomicLong(0);

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            atomicLong.incrementAndGet();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public boolean isPaused() {
        return atomicLong.get() % 2 == 1;
    }
}
