import java.util.ArrayList;

public class Fish extends Creature{
    private static final int[] BREED_TIME = {20, 25, 30, 35};
    private int breedTime;

    public Fish(int x, int y) {
        super(x, y);
        this.breedTime = BREED_TIME[(int)(Math.random() * BREED_TIME.length)];
        if(x < 0 || y < 0) {
            throw new IllegalArgumentException("Position cannot be negative");
        }
    }

    @Override
    public void move(World world) {
        if(this.isMoved) return;

        ArrayList<Position> freePositions = getFreePositions(world);
        Position oldPosition = this.position;
        Position newPosition;

        if(!freePositions.isEmpty()){
            newPosition = freePositions.get((int)(Math.random() * freePositions.size()));
        }
        else {
            newPosition = this.position;
        }

        if(this.breedTime > 0) {
            this.breedTime--;
            this.position = newPosition;
            world.world[oldPosition.x][oldPosition.y] = null;
            world.world[this.position.x][this.position.y] = this;
        }
        else{
            this.breedTime = BREED_TIME[(int)(Math.random() * BREED_TIME.length)];
            world.world[newPosition.x][newPosition.y] = new Fish(newPosition.x, newPosition.y);
        }
        this.isMoved = true;
    }

    private ArrayList<Position> getFreePositions(World world){
        return getPositions.getFreePositions(world, this.position);
    }
}