abstract class Creature {
    protected boolean isMoved;
    protected Position position;

    public Creature(int x, int y) {
        position = new Position(x, y);
        this.isMoved = false;
    }
    public void move(World world) {}
}