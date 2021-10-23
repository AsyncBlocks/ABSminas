package br.asyncblocks.absminas.configuracao;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static br.asyncblocks.absminas.ABSminas.getPlugin;
import static br.asyncblocks.absminas.utilidade.PluginUtils.consoleMsg;

public class BlockSavesDatabase {

    private final File file;
    private final FileConfiguration fileConfiguration;

    public BlockSavesDatabase() {
        file = new File(getPlugin().getDataFolder(),"blocksave.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()) {
            criarArquivo();
        }

    }

    public boolean salvar(){
        try {
            fileConfiguration.save(this.file);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public FileConfiguration pegarConfig() {
        return this.fileConfiguration;
    }

    private void criarArquivo() {
        try {
            file.createNewFile();
            pegarConfig().createSection("save");
            salvar();
        } catch (IOException e) {
            consoleMsg(ChatColor.RED+"Nao foi possivel criar os arquivos da config! desligando plugin..");
            Bukkit.getServer().getPluginManager().disablePlugin(getPlugin());
            return;
        }
    }

}