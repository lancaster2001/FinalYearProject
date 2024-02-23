import org.json.*;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


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
                String[] jsonsInFolder = getJsonsInFolder(towersFolder + currentTowerType);


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
            String holder = link.substring(0, link.lastIndexOf("/"));
            String type = holder.substring(holder.lastIndexOf("/") + 1);
            String name = link.substring(link.lastIndexOf("/") + 1, link.length() - 5);
            TowerTemplate template = loadTemplateFromJsonObject(jsonObject,type,name);
            return template;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TowerTemplate loadTemplateFromJsonObject(JSONObject jsonObject,String giventype, String givenname) {
        TowerTemplate template = new TowerTemplate();
        String type;
        String name;
        try {
            type = jsonObject.getString("Type");
            name = jsonObject.getString("Name");
        } catch (Exception e) {
            name = givenname;
            type = giventype;
        }
        String BuildMenuList;
        try {
            BuildMenuList = jsonObject.getString("BuildMenuList");
        } catch (Exception e) {
            BuildMenuList = type;
        }
        try {
            double speed;
            try {
                speed = jsonObject.getDouble("Speed");
            } catch (Exception e) {
                speed = 0;
            }
            String costResource = jsonObject.getString("CostResource");
            int costQuantity = jsonObject.getInt("CostQuantity");
            double width = jsonObject.getDouble("Width");
            double height = jsonObject.getDouble("Height");
            double maxhealth = jsonObject.getDouble("MaxHealth");
            String imageLink = jsonObject.getString("ImageLink");
            template = new TowerTemplate(name, width, height, costResource, costQuantity, imageLink, type, maxhealth, BuildMenuList, speed);
            if (type.equals("Turret")) {
                double range = jsonObject.getDouble("Range");
                int BulletCostQuantity = jsonObject.getInt("BulletCostQuantity");
                String BulletCostResource = jsonObject.getString("BulletCostResource");
                JSONArray bullet = jsonObject.getJSONArray("Bullet");
                template.setupTurret(range, readBulletTemplate(bullet), BulletCostResource, BulletCostQuantity);
            } else if (type.equals("Drill")) {
                String spinnerLink = jsonObject.getString("SpinnerLink");
                template.setupDrill(spinnerLink);
            }
            return template;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return template;
    }

    private BulletTemplate readBulletTemplate(JSONArray bullet) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        BulletTemplate template = new BulletTemplate();

        Iterator<Object> iterator = bullet.iterator();
        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            double movespeed = object.getDouble("MoveSpeed");
            double damage = object.getDouble("Damage");
            double width = object.getDouble("Width");
            double height = object.getDouble("Height");
            String imageLink = object.getString("ImageLink");
            template = new BulletTemplate(movespeed, damage, width, height, imageLink);

        }
        return template;
    }

    public ArrayList<TowerTemplate> getTowerArrayList() {
        return towerArrayList;
    }

    public TowerTemplate getTemplate(String name) {
        for (TowerTemplate template : towerArrayList) {
            if (template.getName().equalsIgnoreCase(name)) {
                return template;
            }
        }
        return new TowerTemplate();
    }
    public TowerTemplate getDeletionTower(){
        return getTemplate("deletion");
    }
    public String[] getJsonsInFolder(String theLink) {
        File dir = new File(theLink);
        String[] files = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).getName().endsWith(".json");
            }
        });
        return files;
    }
}
