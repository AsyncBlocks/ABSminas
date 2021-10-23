package br.asyncblocks.absminas.comandos;

import br.asyncblocks.absminas.controladores.MinasManager;
import br.asyncblocks.absminas.erros.MinaJaExisteException;
import br.asyncblocks.absminas.eventos.CriadorDaMina;
import br.asyncblocks.absminas.utilidade.PlayerSeletor;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static br.asyncblocks.absminas.ABSminas.*;

public class CriarMina implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(!player.hasPermission("absminas.criarmina")) {
            PluginUtils.playerMsg(player, ChatColor.RED+"Você não tem permissão!");
            PluginUtils.playerSound_ERRO(player);
            return false;
        }

        if(!CriadorDaMina.getPlayersSeletores().containsKey(player)) {
            PluginUtils.playerMsg(player,ChatColor.RED+"Voce precisa selecionar o pos1 e o pos2 da mina!");
            PluginUtils.playerSound_ERRO(player);
            return false;
        }

        PlayerSeletor playerSeletor = CriadorDaMina.getPlayersSeletores().get(player);
        Location pos1 = playerSeletor.getPos1();
        Location pos2 = playerSeletor.getPos2();

        try {

            if (getMinasManager().carregarMina(strings[0], pos1, pos2)) {
                PluginUtils.playerMsg(player, ChatColor.GREEN + "A mina " + strings[0] + " foi criada!");
                PluginUtils.playerSound_NOTE_BASS(player);
                return true;
            } else {
                PluginUtils.playerMsg(player, ChatColor.GREEN + "A mina " + strings[0] + " nao foi criada! algo de errado ocorreu!");
                PluginUtils.playerSound_ERRO(player);
                return false;
            }

        } catch (MinaJaExisteException e) {
            PluginUtils.playerMsg(player,ChatColor.RED+" esta nome ja esta registrado!");
            return false;
        }

    }
}
