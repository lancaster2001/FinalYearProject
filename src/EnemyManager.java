import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EnemyManager {
    //singleton------------------------------------------------------------------------
    private static EnemyManager instance = new EnemyManager();

    public static EnemyManager getInstance() {
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }

    private EnemyManager() {
        loadEnemyTemplates(enemyTemplatesPath);
    }

    //----------------------------------------------------------------------------------
    private final ArrayList<BaseEnemy> enemyList = new ArrayList<>();
    private final ArrayList<EnemyTemplate> enemyTemplates = new ArrayList<>();
    private final String enemyTemplatesPath = "src/Enemies/";
    private static int spawnerCounter = 0;
    private static boolean spawnerOn = false;
    private double countdown = 60;
    private double difficulty = 0;



    public void tick(double tickMultiplier) {
        ArrayList<Integer> enemiesToRemove = new ArrayList<Integer>();
        for (Integer index = 0; index < enemyList.size(); index++) {
            if (!enemyList.get(index).getAlive()) {
                enemiesToRemove.add(index);
            }
        }
        if (!enemiesToRemove.isEmpty()) {
            removeListOfIndexes(enemiesToRemove);
        }

        for (Integer index = 0; index < enemyList.size(); index++) {
            enemyList.get(index).tick(tickMultiplier);
        }
        countdown-=tickMultiplier;
        if(spawnerCounter>= difficulty-1) {
            spawnerCounter = 0;
            spawnerOn = false;
            difficulty += 5;
        }
        if(countdown<=0){
            countdown = 60;
            spawnerOn = true;
            spawner();
        }
    }

    public void drawEnemies(Graphics g) {
        for (int index = 0; index < enemyList.size(); index++) {
            if (enemyList.get(index).onScreenCheck()) {
                enemyList.get(index).draw(g);
            }
        }
        for (BaseEnemy enemy : enemyList) {
            if (enemy.onScreenCheck()) {
                enemy.draw(g);
            }
        }
    }

    private void randomEnemyChance() {
        Random rnd = new Random();
        if (rnd.nextInt(1, 10) == 11) {
            int xSpawn;
            int ySpawn;
            if (rnd.nextInt(1, 2) == 1) {
                xSpawn = rnd.nextInt(1, gameConstants.mapWidth);
                if (rnd.nextInt(1, 2) == 1) {
                    ySpawn = 0;
                } else {
                    ySpawn = gameConstants.mapHeight;
                }
            } else {
                ySpawn = rnd.nextInt(1, gameConstants.mapHeight);
                if (rnd.nextInt(1, 2) == 1) {
                    xSpawn = 0;
                } else {
                    xSpawn = gameConstants.mapWidth;
                }
            }
            createEnemy(xSpawn, ySpawn, "soldier");
        }
        if (rnd.nextInt(1, 100) == 5) {
            createEnemy(1, 1, "scout");
        }
        if (rnd.nextInt(1, 100) == 10) {
            createEnemy(1, 1, "soldier");
        }
    }

    private void removeListOfIndexes(ArrayList<Integer> indexesToRemove) {
        for (int toRemoveIndex = indexesToRemove.size() - 1; toRemoveIndex >= 0; toRemoveIndex--) {
            for (int index = enemyList.size() - 1; index >= 0; index--) {
                if (indexesToRemove.get(toRemoveIndex) == index) {
                    enemyList.remove(index);
                    break;
                }
            }
        }
    }

    private void createEnemy(double x, double y, String name) {
        for (EnemyTemplate template : enemyTemplates) {
            if (name.equalsIgnoreCase(template.getName())) {
                enemyList.add(new BaseEnemy(new Pose(x, y, 0), template));
            }
        }
    }

    private void loadEnemyTemplates(String enemyTemplatesPath) {
        // Replace "path/to/your/file.json" with the actual path to your JSON file
        String filePath = enemyTemplatesPath;

        String[] jsonsInFolder = AssetManager.getInstance().getJsonsInFolder(filePath);
        //get the towers of each type e.g. turret1, turret1, turret3, etc
        for (String currentEnemy : jsonsInFolder) {


            try (FileReader reader = new FileReader(filePath + currentEnemy)) {
                // Using JSONTokener to parse the JSON file
                JSONTokener tokener = new JSONTokener(reader);
                JSONObject jsonObject = new JSONObject(tokener);
                // Accessing values from the JSON object
                String name = currentEnemy.replace(".json","");
                double width = jsonObject.getDouble("Width");
                double height = jsonObject.getDouble("Height");
                double maxhealth = jsonObject.getDouble("MaxHealth");
                double damage = jsonObject.getDouble("Damage");
                double movespeed = jsonObject.getDouble("MoveSpeed");
                String imageLink = jsonObject.getString("ImageLink");
                double range = jsonObject.getDouble("Range");
                double cooldown = jsonObject.getDouble("Cooldown");
                JSONArray bullet = jsonObject.getJSONArray("Bullet");
                enemyTemplates.add(new EnemyTemplate(name, width, height, damage, maxhealth, movespeed, imageLink, range, cooldown, readBulletTemplate(bullet)));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
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
    /*public void timer(){
        countdown-=;
        if(spawnerCounter>= difficulty-1) {
            spawnerCounter = 0;
            spawnerOn = false;
            difficulty += 5;
        }
        if(countdown<=0){
            countdown = 60;
            spawnerOn = true;
            spawner();
        }

    }*/
    private void spawner() {

        Timer actTimer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                ++spawnerCounter;
                Random rnd = new Random();
                int chance = rnd.nextInt(1, 3);
                if (chance == 1) {
                    createEnemy(1, 1, "scout");
                }else{
                    createEnemy(1, 1, "soldier");
                }
                if(spawnerOn) {
                    spawner();
                }
            }
        };

        actTimer.schedule(task, 1000);

    }

    public ArrayList<BaseEnemy> getEnemyList() {
        return enemyList;
    }
}
