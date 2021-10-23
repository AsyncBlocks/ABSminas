package br.asyncblocks.absminas.controladores;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.Objects;

public class BlockSave{
    // ID;DATA;WORLD;X;Y;Z
    private final int id;
    private final byte data;
    private final Location location;

    public BlockSave(int id,byte data,Location location) {
        this.id = id;
        this.data = data;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public byte getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "BlockSave{"+id+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockSave blockSave = (BlockSave) o;
        return Objects.equals(location, blockSave.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }
}
