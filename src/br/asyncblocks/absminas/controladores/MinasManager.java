package br.asyncblocks.absminas.controladores;

import br.asyncblocks.absminas.configuracao.MinasConfig;
import br.asyncblocks.absminas.erros.MinaJaExisteException;
import br.asyncblocks.absminas.utilidade.Cuboid;
import br.asyncblocks.absminas.utilidade.Mina;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static br.asyncblocks.absminas.ABSminas.*;

public class MinasManager {

    private List<Mina> minas;
    private HashMap<ItemStack,String> blocos_e_permissoes;
    private HashMap<ItemStack,Integer> blocos_e_cooldowns;
    private List<ItemStack> blocos;

    public MinasManager() {
        blocos_e_permissoes = new HashMap<>();
        blocos_e_cooldowns = new HashMap<>();
        blocos = new ArrayList<>();
        minas = new ArrayList<>();
        carregarBlocosPermitidos();
        carregarMinas();
    }

    public boolean carregarMina(String nome, Location pos1, Location pos2) throws MinaJaExisteException{
        if(minas.stream().anyMatch(m -> m.getNome().equals(nome)))
            throw new MinaJaExisteException();

        Cuboid regiao = new Cuboid(pos1,pos2);
        Mina mina = new Mina(nome,regiao);
        salvarMina(new Location[]{pos1, pos2},nome);
        return minas.add(mina);
    }

    private void salvarMina(Location[] pos,String nome) {
        Location pos1=pos[0],pos2=pos[1];
        String pos1String = pos1.getWorld().getName()+";"+pos1.getX()+";"+pos1.getY()+";"+pos1.getZ();
        String pos2String = pos2.getWorld().getName()+";"+pos2.getX()+";"+pos2.getY()+";"+pos2.getZ();
        getMinasConfig().set("minas."+nome+".pos1",pos1String);
        getMinasConfig().set("minas."+nome+".pos2",pos2String);
        salvarMinasConfig();
    }

    private void carregarMinas() {
        if(getMinasConfig().getConfigurationSection("minas") == null) return;
        final ConfigurationSection configurationSection = getMinasConfig().getConfigurationSection("minas");
        configurationSection.getKeys(false).stream().forEach(s -> {
            Location[] pos = new Location[2];
            for(int i=0;i < 2;i++) {
                int b = i+1;
                String[] split = getMinasConfig().getString("minas."+s+".pos"+b).split(";");
                World world = Bukkit.getWorld(split[0]);
                double x,y,z;
                x = Double.parseDouble(split[1]);
                y = Double.parseDouble(split[2]);
                z = Double.parseDouble(split[3]);
                pos[i] = new Location(world,x,y,z);
            }
            Cuboid region = new Cuboid(pos[0],pos[1]);
            Mina mina = new Mina(s,region);
            minas.add(mina);
        });
    }

    private void carregarBlocosPermitidos() {
        List<String> dataStrings = getPlugin().getConfig().getStringList("configuracao-sistema.blocos-permitidos");
        dataStrings.stream().forEach(dataString -> {
            String[] sDataString = dataString.split(";");
            int id = Integer.parseInt(sDataString[0]);
            byte data = Byte.parseByte(sDataString[1]);
            String permissao = sDataString[2];
            int cooldown = Integer.parseInt(sDataString[3]);

            ItemStack itemStack = new ItemStack(id,data);
            blocos_e_permissoes.put(itemStack,permissao);
            blocos_e_cooldowns.put(itemStack,cooldown);
            blocos.add(itemStack);

        });
    }

    public HashMap<ItemStack, Integer> getBlocos_e_cooldowns() {
        return blocos_e_cooldowns;
    }

    public HashMap<ItemStack, String> getBlocos_e_permissoes() {
        return blocos_e_permissoes;
    }

    public List<ItemStack> getBlocos() {
        return blocos;
    }

    public List<Mina> getMinas() {
        return minas;
    }
}
