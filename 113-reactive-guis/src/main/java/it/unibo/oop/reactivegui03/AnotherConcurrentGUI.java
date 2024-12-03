package it.unibo.oop.reactivegui03;

import it.unibo.oop.reactivegui02.ConcurrentGUI;

/**
 * Third experiment with reactive gui.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class AnotherConcurrentGUI {
    private final ConcurrentGUI c;
    private static final int MAX_RUNNING = 10_000;
    /**
     * ignore.
     */
    public AnotherConcurrentGUI() {
        c = new ConcurrentGUI();
        new Thread( new AnotherAgent()).start();
    }

    /**
     * ignore.
     */
    private final class AnotherAgent implements Runnable {

        @Override
        public void run() {
            while (!c.getAgent().isStopped()) {
                try {
                    Thread.sleep(MAX_RUNNING);
                    c.getAgent().stopCounting();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
