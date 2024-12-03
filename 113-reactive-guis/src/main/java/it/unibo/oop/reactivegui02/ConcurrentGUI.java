package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Second example of reactive GUI.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public class ConcurrentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    
    /**
     * constructor.
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        final JButton stop = new JButton("stop");
        final JButton up = new JButton("up");
        final JButton down = new JButton("down");
        panel.add(display);
        panel.add(stop);
        panel.add(up);
        panel.add(down);
        super.getContentPane().add(panel);
        super.setVisible(true);
        final Agent agent = new Agent();
        new Thread(agent).start();
        stop.addActionListener((e) -> agent.stopCounting());
        up.addActionListener((e) -> agent.upCounter());
        down.addActionListener((e) -> agent.downCounter());
    }

    /**
     * ignore.
     */
    public class Agent implements Runnable, Serializable {
        private static final long serialVersionUID = 1L;
        private volatile boolean stop;
        private volatile boolean up;
        private int counter;

        /**
         * ignore.
         */
        public Agent() {
            up = true;
            counter = 0;
            stop = false;
        }
        /**
         * run.
         */
        @Override
        public void run() {
            while (!this.stop) {
                try {
                    final var nextText = Integer.toString(this.counter);
                    this.counter += up ? 1 : -1;
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(nextText));
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        /**
         * ignore.
         */
        public void stopCounting() {
            this.stop = true;

        }

        /**
         * ignore.
         */
        public void upCounter() {
            up = true;
        }

        /**
         * ignore.
         */
        public void downCounter() {
            up = false;
        }

        /**
         * ignore.
         * @return stop.
         */
        public boolean isStopped() {
            return stop;
        }

    }
}
