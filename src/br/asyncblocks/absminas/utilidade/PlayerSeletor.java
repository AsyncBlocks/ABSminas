package br.asyncblocks.absminas.utilidade;

import org.bukkit.Location;

public class PlayerSeletor {

    private Location pos1;
    private Location pos2;

    public PlayerSeletor() {

    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }
}
