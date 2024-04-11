import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public final class EnemyManager {
    //singleton------------------------------------------------------------------------
    private static EnemyManager instance = new EnemyManager();

    public static EnemyManager getInstance() {
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
    private double waveCountdown = 10;
    private double countdown = waveCountdown;
    private double difficulty = 0;



    public void tick(double tickMultiplier) {
        ArrayList<Integer> enemiesToRemove = new ArrayList<Integer>();
        for (Integer index = 0; index < enemyList.size(); index++) {
            if (!enemyList.get(index).getAlive()) {
                enemiesToRemove.add(index);
            }
            enemyList.get(index).tick(tickMultiplier);
        }
        if (!enemiesToRemove.isEmpty()) {
            //todo: remove this method and replace with remove collection
            removeListOfIndexes(enemiesToRemove);
        }
        countdown-=tickMultiplier;
        if(spawnerCounter>= difficulty-1) {
            spawnerCounter = 0;
            spawnerOn = false;
            difficulty += 5;
        }
        if(countdown<=0){
            countdown = waveCountdown;
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
    }

    private void randomEnemySpawnChance() {
        int mapWidth = GameState.getInstance().getMapInstance().getMapWidth();
        int mapHeight = GameState.getInstance().getMapInstance().getMapHeight();
        Random rnd = new Random();
        if (rnd.nextInt(1, 10) == 11) {
            int xSpawn;
            int ySpawn;
            if (rnd.nextInt(1, 2) == 1) {
                xSpawn = rnd.nextInt(1, mapWidth);
                if (rnd.nextInt(1, 2) == 1) {
                    ySpawn = 0;
                } else {
                    ySpawn = mapHeight;
                }
            } else {
                ySpawn = rnd.nextInt(1, mapHeight);
                if (rnd.nextInt(1, 2) == 1) {
                    xSpawn = 0;
                } else {
                    xSpawn = mapWidth;
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
        //todo:make this methods return an array of templates instead of loading them directly into the public array
        String filePath = enemyTemplatesPath;
        //get list of jsons in the folder
        String[] jsonsInFolder = getJsonsInFolder(filePath);
        //load the template into template class for each json
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
                double movespeed = jsonObject.getDouble("MoveSpeed");
                String imageLink = jsonObject.getString("ImageLink");
                double range = jsonObject.getDouble("Range");
                double cooldown = jsonObject.getDouble("Cooldown");
                JSONArray bullet = jsonObject.getJSONArray("Bullet");
                enemyTemplates.add(new EnemyTemplate(name, width, height, maxhealth, movespeed, imageLink, range, cooldown, readBulletTemplate(bullet)));
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
    //returns a list of all the directory paths for jsons in a provided directory
    public String[] getJsonsInFolder(String theLink) {
        File dir = new File(theLink);
        String[] files = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return new File(dir, name).getName().endsWith(".json");
            }
        });
        return files;
    }
    public ArrayList<BaseEnemy> getEnemyList() {
        return enemyList;
    }
}
