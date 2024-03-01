import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    private static final int MAX_CAPACITY = 4; // Maximum number of resources in a quadtree node
    private Rectangle2D boundary; // Boundary of the quadtree node
    private List<Resource> resources; // List of resources in the quadtree node
    private boolean divided; // Indicates whether the node has been divided into quadrants
    private Quadtree[] quadrants; // Quadrants of the quadtree node

    public Quadtree(Rectangle2D boundary) {
        this.boundary = boundary;
        this.resources = new ArrayList<>();
        this.divided = false;
        this.quadrants = new Quadtree[4];
    }

    // Subdivides the node into quadrants
    private void subdivide() {
        double x = boundary.getX();
        double y = boundary.getY();
        double width = boundary.getWidth() / 2;
        double height = boundary.getHeight() / 2;

        quadrants[0] = new Quadtree(new Rectangle2D.Double(x, y, width, height));
        quadrants[1] = new Quadtree(new Rectangle2D.Double(x + width, y, width, height));
        quadrants[2] = new Quadtree(new Rectangle2D.Double(x, y + height, width, height));
        quadrants[3] = new Quadtree(new Rectangle2D.Double(x + width, y + height, width, height));

        divided = true;
    }

    // Insert a resource into the quadtree
    public boolean insert(Resource resource) {
        if (!boundary.contains(resource.getHitBox())) {
            return false; // Resource is outside the boundary
        }

        if (resources.size() < MAX_CAPACITY) {
            resources.add(resource);
            return true;
        }

        if (!divided) {
            subdivide();
        }

        for (Quadtree quadrant : quadrants) {
            if (quadrant.insert(resource)) {
                return true;
            }
        }

        return false;
    }

    // Remove a resource from the quadtree
    public boolean remove(Resource resource) {
        if (!boundary.contains(resource.getHitBox())) {
            return false; // Resource is outside the boundary
        }

        if (resources.remove(resource)) {
            return true;
        }

        if (divided) {
            for (Quadtree quadrant : quadrants) {
                if (quadrant.remove(resource)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Clear the quadtree
    public void clear() {
        resources.clear();

        if (divided) {
            for (Quadtree quadrant : quadrants) {
                quadrant.clear();
            }
            divided = false;
        }
    }

    // Query for resources within a range
    public ArrayList<Resource> queryRange(Rectangle2D range) {
        ArrayList<Resource> found = new ArrayList<>();

        if (!boundary.intersects(range)) {
            return found; // Range does not intersect with boundary
        }

        for (Resource resource : resources) {
            if (range.intersects(resource.getHitBox())) {
                found.add(resource);
            }
        }

        if (divided) {
            for (Quadtree quadrant : quadrants) {
                found.addAll(quadrant.queryRange(range));
            }
        }

        return found;
    }
}