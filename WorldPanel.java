import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel{
    private final World world;

    public WorldPanel(World world) {
        this.world = world;
        setPreferredSize(new Dimension(world.width, world.height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < world.width; x++) {
            for (int y = 0; y < world.height; y++) {
                if (world.world[x][y] == null) {
                    g.setColor(Color.BLUE);
                } else if (world.world[x][y] instanceof Fish) {
                    g.setColor(Color.GREEN);
                } else if (world.world[x][y] instanceof Shark) {
                    g.setColor(Color.RED);
                }
                g.fillRect(x, y, 1, 1);
            }
        }
    }
}