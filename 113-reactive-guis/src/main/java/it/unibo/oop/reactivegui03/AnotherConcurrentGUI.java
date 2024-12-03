package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui, solution using lambdas.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class AnotherConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private static final long WAITING_TIME = TimeUnit.SECONDS.toMillis(10);

    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");

    private final CounterAgent counterAgent = new CounterAgent();

    /**
     * Builds a C3GUI.
     */
    @SuppressWarnings("CPD-START")
    public AnotherConcurrentGUI() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        up.addActionListener(e -> counterAgent.upCounting());
        down.addActionListener(e -> counterAgent.downCounting());
        stop.addActionListener(e -> this.stopAll());
        new Thread(counterAgent).start();
        new Thread(() -> {
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.stopAll();
        }).start();
    }

    private void stopAll() {
        counterAgent.stop();
        SwingUtilities.invokeLater(() -> {
            stop.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
        });
    }

    private class CounterAgent implements Runnable, Serializable {
        private static final long serialVersionUID = 1L;
        private volatile boolean stop;
        private volatile boolean up = true;

        @Override
        public void run() {
            int counter = 0;
            while (!stop) {
                try {
                    final var nextText = Integer.toString(counter);
                    SwingUtilities.invokeAndWait(() -> display.setText(nextText));
                    counter += up ? 1 : -1;
                    Thread.sleep(100);
                } catch (InterruptedException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }
        public void stop() {
            this.stop = true;
        }
        public void upCounting() {
            this.up = true;
        }
        public void downCounting() {
            this.up = false;
        }
    }
}