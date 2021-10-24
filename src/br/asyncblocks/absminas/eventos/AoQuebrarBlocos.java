package br.asyncblocks.absminas.eventos;

import br.asyncblocks.absminas.controladores.BlockSave;
import br.asyncblocks.absminas.controladores.MinasManager;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static br.asyncblocks.absminas.ABSminas.*;
public class AoQuebrarBlocos implements Listener {

    private static List<BlockSave> blockSaves = new ArrayList<>();

    @EventHandler
    public void aoQuebrar(BlockBreakEvent e) {
        Block blocoQuebrado = e.getBlock();
        if(getMinasManager().getMinas().stream().anyMatch(m -> m.getRegiao().contains(blocoQuebrado))) {
            int id = blocoQuebrado.getTypeId();
            byte data = blocoQuebrado.getData();
            Location location = blocoQuebrado.getLocation();
            boolean resultado = getMinasManager().getBlocos().stream()
                    .anyMatch(b -> b.getTypeId() == id
                            && b.getData().getData() == data);
            if(resultado) {
                e.setCancelled(true);
                ItemStack key = new ItemStack(id,data);
                String permissao = getMinasManager().getBlocos_e_permissoes().get(key);
                int cooldown = getMinasManager().getBlocos_e_cooldowns().get(key);

                if(!e.getPlayer().hasPermission(permissao)) {
                    PluginUtils.playerMsg(e.getPlayer(),"Voce nao pode pegar este minerio!");
                    return;
                }

                BlockSave blockSave = new BlockSave(id,data,location);
                blockSaves.add(blockSave);
                blocoQuebrado.setTypeIdAndData(7,(byte) 0,true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(),() -> {
                    blocoQuebrado.setTypeIdAndData(id,data,true);
                    blockSaves.remove(blockSave);
                },20*cooldown);
            }
        }
    }

    public static void salvarBlockSaves() {
        List<String> blockSaveString = new ArrayList<>();
        blockSaves.forEach(blockSaves -> { // ID;DATA;WORLD;X;Y;Z
            String saveString = blockSaves.getId()+
                    ";"
                    +blockSaves.getData()+
                    ";"
                    +blockSaves.getLocation().getWorld().getName()+
                    ";"
                    +blockSaves.getLocation().getX()+
                    ";"
                    +blockSaves.getLocation().getY()+
                    ";"
                    +blockSaves.getLocation().getZ()+
                    ";";
            blockSaveString.add(saveString);
        });
        getBlockSave().set("save",blockSaveString);
        salvarBlockSave();
    }

    public static void pegarBlockSavesList() {
        List<String> blockSaveString = getBlockSave().getStringList("save");
        List<BlockSave> blockSavesList = new ArrayList<>();
        blockSaveString.forEach(s -> {
            String[] strings = s.split(";");
            int id = Integer.parseInt(strings[0]);
            byte data = Byte.parseByte(strings[1]);
            World world = Bukkit.getWorld(strings[2]);
            double x = Double.parseDouble(strings[3]);
            double y = Double.parseDouble(strings[4]);
            double z = Double.parseDouble(strings[5]);
            Location location = new Location(world,x,y,z);
            BlockSave blockSave = new BlockSave(id,data,location);
            blockSavesList.add(blockSave);
        });
        blockSaves = blockSavesList;
    }

    public static void carregarBlockSaves() {
        getBlockSaves().forEach(blockSave -> {
            blockSave.getLocation()
                    .getWorld()
                    .getBlockAt(blockSave.getLocation())
                    .setTypeIdAndData(blockSave.getId(),blockSave.getData(),true);
        });
    }

    public static List<BlockSave> getBlockSaves() {
        return blockSaves;
    }
}
