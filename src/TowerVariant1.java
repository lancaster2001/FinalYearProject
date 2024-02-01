public class TowerVariant1 extends BaseTower {
    private Tower1Properties values = new Tower1Properties();

    public TowerVariant1(){
        number = values.fixedNumber;
        imageLink ="src/Towers/Tower1/image.png";

    }
    @Override
    public void shoot(){
        System.out.println("tower variant1 shot");
    }
}
