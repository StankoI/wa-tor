abstract class WorldObject {
    protected Position position;

    public WorldObject(int x, int y) {
        this.position = new Position(x, y);
    }
}