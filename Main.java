import java.util.ArrayList;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

public class Main {
    private static final boolean ENABLE_ANIMATION = true;

    public static void main(String[] args) {

        int NUM_THREADS = 8;

        int numSharks = 10000;
        int numFish = 100000;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        World w = new World(screenWidth, screenHeight, numSharks, numFish);

        if(ENABLE_ANIMATION) {
            WorldPanel panel = new WorldPanel(w);
            panel.setPreferredSize(new Dimension(screenWidth, screenHeight));

            JFrame frame = new JFrame("Wa-Tor Simulation");
            frame.setUndecorated(true); // маха рамката на прозореца
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }
                }
            });
            //testing
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<WorldWorker> workers = new ArrayList<>();
            int rowsPerThread = w.height / NUM_THREADS;

            for (int i = 0; i < NUM_THREADS; i++) {
                int startRow = i * rowsPerThread;
                int endRow = (i == NUM_THREADS - 1) ? w.height - 1 : (i + 1) * rowsPerThread - 1;
                WorldWorker worker = new WorldWorker(w, startRow, endRow);
                workers.add(worker);
            }

            List<Callable<Void>> tasks = new ArrayList<>();
            for (WorldWorker worker : workers) {
                tasks.add(() -> { worker.run(); return null; });
            }

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    executor.shutdownNow();
                }
            });

            while(true) {
                try {
                    executor.invokeAll(tasks);

                    w.resetMovedFlags();
                    panel.repaint();
                    Thread.sleep(10);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
        else  // =================================================  test
        {
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<WorldWorker> workers = new ArrayList<>();
            int rowsPerThread = w.height / NUM_THREADS;

            for (int i = 0; i < NUM_THREADS; i++) {
                int startRow = i * rowsPerThread;
                int endRow = (i == NUM_THREADS - 1) ? w.height - 1 : (i + 1) * rowsPerThread - 1;
                WorldWorker worker = new WorldWorker(w, startRow, endRow);
                workers.add(worker);
            }

            List<Callable<Void>> tasks = new ArrayList<>();
            for (WorldWorker worker : workers) {
                tasks.add(() -> { worker.run(); return null; });
            }

            long startTime = System.nanoTime();

            for (int m = 0; m < 200; m++) {
                try {
                    executor.invokeAll(tasks);
                    w.resetMovedFlags();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // в наносекунди
            double durationMillis = duration / 1_000_000.0;
            System.out.println("Време за изпълнение (многонишков): " + durationMillis + " ms");

        }
    }
}