public class MapSlot {
    int[] coordinates = {-1,-1};//{x,y}
    BaseTower tower1 = new TowerVariant1();

    public MapSlot(int x,int y){
        coordinates[0] = x;
        coordinates[1] = y;

    }

    public int[] getCoordinates() {
        return coordinates;
    }
}
