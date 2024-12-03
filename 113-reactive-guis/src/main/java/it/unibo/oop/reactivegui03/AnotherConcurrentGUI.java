package it.unibo.oop.reactivegui03;

import it.unibo.oop.reactivegui02.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class AnotherConcurrentGUI extends ConcurrentGUI {
    
    public AnotherConcurrentGUI() {
        super();
        
        final AnotherAgent agent = new AnotherAgent();
        new Thread(agent).start();
    }

    public final class AnotherAgent extends ConcurrentGUI.Agent{
        private int counter;

        @Override
        public void run() {
            while (!stop) {
                try {
                    Thread.sleep(1000);
                    counter++;
                    if(counter>=10) {
                        this.stopCounting();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
