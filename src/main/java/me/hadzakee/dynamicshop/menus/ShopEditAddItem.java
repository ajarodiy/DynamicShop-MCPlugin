package me.hadzakee.dynamicshop.menus;

import me.hadzakee.dynamicshop.menu.*;
import me.hadzakee.dynamicshop.models.ShopItem;
import me.hadzakee.dynamicshop.models.ShopItemsList;
import me.hadzakee.dynamicshop.utils.ColorTranslator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopEditAddItem extends Menu {

    private PlayerMenuUtility pmu;

    public ShopEditAddItem(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        this.pmu = (PlayerMenuUtility) pmu;
    }

    @Override
    public String getMenuName() {
        return "Place item in the slot";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return false;
    }

    @Override
    public boolean cancelNullClicks() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        if (e.getCurrentItem() !=null && e.getCurrentItem().equals(pmu.getFillerItem())) {
            e.setCancelled(true);
            return;
        }

        if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
            new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), 1).open();
            return;
        }

        if (e.getCursor() != null && !e.getCursor().getType().isAir() && e.getSlot() == 13) {
            ShopItem item = new ShopItem();
            item.setSlot(pmu.getSlot());
            item.setPage(pmu.getPage());
            item.setMaterial(e.getCursor().getType().name());
            item.setSellPrice(0);
            item.setBuyPrice(0);
            item.setPurchased(0);
            item.setSold(0);
            ShopItemsList.getItems().add(item);
            new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), pmu.getPage()).open();
        }

    }

    @Override
    public void setMenuItems() {
        for (int i=0; i<inventory.getSize(); ++i) inventory.setItem(i, pmu.getFillerItem());
        inventory.setItem(13, new ItemStack(Material.AIR));
        ItemStack back = makeItem(Material.BARRIER,  ColorTranslator.translateColorCodes("&#f0300e&lBack"));
        inventory.setItem(26, back);

    }
}
