public class MapSlot {
    int[] coordinates = {-1,-1};//{x,y}
    private BaseTower tower = new TowerVariant1();
    private BaseTile tile = new GrassTile();

    public MapSlot(int x,int y){
        coordinates[0] = x;
        coordinates[1] = y;

    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public BaseTile getTile() {return tile;}
    public BaseTower getTower() {return tower;}
}
