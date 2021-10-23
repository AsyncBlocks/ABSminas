package br.asyncblocks.absminas.comandos;

import br.asyncblocks.absminas.eventos.AoQuebrarBlocos;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class blocksaves implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        player.sendMessage(AoQuebrarBlocos.getBlockSaves().toString());

        return false;
    }
}
