
public class Creature extends WorldObject {
    protected boolean isMoved;

    public Creature(int x, int y) {
        super(x, y);
        this.isMoved = false;
    }
    public void move(World world) {}
}