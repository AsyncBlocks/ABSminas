package br.asyncblocks.absminas.utilidade;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder_1_8_R3 {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder_1_8_R3(Material material, int quantidade) {
        itemStack = new ItemStack(material,quantidade);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder_1_8_R3(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder_1_8_R3 setNome(String nome) {
        itemMeta.setDisplayName(nome);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder_1_8_R3 setLore(List<String> lore) {
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

}
