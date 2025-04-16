import java.util.ArrayList;

public class getPositions {
    public static ArrayList<Position> getFreePositions(World world, Position position){
        ArrayList<Position> resultEmpty = new ArrayList<>();

        if(world.world[(position.x + 1) % world.height][position.y] instanceof Water) {
            resultEmpty.add(new Position((position.x + 1) % world.height, position.y));
        }
        if(world.world[position.x][(position.y + 1) % world.width] instanceof Water) {
            resultEmpty.add(new Position(position.x, (position.y + 1) % world.width));
        }
        if(world.world[(position.x - 1 + world.height) % world.height][position.y] instanceof Water) {
            resultEmpty.add(new Position((position.x - 1 + world.height) % world.height, position.y));
        }
        if(world.world[position.x][(position.y - 1 + world.width) % world.width] instanceof Water) {
            resultEmpty.add(new Position(position.x, (position.y - 1 + world.width) % world.width));
        }
        return resultEmpty;
    }

    public static ArrayList<Position> getPositionsWithFish(World world, Position position){
        ArrayList<Position> resultFish = new ArrayList<>();

        if(world.world[(position.x + 1) % world.height][position.y] instanceof Fish) {
            resultFish.add(new Position((position.x + 1) % world.height, position.y));
        }
        if(world.world[position.x][(position.y + 1) % world.width] instanceof Fish) {
            resultFish.add(new Position(position.x, (position.y + 1) % world.width));
        }
        if(world.world[(position.x - 1 + world.height) % world.height][position.y] instanceof Fish) {
            resultFish.add(new Position((position.x - 1 + world.height) % world.height, position.y));
        }
        if(world.world[position.x][(position.y - 1 + world.width) % world.width] instanceof Fish) {
            resultFish.add(new Position(position.x, (position.y - 1 + world.width) % world.width));
        }
        return resultFish;
    }
}
