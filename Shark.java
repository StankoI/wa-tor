import java.util.ArrayList;

public class Shark extends Creature {

    private static final int ENERGY_FROM_EATEN_FISH = 8;
    private static final int ENERGY_NEEDED_TO_BREED = 20;
    private int energy;

    public Shark(int x, int y, int _energy) {
        super(x, y);
        energy = _energy;
    }

    @Override
    public void move(World world) {
        if(this.isMoved) return;

        Position oldPosition = this.position;
        Pair<ArrayList<Position>, ArrayList<Position>> freePositions = getFreePositions(world);
        Position newPosition; //water-first fish-second in pair

        //move logic , fish have a priority
        if (!freePositions.second.isEmpty()) {
            newPosition = freePositions.second.get((int) (Math.random() * freePositions.second.size()));
        } else if (!freePositions.first.isEmpty()) {
            newPosition = freePositions.first.get((int) (Math.random() * freePositions.first.size()));
        } else {
            newPosition = this.position;
        }

        if (world.world[newPosition.x][newPosition.y] instanceof Fish) {
            eatFish(newPosition, oldPosition, world);
        } else {
            this.position = newPosition;
            world.world[newPosition.x][newPosition.y] = this;
            world.world[oldPosition.x][oldPosition.y] = null;
        }

        this.energy--;

        if (this.energy <= 0) {
            leaveThisWorld(world);
            return;
        }

        if(this.energy >= ENERGY_NEEDED_TO_BREED) {
            this.position = newPosition;
            breed(world, oldPosition, newPosition);
        }

        this.isMoved = true;
    }

    private void leaveThisWorld(World world) {
        world.world[this.position.x][this.position.y] = null;
    }

    private void breed(World world, Position oldPosition, Position newPosition) {
        if(oldPosition != newPosition){
            int newSharkEnergy = this.energy / 2;
            world.world[oldPosition.x][oldPosition.y] = new Shark(oldPosition.x, oldPosition.y, newSharkEnergy);
            this.energy = this.energy - newSharkEnergy;
        }
    }

    private void eatFish(Position newPosition, Position oldPosition, World world) {
        this.energy += ENERGY_FROM_EATEN_FISH;
        this.position = newPosition;
        world.world[newPosition.x][newPosition.y] = this;
        world.world[oldPosition.x][oldPosition.y] = null;
    }

    public Pair<ArrayList<Position>, ArrayList<Position>> getFreePositions(World world) {
        return new Pair<>
                (getPositions.getFreePositions(world, this.position),
                        getPositions.getPositionsWithFish(world, this.position));
    }

}
