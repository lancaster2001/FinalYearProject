import org.json.JSONObject;

import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Map {
    private final List<MapSlot> mapArray;
    private final String name;
    private final int mapWidth;
    private final int mapHeight;
    private final Random random = new Random();

    public Map(List<MapSlot> mapArray, int width, int height) {
        this(mapArray, width, height, String.valueOf(new Random().nextLong()));
    }

    public Map(List<MapSlot> mapArray, int width, int height, String name) {
        this.mapArray = Objects.requireNonNull(mapArray);
        this.name = Objects.requireNonNull(name);
        this.mapWidth = width;
        this.mapHeight = height;
    }

    private MapSlot getSlotFromCoord(int x, int y) {
        return mapArray.stream()
                .filter(slot -> slot.getX() == x && slot.getY() == y)
                .findFirst()
                .orElse(null);
    }

    public ArrayList<MapSlot> getMapSection(Rectangle2D.Double section) {
        ArrayList<MapSlot> mapSection = new ArrayList<>();
        mapArray.forEach(currentSlot -> {
            if (section.intersects(currentSlot.getX(), currentSlot.getY(), 1.0, 1.0)) {
                mapSection.add(currentSlot);
            }
        });
        return mapSection;
    }

    public boolean intersects(Rectangle2D section) {
        return section.intersects(1, 1, mapWidth, mapHeight);
    }

    public Rectangle2D.Double sectionOutOfBoundsCheck(Rectangle2D.Double section, boolean canClipOutOfBound) {
        if (section.intersects(1, 1, mapWidth, mapHeight) && canClipOutOfBound) {
            return section;
        }
        section.x = Math.max(1, Math.min(section.x, mapWidth - section.width + 1));
        section.y = Math.max(1, Math.min(section.y, mapHeight - section.height + 1));
        return section;
    }

    public void tick(double tickMultiplier) {
        mapArray.forEach(slot -> slot.tick(tickMultiplier));
    }

    public String save() {
        String saveLink = "src/Saves/Maps/";
        try {
            Files.createDirectories(Paths.get(saveLink));
            Path fileName = Paths.get(saveLink, name + ".json");
            JSONObject json = new JSONObject();
            json.put("Name", name);
            json.put("Width", mapWidth);
            json.put("Height", mapHeight);
            mapArray.forEach(currentSlot ->
                    json.put("" + (((currentSlot.getY() - 1) * mapWidth) + (currentSlot.getX() - 1)), currentSlot.getJsonObject()));
            try (FileWriter file = new FileWriter(fileName.toFile())) {
                file.write(json.toString());
            }
            return fileName.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while saving map: " + e.getMessage(), e);
        }
    }

    public void clearTower(int x, int y) {
        MapSlot holder = getSlotFromCoord(x, y);
        if (holder != null) {
            if (holder.getTower() instanceof BaseBaseTower) {
                StateManager.getInstance().setCurrentState(GameSettings.STATE.GAMEOVER);
                return;
            }
            holder.clearTower();
        }
    }
    public MapSlot getMapslot(int x, int y) {
        int index = ((y - 1) * mapWidth) + (x) - 1;
        return mapArray.get(index);
    }

    public void setTower(TowerTemplate newTower, Pose pose) {
        setTower(newTower, pose, true);
    }

    public void setTower(TowerTemplate newTower, Pose pose, boolean charge) {
        if (newTower != null && pose != null) {
            Objects.requireNonNull(getSlotFromCoord((int) pose.getX(), (int) pose.getY())).setTower(newTower, pose.getTheta(), charge);
        }
    }
    public void setTower(BaseTower newTower, int x, int y) {
        setTower(newTower, x, y, true);
    }
    public void setTower(BaseTower newTower, int x, int y, boolean charge) {
        Objects.requireNonNull(getSlotFromCoord(x, y)).setTower(newTower, charge);
    }

    public void setTempTower(TowerTemplate newTower, Pose pose) {
        if (newTower != null && pose != null) {
            Objects.requireNonNull(getSlotFromCoord((int) pose.getX(), (int) pose.getY())).setTempTower(newTower, pose.getTheta());
        }
    }
    public void setTowerFromTempTower(int x, int y) {
        Objects.requireNonNull(getSlotFromCoord(x, y)).setTowerFromTempTower();
    }
    public void clearTempTower(int x, int y) {
        Objects.requireNonNull(getSlotFromCoord(x, y)).clearTempTower();
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public Pose getBasePose() {
        for (MapSlot currentSlot : mapArray) {
            if (currentSlot.getTower() instanceof BaseBaseTower) {
                return currentSlot.getTower().getPose();
            }
        }
        return null;
    }
}