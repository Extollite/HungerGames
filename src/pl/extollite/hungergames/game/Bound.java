package pl.extollite.hungergames.game;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import lombok.Setter;
import lombok.ToString;
import pl.extollite.hungergames.HG;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Bounding box object for creating regions
 */

@ToString
@Setter
@SuppressWarnings({"WeakerAccess", "unused"})
public class Bound {

	private int x;
	private int y;
	private int z;
	private int x2;
	private int y2;
	private int z2;
	private String level;
	private List<Entity> entities;

	/** Create a new bounding box between 2 sets of coordinates
	 * @param level World this bound is in
	 * @param x x coord of 1st corner of bound
	 * @param y y coord of 1st corner of bound
	 * @param z z coord of 1st corner of bound
	 * @param x2 x coord of 2nd corner of bound
	 * @param y2 y coord of 2nd corner of bound
	 * @param z2 z coord of 2nd corner of bound
	 */
	public Bound(String level, int x, int y, int z, int x2, int y2, int z2) {
		this.level = level;
		this.x = Math.min(x,x2);
		this.y = Math.min(y, y2);
		this.z = Math.min(z, z2);
		this.x2 = Math.max(x,x2);
		this.y2 = Math.max(y, y2);
		this.z2 = Math.max(z, z2);
		this.entities = new ArrayList<>();
	}

    /** Create a new bounding box between 2 locations (must be in same world)
     * @param location Location 1
     * @param location2 Location 2
     */
    public Bound(Location location, Location location2) {
        this(Objects.requireNonNull(location.getLevel()).getId(), ((int) location.getX()), ((int) location.getY()),
                ((int) location.getZ()), ((int) location2.getX()), ((int) location2.getY()), ((int) location2.getZ()));
    }

	public Integer[] getRandomLocs() {
		Random r = new Random();
		return new Integer[] {r.nextInt(x2 - x + 1) + x, y2, r.nextInt(z2 - z + 1) + z};
	}

	/** Check if a location is within the region of this bound
	 * @param loc Location to check
	 * @return True if location is within this bound
	 */
	public boolean isInRegion(Location loc) {
		if (!Objects.requireNonNull(loc.getLevel()).getId().equals(level)) return false;
		int cx = loc.getBlock().getX();
		int cy = loc.getBlock().getY();
		int cz = loc.getBlock().getZ();
		return (cx >= x && cx <= x2) && (cy >= y && cy <= y2) && (cz >= z && cz <= z2);
	}

	/**
	 * Kill/Remove all entities in this bound
	 */
	public void removeEntities() {
		entities.forEach(Entity::close);
		entities.clear();
	}

	/** Add an entity to the entity list
	 * @param entity The entity to add
	 */
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	/** Get a list of all entities in this bound
	 * @return Entities in this bound
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}

	/** Get the world of this bound
	 * @return World of this bound
	 */
	public Level getLevel() {
		return HG.getInstance().getServer().getLevel(level);
	}

	/** Get the greater corner of this bound
	 * @return Location of greater corner
	 */
	public Location getGreaterCorner() {
		return Location.from(x, y, z, this.getLevel());
	}

	/** Get the lesser corner of this bound
	 * @return Location of lesser corner
	 */
	public Location getLesserCorner() {
		return Location.from(x2, y2, z2, this.getLevel());

	}

	/** Get the center location of this bound
	 * @return The center location
	 */
	public Location getCenter() {
		return Location.from((x+x2)/2F, (y+y2)/2F, (z+z2)/2F, this.getLevel());
	}

}
