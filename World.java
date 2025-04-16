public class World {

    int width;
    int height;
    WorldObject[][] world;

    World(int width, int height){
        this.width = width;
        this.height = height;
        this.world = new WorldObject[width][height];

        initWorld();
    }

    public void initWorld(){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                world[x][y] = new Water(x, y);
            }
        }
    }
}