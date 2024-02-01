public class TowerVariant1 extends BaseTower {
    private double shootAccumulator = 0.0;
    private double shootAccumulatorLimit = 0.0;
    private Tower1Properties values = new Tower1Properties();

    public TowerVariant1(){
        number = values.fixedNumber;
        imageLink = "src/Towers/Tower1/image.png";

    }
    public void tick(double tickMultiplier){
        shootAccumulator+=tickMultiplier;
        if (shootAccumulator>=shootAccumulatorLimit){
            shootAccumulator-=shootAccumulatorLimit;

        }
    }
    @Override
    public void shoot(){
        System.out.println("tower variant1 shot");
    }
}
