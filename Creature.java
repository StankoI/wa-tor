
public class Creature extends WorldObject {

    protected int breedTime;
    protected boolean isMoved;

    public Creature(int x, int y, int breedTime) {
        super(x, y);
        this.breedTime = breedTime;
        this.isMoved = false;
    }

    public void move(World world) {}

}