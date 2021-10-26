package br.asyncblocks.absminas;

import br.asyncblocks.absminas.comandos.CriarMina;
import br.asyncblocks.absminas.comandos.MinaSelector;
import br.asyncblocks.absminas.comandos.blocksaves;
import br.asyncblocks.absminas.configuracao.BlockSavesDatabase;
import br.asyncblocks.absminas.configuracao.MinasConfig;
import br.asyncblocks.absminas.controladores.MinasManager;
import br.asyncblocks.absminas.eventos.AoQuebrarBlocos;
import br.asyncblocks.absminas.eventos.CriadorDaMina;
import br.asyncblocks.absminas.utilidade.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static br.asyncblocks.absminas.utilidade.PluginUtils.*;

public class ABSminas extends JavaPlugin {

    private static ABSminas plugin;
    private static String servidorVersao;

    private static MinasConfig minasConfig;
    private static MinasManager minasManager;
    private static BlockSavesDatabase blockSavesDatabase;

    @Override
    public void onEnable() {

        if(!validarVersao()) {
           Bukkit.getServer().getPluginManager().disablePlugin(this);
           return;
        }

        plugin = this;
        carregarConfiguracao();
        minasManager = new MinasManager();
        registrarEventos();
        registrarComandos();

        AoQuebrarBlocos.pegarBlockSavesList();
        AoQuebrarBlocos.carregarBlockSaves();
        autoSaveBlocks();
    }

    @Override
    public void onDisable() {
        AoQuebrarBlocos.salvarBlockSaves();
    }

    private void registrarEventos() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new CriadorDaMina(),this);
        pluginManager.registerEvents(new AoQuebrarBlocos(),this);
    }

    private void registrarComandos() {
        getCommand("minaselector").setExecutor(new MinaSelector());
        getCommand("criarmina").setExecutor(new CriarMina());
        getCommand("blocksaves").setExecutor(new blocksaves());
    }

    private boolean validarVersao() {
        servidorVersao = "N/A";

        try {
            servidorVersao = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            consoleMsg(ChatColor.BLUE+"Versão "+servidorVersao+" foi detectada!");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        if(servidorVersao.equals("v1_8_R3")) {

        }else if(servidorVersao.equals("v1_12_R1")) {

        } else {
            consoleMsg(ChatColor.RED+"Versão não compativel.. desligando o plugin!");
            return false;
        }

        consoleMsg(ChatColor.GREEN+"Versão compativel! iniciando o Plugin..");
        return true;
    }

    private void carregarConfiguracao() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();

        minasConfig = new MinasConfig();
        blockSavesDatabase = new BlockSavesDatabase();
    }

    private void autoSaveBlocks() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,() -> {
            AoQuebrarBlocos.salvarBlockSaves();
        },0L,(20*60)*2);
    }

    public static FileConfiguration getMinasConfig() {
        return minasConfig.pegarConfig();
    }

    public static FileConfiguration getBlockSave() {
        return blockSavesDatabase.pegarConfig();
    }

    public static boolean salvarBlockSave() {
        return blockSavesDatabase.salvar();
    }

    public static boolean salvarMinasConfig() {
        return minasConfig.salvar();
    }

    public static MinasManager getMinasManager() {
        return minasManager;
    }

    public static ABSminas getPlugin() {
        return plugin;
    }

    public static String getServidorVersao() {
        return servidorVersao;
    }
}
