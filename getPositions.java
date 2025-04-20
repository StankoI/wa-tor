import java.util.ArrayList;

public class getPositions {
    public static ArrayList<Position> getFreePositions(World world, Position position){
        ArrayList<Position> resultEmpty = new ArrayList<>();
        int width = world.width;
        int height = world.height;

        if (world.world[(position.x + 1) % width][position.y] == null) {
            resultEmpty.add(new Position((position.x + 1) % width, position.y));
        }
        if (world.world[position.x][(position.y + 1) % height] == null) {
            resultEmpty.add(new Position(position.x, (position.y + 1) % height));
        }
        if (world.world[(position.x - 1 + width) % width][position.y] == null) {
            resultEmpty.add(new Position((position.x - 1 + width) % width, position.y));
        }
        if (world.world[position.x][(position.y - 1 + height) % height] == null) {
            resultEmpty.add(new Position(position.x, (position.y - 1 + height) % height));
        }
        return resultEmpty;
    }

    public static ArrayList<Position> getPositionsWithFish(World world, Position position){
        ArrayList<Position> resultFish = new ArrayList<>();
        int width = world.width;
        int height = world.height;

        if (world.world[(position.x + 1) % width][position.y] instanceof Fish) {
            resultFish.add(new Position((position.x + 1) % width, position.y));
        }
        if (world.world[position.x][(position.y + 1) % height] instanceof Fish) {
            resultFish.add(new Position(position.x, (position.y + 1) % height));
        }
        if (world.world[(position.x - 1 + width) % width][position.y] instanceof Fish) {
            resultFish.add(new Position((position.x - 1 + width) % width, position.y));
        }
        if (world.world[position.x][(position.y - 1 + height) % height] instanceof Fish) {
            resultFish.add(new Position(position.x, (position.y - 1 + height) % height));
        }
        return resultFish;
    }


}
