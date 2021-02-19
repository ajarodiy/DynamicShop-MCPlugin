package me.hadzakee.dynamicshop.menus;


import me.hadzakee.dynamicshop.menu.AbstractPlayerMenuUtility;
import me.hadzakee.dynamicshop.models.ShopItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerMenuUtility extends AbstractPlayerMenuUtility {
    public PlayerMenuUtility(Player p) {
        super(p);
    }

    private int slot;
    private int page;
    private ItemStack item;
    private ShopItem shopItem;

    public ShopItem getShopItem() {
        return shopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getFillerItem() {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta(); meta.setDisplayName(" "); filler.setItemMeta(meta);
        return filler;
    }

    public Set<Integer> getBorderSlots() {
        Set<Integer> slots = new HashSet<>();
        for (int i=0; i<9; ++i) slots.add(i);
        for (int i=1; i<=5; ++i) {
            slots.add(i*9); slots.add(i*9-1);
        }
        for (int i=45; i<54; ++i) slots.add(i);
        return slots;
    }

    public String parseItemName(String name) {
        List<String> words = Arrays.asList(name.split("_"));
        StringBuilder newName = new StringBuilder();
        words.forEach(word -> {
            newName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
        });
        return newName.toString();
    }
}
