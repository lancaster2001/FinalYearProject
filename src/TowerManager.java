import java.io.*;
import java.util.ArrayList;

import  org.json.*;


public class TowerManager {
    //singleton-------------------------------------------------------------------------
    private static TowerManager instance = new TowerManager();
    private TowerManager(){
        loadTowers();
    }
    public static TowerManager getInstance() {
        if (instance == null) {
            instance = new TowerManager();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------
    private final ArrayList<TowerTemplate> towerArrayList = new ArrayList<TowerTemplate>();
    private final String towersLink = "src/Towers/";
    private final String bulletsImagesLink ="src/Bullets/";
    private void loadTowers() {
        loadListOfTowers(towersLink);
    }

    private void loadListOfTowers(String towersFolder){
        File towerTypesFolders = new File(towersFolder);
        String[] directories = towerTypesFolders.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        //get the directory of each tower type e.g. turret, driller, etc
        if (directories != null) {
            for (String currentTowerType : directories) {

                File towerFolders = new File(towersFolder+"/"+currentTowerType);
                String[] files = towerFolders.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        return new File(current, name).isDirectory();
                    }
                });
                //get the towers of each type e.g. turret1, turret1, turret3, etc
                for (String currentTower : files) {
                    towerArrayList.add(readTowerTemplate(towersFolder+"/"+currentTowerType+"/"+currentTower));
                }
            }
        }
    }

    private TowerTemplate readTowerTemplate(String link) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        String filePath = link+ "/tower.json";
        TowerTemplate template = new TowerTemplate();

        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Accessing values from the JSON object
            String type = jsonObject.getString("Type");
            String name = jsonObject.getString("Name");
            String costResource = jsonObject.getString("CostResource");
            int costQuantity = jsonObject.getInt("CostQuantity");
            double width = jsonObject.getDouble("Width");
            double height = jsonObject.getDouble("Height");
            template = new TowerTemplate(name, width,height, costResource, costQuantity, link+"/image.png",type);
            if (type.equals("Turret")){
                double range = jsonObject.getDouble("Range");
                double cooldown = jsonObject.getDouble("Cooldown");
                template.setupTurret(range,cooldown,readBulletTemplate(link+"/bullet.json"));
            } else if (type.equals("Drill")){

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return template;
    }

    private BulletTemplate readBulletTemplate(String link) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        String filePath = link;
        BulletTemplate template = new BulletTemplate();

        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            // Accessing values from the JSON object
            double movespeed = jsonObject.getDouble("MoveSpeed");
            double damage = jsonObject.getDouble("Damage");
            double width = jsonObject.getDouble("Width");
            double height = jsonObject.getDouble("Height");
            String imageLink = bulletsImagesLink+jsonObject.getString("ImageLink");
            template = new BulletTemplate(movespeed, damage ,width,height, imageLink);

            return template;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return template;
    }

    public ArrayList<TowerTemplate> getTowerArrayList() {
        return towerArrayList;
    }
}
