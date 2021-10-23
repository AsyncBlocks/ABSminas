package br.asyncblocks.absminas.comandos;

import br.asyncblocks.absminas.eventos.CriadorDaMina;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinaSelector implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(!player.hasPermission("absminas.selector")) {
            PluginUtils.playerMsg(player, ChatColor.RED+"Você não tem permissão!");
            return false;
        }

        player.getInventory().addItem(CriadorDaMina.seletor);
        PluginUtils.playerSound_NOTE_BASS(player);

        return true;
    }
}
