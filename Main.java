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
    private static final boolean ENABLE_ANIMATION = false; // Добавена константа

    public static void main(String[] args) {

        int NUM_THREADS = 4;

        int numSharks = 10000;
        int numFish = 100000;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        World w = new World(screenWidth, screenHeight, numSharks, numFish);

        // Създаваме панела с този свят

        if(ENABLE_ANIMATION) {
            WorldPanel panel = new WorldPanel(w);
            panel.setPreferredSize(new Dimension(screenWidth, screenHeight)); // задаваме размер

            JFrame frame = new JFrame("Wa-Tor Simulation");
            frame.setUndecorated(true); // маха рамката на прозореца
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel); // добавяме панела
            frame.pack(); // преоразмеряваме спрямо панела
            frame.setLocationRelativeTo(null); // центрира прозореца (не е задължително)
            frame.setVisible(true);

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0); // спира приложението
                    }
                }
            });

            // Create a fixed ExecutorService once
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<WorldWorker> workers = new ArrayList<>();
            int rowsPerThread = w.height / NUM_THREADS;

            // Initialize workers only once
            for (int i = 0; i < NUM_THREADS; i++) {
                int startRow = i * rowsPerThread;
                int endRow = (i == NUM_THREADS - 1) ? w.height - 1 : (i + 1) * rowsPerThread - 1;
                WorldWorker worker = new WorldWorker(w, startRow, endRow);
                workers.add(worker);
            }

            // Add a window listener to properly terminate the executor on window closing
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    executor.shutdownNow(); // Forcefully stop the executor
                }
            });

            // Main simulation loop
            while (true) {
                // Submit tasks to the executor service
                for (WorldWorker worker : workers) {
                    executor.submit(worker); // Use submit instead of execute for better error handling
                }

                // Await task completion before proceeding
                try {
                    // Wait for all tasks to finish in the current step by blocking until they complete
                    executor.awaitTermination(1, TimeUnit.MILLISECONDS); // Use a short timeout and repeat
                    w.resetMovedFlags(); // Reset the movement flags before repainting
                    if (ENABLE_ANIMATION) { // Проверка на константата преди прерисуване
                        panel.repaint(); // Refresh the display
                        Thread.sleep(10); // Short pause for smoother animation
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the thread's interrupted status
                    break; // Stop the simulation on interruption
                }

//                if (!ENABLE_ANIMATION) { // Ако анимацията е изключена, може да искаш да спреш цикъла след определено време или брой итерации
//                    // Например, след 1000 итерации за тестване на скоростта
//                    // frameCounter++;
//                    // if (frameCounter >= 1000) {
//                    //     break;
//                    // }
//                }
            }

            // Clean up the executor when the simulation ends
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Forcefully stop remaining tasks
                }
            } catch (InterruptedException e) {
                executor.shutdownNow(); // Force shutdown on interruption
            }
        }
        else  // =================================================
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

// Създаваме веднъж Callable задачи за invokeAll()
            List<Callable<Void>> tasks = new ArrayList<>();
            for (WorldWorker worker : workers) {
                tasks.add(() -> { worker.run(); return null; });
            }

// Старт на таймера
            long startTime = System.nanoTime();

// Основен цикъл на симулацията (без анимация)
            for (int m = 0; m < 200; m++) {
                try {
                    // Изпълняваме всички задачи и изчакваме приключване
                    executor.invokeAll(tasks);

                    // Рестартираме флаговете за движение
                    w.resetMovedFlags();

                    // Без излишно забавяне, защото анимацията е изключена
                    // (ако анимацията е включена, премести sleep в отделна версия)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

// Изключване на executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

// Спиране на таймера и принтиране на резултата
            long endTime = System.nanoTime();
            long duration = (endTime - startTime); // в наносекунди
            double durationMillis = duration / 1_000_000.0;
            System.out.println("Време за изпълнение (многонишков): " + durationMillis + " ms");

        }
//



    }
}