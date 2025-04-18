import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorldWorker implements Runnable{
    private final World world;
    private final int startRow, endRow;

    public WorldWorker(World world, int startRow, int endRow) {
        this.world = world;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    public void run() {
        for (int i = startRow; i <= endRow; i++) {
            int prevRow = (i - 1 + world.height) % world.height;
            int nextRow = (i + 1) % world.height;

            synchronized(world.rowLocks[prevRow]) {
                synchronized(world.rowLocks[i]) {
                    synchronized(world.rowLocks[nextRow]) {
                        for (int j = 0; j < world.width; j++) {
                            if (world.world[j][i] instanceof Creature) {
                                ((Creature) world.world[j][i]).move(world);
                            }
                        }
                    }
                }
            }
        }
    }
}
