import java.awt.*;

public class TowerVariant1 extends BaseTurretTower {
    private Tower1Properties values = new Tower1Properties();

    public TowerVariant1(){
        imageLink = "src/Towers/Tower1/image.png";
        range = 10;
    }


    @Override
    public void shoot(){
        System.out.println("tower variant1 shot");
    }
}
