public class MapSlot {
    int[] coordinates = {-1,-1};//{x,y}
    private BaseTower tower;
    private BaseTile tile;

    public MapSlot(int x,int y){
        coordinates[0] = x;
        coordinates[1] = y;
        tower = new TowerVariant1();
        tile = new GrassTile();
    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public BaseTile getTile() {return tile;}
    public BaseTower getTower() {return tower;}
}
