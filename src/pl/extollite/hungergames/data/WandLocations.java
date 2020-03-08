package pl.extollite.hungergames.data;

import cn.nukkit.level.Location;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class WandLocations {

    private Location loc1;

    private Location loc2;

    public WandLocations(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public boolean hasValidSelection() {
        if (loc1 == null || loc2 == null) return false;
        return true;
    }

    public String getInvalidLoc() {
        if (loc1 == null) return "Position 1";
        else return "Position 2";
    }

}
