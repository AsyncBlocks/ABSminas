package br.asyncblocks.absminas.eventos;

import br.asyncblocks.absminas.controladores.BlockSave;
import br.asyncblocks.absminas.controladores.MinasManager;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static br.asyncblocks.absminas.ABSminas.*;
public class AoQuebrarBlocos implements Listener {

    private static List<BlockSave> blockSaves = new ArrayList<>();
    private static HashMap<Player,Long> msgCooldowns = new HashMap<>();

    @EventHandler
    public void aoQuebrar(BlockBreakEvent e) {
        Block blocoQuebrado = e.getBlock();
        if(verificarRegiao(blocoQuebrado)) {
            int id = blocoQuebrado.getTypeId();
            byte data = blocoQuebrado.getData();
            Location location = blocoQuebrado.getLocation();

            boolean resultado = verificarBlocoValido(id,data);

            if(resultado) {
                e.setCancelled(true);
                Player player = e.getPlayer();
                ItemStack itemStack = new ItemStack(id,data);
                String permissao = coletarPermissao(itemStack);
                int cooldown = coletarCooldown(itemStack);

                if(!verificarSePodeColetarBloco(permissao,player)) return;

                BlockSave blockSave = new BlockSave(id,data,location);
                blockSaves.add(blockSave);
                blocoQuebrado.setTypeIdAndData(7,(byte) 0,true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(),() -> {
                    blocoQuebrado.setTypeIdAndData(id,data,true);
                    blockSaves.remove(blockSave);
                },20*cooldown);
                coletarBloco(player,itemStack);
            }
        }
    }

    private boolean verificarRegiao(Block block) {
        return getMinasManager().getMinas().stream().anyMatch(m -> m.getRegiao().contains(block));
    }

    private boolean verificarBlocoValido(int id,byte data) {
        return getMinasManager().getBlocos().stream()
                .anyMatch(b -> b.getTypeId() == id
                        && b.getData().getData() == data);
    }

    private String coletarPermissao(ItemStack key) {
        return getMinasManager().getBlocos_e_permissoes().get(key);
    }

    private int coletarCooldown(ItemStack key) {
        return getMinasManager().getBlocos_e_cooldowns().get(key);
    }

    private boolean verificarSePodeColetarBloco(String permissao,Player player) {
        if(!player.hasPermission(permissao)) {
            if(msgCooldowns.containsKey(player) && System.currentTimeMillis() < msgCooldowns.get(player))
                return false;
            else msgCooldowns.remove(player);
            PluginUtils.playerMsg(player, "Voce nao pode pegar este minerio!");
            PluginUtils.playerSound_ERRO(player);
            msgCooldowns.put(player,System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2));
            return false;
        } else return true;
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
        blockSaves = new ArrayList<>();
        getBlockSave().set("save", new ArrayList<String>());
        salvarBlockSave();
    }

    private void coletarBloco(Player player,ItemStack itemStack) {
        if(player.getInventory().firstEmpty() != -1) {
            int playerFortune = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            int quantidadeDrop = playerFortune == 0 ? 3 : (playerFortune * 3);
            itemStack.setAmount(quantidadeDrop);
            player.getInventory().addItem(itemStack);
        } else {
            if(msgCooldowns.containsKey(player) && System.currentTimeMillis() < msgCooldowns.get(player)) {
                return;
            } else msgCooldowns.remove(player);
            PluginUtils.playerMsg(player, "Seu Inventario esta Cheio!");
            PluginUtils.playerSound_NOTE_BASS(player);
            msgCooldowns.put(player,System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2));
        }
    }

    public static List<BlockSave> getBlockSaves() {
        return blockSaves;
    }
}
