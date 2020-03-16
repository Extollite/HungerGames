package pl.extollite.hungergames.hgutils;

public enum HGItemIds {
    WAND(1);

    private int id;

    HGItemIds(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
