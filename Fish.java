import java.util.ArrayList;

public class Fish extends Creature{
    public static final int BREED_TIME = 20;

    public Fish(int x, int y) {
        super(x, y, BREED_TIME);
    }

    @Override
    public void move(World world) {
        ArrayList<Position> freePositions = getFreePositions(world);
        Position oldPosition = this.position;
        Position newPosition;

        if(!freePositions.isEmpty()){
            newPosition = freePositions.get((int)(Math.random() * freePositions.size()));
        }
        else
        {
            newPosition = this.position;
        }

        if(this.breedTime > 0)
        {
            this.breedTime--;
            world.world[oldPosition.x][oldPosition.y] = new Water(this.position.x, this.position.y);
            this.position = newPosition;
            world.world[this.position.x][this.position.y] = this;
        }
        else{
            this.breedTime = BREED_TIME;
            world.world[newPosition.x][newPosition.y] = new Fish(newPosition.x, newPosition.y);
        }
    }

    private ArrayList<Position> getFreePositions(World world){
        return getPositions.getFreePositions(world, this.position);
    }
}