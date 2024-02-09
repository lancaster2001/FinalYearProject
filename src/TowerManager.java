import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import  org.json.*;


public class TowerManager {
    //singleton-------------------------------------------------------------------------
    private static TowerManager instance = new TowerManager();

    private TowerManager() {
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
    private final String bulletsImagesLink = "src/Bullets/";

    private void loadTowers() {
        loadListOfTowers(towersLink);
    }

    private void loadListOfTowers(String towersFolder) {
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
                String[] jsonsInFolder = AssetManager.getInstance().getJsonsInFolder(towersFolder + currentTowerType);


                //get the towers of each type e.g. turret1, turret1, turret3, etc
                for (String currentTower : jsonsInFolder) {
                    towerArrayList.add(readTowerTemplate(towersFolder + currentTowerType + "/" + currentTower));
                }
            }
        }
    }

    private TowerTemplate readTowerTemplate(String link) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        String filePath = link;
        try (FileReader reader = new FileReader(filePath)) {
            // Using JSONTokener to parse the JSON file
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            TowerTemplate template = loadTemplateFromJsonObject(jsonObject);
            String holder = link.substring(0, link.lastIndexOf("/"));
            template.setType(holder.substring(holder.lastIndexOf("/") + 1));
            template.setName(link.substring(link.lastIndexOf("/") + 1, link.length() - 5));
            return template;
        } catch(IOException | JSONException e){
        e.printStackTrace();
        }
        return null;
    }
    public TowerTemplate loadTemplateFromJsonObject (JSONObject jsonObject){
        TowerTemplate template = new TowerTemplate();
        String type;
        String name;
        try {
            type = jsonObject.getString("Type");
            name = jsonObject.getString("Name");
        }catch (Exception e){
            name =  "";
            type = "";
        }
        String BuildMenuList;
        try {
            BuildMenuList = jsonObject.getString("BuildMenuList");
        } catch (Exception e) {
            BuildMenuList = type;
        }
        try {
            String costResource = jsonObject.getString("CostResource");
            int costQuantity = jsonObject.getInt("CostQuantity");
            double width = jsonObject.getDouble("Width");
            double height = jsonObject.getDouble("Height");
            double maxhealth = jsonObject.getDouble("MaxHealth");
            String imageLink = jsonObject.getString("ImageLink");
            template = new TowerTemplate(name, width, height, costResource, costQuantity, imageLink, type, maxhealth, BuildMenuList);
            if (type.equals("Turret")) {
                double range = jsonObject.getDouble("Range");
                double cooldown = jsonObject.getDouble("Cooldown");
                    JSONArray bullet = jsonObject.getJSONArray("Bullet");template.setupTurret(range, cooldown, readBulletTemplate(bullet));
                } else if (type.equals("Drill")) {
                    double speed = jsonObject.getDouble("Speed");
                    template.setupDrill(speed);
                } else if (type.equals("Conveyer")) {
                    double speed = jsonObject.getDouble("Speed");
                    template.setupConveyer(speed);
                }
                return template;
        } catch(JSONException e){
            e.printStackTrace();
        }
        return template;
    }
    private BulletTemplate readBulletTemplate(JSONArray bullet) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        BulletTemplate template = new BulletTemplate();

        Iterator<Object> iterator = bullet.iterator();
        while(iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            double movespeed = object.getDouble("MoveSpeed");
            double damage = object.getDouble("Damage");
            double width = object.getDouble("Width");
            double height = object.getDouble("Height");
            String imageLink = object.getString("ImageLink");
            template = new BulletTemplate(movespeed, damage ,width,height, imageLink);

        }
        return template;
    }
    public ArrayList<TowerTemplate> getTowerArrayList() {
        return towerArrayList;
    }
    public TowerTemplate getTemplate(String name){
        for(TowerTemplate template: towerArrayList){
            if (template.getName().equalsIgnoreCase(name)){
                return  template;
            }
        }
        return new TowerTemplate();
    }
}
