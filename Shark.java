import java.util.ArrayList;

public class Shark extends Creature{

    public static final int ENERGY = 10;
    public static final int ENERGY_FROM_EATEN_FISH = 3;
    public static final int ENERGY_NEEDED_TO_BREED = 20;


    public Shark(int x, int y) {
        super(x, y, ENERGY);
    }

    @Override
    public void move(World world)
    {
        Position oldPosition = this.position;
        Pair<ArrayList<Position>,ArrayList<Position>> freePositions = getFreePositions(world);
        Position newPosition;

        if(!freePositions.second.isEmpty()){
            newPosition = freePositions.second.get((int)(Math.random() * freePositions.second.size()));
        }
        else if(!freePositions.first.isEmpty()) {
            newPosition = freePositions.first.get((int)(Math.random() * freePositions.first.size()));
        }
        else {
            newPosition = this.position;
        }

        if(world.world[newPosition.x][newPosition.y] instanceof Fish) {
            eatFish(newPosition, oldPosition, world);
        }
        else {
            world.world[newPosition.x][newPosition.y] = this;
            world.world[oldPosition.x][oldPosition.y] = new Water(oldPosition.x, oldPosition.y);
        }

    }

    private void eatFish(Position newPosition, Position oldPosition, World world){
        this.breedTime += ENERGY_FROM_EATEN_FISH;
        world.world[newPosition.x][newPosition.y] = this;
        world.world[oldPosition.x][oldPosition.y] = new Water(oldPosition.x, oldPosition.y);
    }

    private Pair<ArrayList<Position>,ArrayList<Position>> getFreePositions(World world){
        return new Pair<ArrayList<Position>,ArrayList<Position>>
                      (getPositions.getFreePositions(world, this.position),
                              getPositions.getPositionsWithFish(world, this.position));
    }
}