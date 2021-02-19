package me.hadzakee.dynamicshop.menus;

import me.hadzakee.dynamicshop.menu.*;
import me.hadzakee.dynamicshop.models.ShopItemsList;
import me.hadzakee.dynamicshop.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopEditMenu extends Menu {

    private int page;
    private PlayerMenuUtility pmu;

    public ShopEditMenu(AbstractPlayerMenuUtility pmu, int page) {
        super(pmu);
        this.pmu = (PlayerMenuUtility) pmu;
        this.page = page;
    }

    @Override
    public String getMenuName() {
        return "Admin Shop Page #" + page;
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public boolean cancelNullClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (!e.getView().getTitle().contains("Shop")) return;

        pmu.setSlot(e.getSlot());
        pmu.setPage(page);

        switch (e.getCurrentItem().getType()) {
            case RED_STAINED_GLASS_PANE:
                new ShopEditAddItem(MenuManager.getPlayerMenuUtility(pmu.getOwner())).open();
                break;
            case BARRIER:
                pmu.getOwner().closeInventory();
                break;
            case ACACIA_BUTTON:
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                if (name.contains("Forward")) {
                    new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), page+1).open();
                }else{
                    if (page>1) {
                        new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), page-1).open();
                    }else{
                        pmu.getOwner().sendMessage(MessageUtils.message("You are at the first page."));
                    }
                }
                break;
            default:
                pmu.setItem(e.getCurrentItem());
                new ShopEditManageItem(MenuManager.getPlayerMenuUtility(pmu.getOwner())).open();

        }
    }

    @Override
    public void setMenuItems() {
        pmu.getBorderSlots().forEach(slot -> inventory.setItem(slot, pmu.getFillerItem()));

        ItemStack forward = makeItem(Material.ACACIA_BUTTON, ColorTranslator.translateColorCodes("&#22f28a&lForward"));
        ItemStack backward = makeItem(Material.ACACIA_BUTTON, ColorTranslator.translateColorCodes("&#22f28a&lBackward"));
        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&#f0300e&lClose"));
        ItemStack empty = makeItem(Material.RED_STAINED_GLASS_PANE, ColorTranslator.translateColorCodes("&#28ed70&lEmpty"),
                ColorTranslator.translateColorCodes("&7&o(( Click to set item ))"));

        inventory.setItem(48, backward);
        inventory.setItem(50, forward);
        inventory.setItem(49, close);

        for (int i=0; i<54; ++i) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().isAir()) inventory.setItem(i, empty);
        }

        ShopItemsList.getItemsOnPage(page).forEach(item -> {
            ItemStack itemStack = new ItemStack(Material.matchMaterial(item.getMaterial()));
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            meta.setDisplayName(ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(itemStack.getType().name())));
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + item.getCurrentPrice()));
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lStock: &#2ca5f5" + item.getStock()));
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lBought: &#2ca5f5" + item.getPurchased()));
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯     &#2ff53c&lSold: &#2ca5f5" + item.getSold()));
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes("&7&o(( Click to manage item ))"));
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inventory.setItem(item.getSlot(), itemStack);
        });
    }
}