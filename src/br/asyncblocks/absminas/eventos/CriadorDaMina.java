package br.asyncblocks.absminas.eventos;

import br.asyncblocks.absminas.utilidade.ItemBuilder_1_8_R3;
import br.asyncblocks.absminas.utilidade.PlayerSeletor;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import static br.asyncblocks.absminas.ABSminas.*;

import java.util.Arrays;
import java.util.HashMap;

public class CriadorDaMina implements Listener {

    public static ItemStack seletor = new ItemBuilder_1_8_R3(Material.DIAMOND_AXE,1)
            .setNome(ChatColor.RED+" -=- Mina Selector -=-")
            .setLore(Arrays.asList("",ChatColor.BLACK+"@%@!#@@!@!!$*!!$!$!$!)$($)$!@)(@#)(#)$$",""))
            .build();
    private static HashMap<Player, PlayerSeletor> playersSeletores = new HashMap<>();

    @EventHandler
    public void aoInteragir(PlayerInteractEvent e) {
        if(e.getItem() != null && e.getItem().isSimilar(seletor)) {

            if(e.getClickedBlock() == null) return;
            if(e.getClickedBlock().getLocation() == null) return;
            if(!e.hasBlock()) return;

            Player player = e.getPlayer();
            if(!player.hasPermission("absminas.selector")) return;
            Location location = e.getClickedBlock().getLocation();
            int blockID = e.getClickedBlock().getTypeId();
            byte blockData = e.getClickedBlock().getData();

            if(!playersSeletores.containsKey(player)) {
                playersSeletores.put(player,new PlayerSeletor());
            }

            if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
                playersSeletores.get(player).setPos1(location);
                PluginUtils.playerMsg(player, ChatColor.GREEN+"pos1 foi marcado com sucesso!");
                PluginUtils.playerSound_NOTE_BASS(player);
                mudarBlocoParaReconhecimento(e.getClickedBlock(), Material.DIAMOND_ORE,blockID,blockData);
            } else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                playersSeletores.get(player).setPos2(location);
                PluginUtils.playerMsg(player, ChatColor.GREEN+"pos2 foi marcado com sucesso!");
                PluginUtils.playerSound_NOTE_BASS(player);
                mudarBlocoParaReconhecimento(e.getClickedBlock(),Material.COAL_ORE,blockID,blockData);
            }
            e.setCancelled(true);
        }
    }

    private void mudarBlocoParaReconhecimento(Block block,Material material,int id,byte data) {
        if(Material.DIAMOND_ORE != block.getType() && Material.COAL_ORE != block.getType()) {
            block.setType(material);
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                block.setTypeIdAndData(id, data, true);
            }, 11);
        }
    }

    public static HashMap<Player, PlayerSeletor> getPlayersSeletores() {
        return playersSeletores;
    }
}
