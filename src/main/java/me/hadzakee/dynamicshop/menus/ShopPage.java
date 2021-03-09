package me.hadzakee.dynamicshop.menus;

import me.hadzakee.dynamicshop.menu.*;
import me.hadzakee.dynamicshop.models.ShopItemsList;
import me.hadzakee.dynamicshop.utils.MessageUtils;
import me.hadzakee.dynamicshop.utils.ColorTranslator;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopPage extends Menu {

    private int page;
    private PlayerMenuUtility pmu;

    public ShopPage(AbstractPlayerMenuUtility pmu, int page) {
        super(pmu);
        this.pmu = (PlayerMenuUtility) pmu;
        this.page = page;
        this.pmu.setPage(page);
    }

    @Override
    public String getMenuName() {
        return "Shop Page #" + page;
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
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) return;

        switch(e.getCurrentItem().getType()) {
            case BARRIER:
                pmu.getOwner().closeInventory();
                break;
            case ACACIA_BUTTON:
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                if (name.contains("Forward")) {
                    if (!ShopItemsList.isPageEmpty(page+1)) {
                        new ShopPage(MenuManager.getPlayerMenuUtility(pmu.getOwner()), page+1).open();
                    }else{
                        pmu.getOwner().sendMessage(MessageUtils.message("You are at the last page."));
                    }
                }else{
                    if (page>1) {
                        new ShopPage(MenuManager.getPlayerMenuUtility(pmu.getOwner()), page-1).open();
                    }else{
                        pmu.getOwner().sendMessage(MessageUtils.message("You are at the first page."));
                    }
                }
                break;
            default:
                pmu.setShopItem(ShopItemsList.getItem(page, e.getSlot()));
                pmu.setItem(e.getCurrentItem());
                new PurchaseSellMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner())).open();
        }

    }

    @Override
    public void setMenuItems() {
        pmu.getBorderSlots().forEach(slot -> inventory.setItem(slot, pmu.getFillerItem()));

        ItemStack forward = makeItem(Material.ACACIA_BUTTON, ColorTranslator.translateColorCodes("&#22f28a&lForward"));
        ItemStack backward = makeItem(Material.ACACIA_BUTTON, ColorTranslator.translateColorCodes("&#22f28a&lBackward"));
        ItemStack close = makeItem(Material.BARRIER, ColorTranslator.translateColorCodes("&#f0300e&lClose"));

        inventory.setItem(48, backward);
        inventory.setItem(50, forward);
        inventory.setItem(49, close);

        ShopItemsList.getItemsOnPage(page).forEach(item -> {
            if (item.getMaterial().equalsIgnoreCase("AIR")) return;
            ItemStack itemStack = new ItemStack(Material.matchMaterial(item.getMaterial()));
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(itemStack.getType().name())));
            List<String> lore = new ArrayList<>();
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lSell Price: &#2ca5f5$" + item.getCurrentSellPrice()));
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lBuy  Price: &#2ca5f5$" + item.getCurrentBuyPrice()));
            lore.add("");
            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lStock: &#2ca5f5" + item.getStock()));
            lore.add("");
//            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lBought: &#2ca5f5" + item.getPurchased()));
//            lore.add(ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lSold  : &#2ca5f5" + item.getSold()));
//            lore.add("");
            lore.add(ColorTranslator.translateColorCodes("&7&o(( Click to buy or sell ))"));
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inventory.setItem(item.getSlot(), itemStack);
        });
    }
}
