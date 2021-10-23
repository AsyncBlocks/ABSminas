package br.asyncblocks.absminas.utilidade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import static br.asyncblocks.absminas.ABSminas.*;

public class PluginUtils{

    private static String consolePrefix = ChatColor.BLUE + "[ABSminas] ";

    public static void consoleMsg(String msg) {
        Bukkit.getConsoleSender().sendMessage(consolePrefix + msg);
    }

    public static void playerMsg(Player player, String msg) {
        player.sendMessage(msg);
    }

    public static void playerSound_NOTE_BASS(Player player){
        if(getServidorVersao().equals("v1_8_R3")
                || getServidorVersao().equals("v1_8_R2") || getServidorVersao().equals("v1_8_R1")) {
            player.playSound(player.getLocation(),Sound.valueOf("NOTE_BASS"),1,1);
        } else if (getServidorVersao().equals("v1_12_R1")) {
            player.playSound(player.getLocation(),Sound.valueOf("BLOCK_NOTE_BASS"),1,1);
        }
    }

    public static void playerSound_ERRO(Player player) {
        if(getServidorVersao().equals("v1_8_R3")
                || getServidorVersao().equals("v1_8_R2") || getServidorVersao().equals("v1_8_R1")) {
            player.playSound(player.getLocation(),Sound.valueOf("VILLAGER_NO"),1,1);
        } else if (getServidorVersao().equals("v1_12_R1")) {
            player.playSound(player.getLocation(),Sound.valueOf("ENTITY_VILLAGER_NO"),1,1);
        }
    }

}