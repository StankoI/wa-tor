import java.util.HashSet;

public class World {
    public static final int SHARK_ENERGY = 10;

    int width;
    int height;
    int sharkCount;
    int fishCount;
    WorldObject[][] world;
    public final Object[] rowLocks;

    World(int width, int height, int sharkCount, int fishCount){
        if(sharkCount < 0 || fishCount < 0) {
            throw new IllegalArgumentException("Shark and Fish count must be positive");
        }
        if(width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if(sharkCount + fishCount > width * height) {
            throw new IllegalArgumentException("Shark and Fish count must be less than the total number of cells");
        }

        this.width = width;
        this.height = height;
        this.sharkCount = sharkCount;
        this.fishCount = fishCount;
        this.world = new WorldObject[width][height];

        rowLocks = new Object[height];

        for (int i = 0; i < height; i++) {
            rowLocks[i] = new Object();
        }


        initWorld();
    }

    private void initWorld(){
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                world[x][y] = new Water(x, y);
            }
        }

        createPositions(this.fishCount).forEach(p -> world[p.x][p.y] = new Fish(p.x, p.y));
        createPositions(this.sharkCount).forEach(p -> world[p.x][p.y] = new Shark(p.x,p.y,10));
    }

    private HashSet<Position> createPositions(int n)
    {
        HashSet<Position> positions = new HashSet<Position>();
        while(positions.size() < n) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            if(world[x][y] instanceof Water){
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }

    public void updateWorld(){
        for(int i = 0; i < this.width; i++){
            for(int j = 0; j < this.height; j++){
                if(world[i][j] instanceof Creature ){
                    ((Creature)world[i][j]).move(this);
                }
            }
        }

        resetMovedFlags();
    }

    public void resetMovedFlags(){
        for(int i = 0; i < this.width; i++){
            for(int j = 0; j < this.height; j++){
                if(world[i][j] instanceof Creature){
                    ((Creature) world[i][j]).isMoved = false;
                }
            }
        }
    }
}