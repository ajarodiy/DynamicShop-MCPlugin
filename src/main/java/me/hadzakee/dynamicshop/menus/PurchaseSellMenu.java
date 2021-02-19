package me.hadzakee.dynamicshop.menus;

import me.hadzakee.dynamicshop.Database;
import me.hadzakee.dynamicshop.DynamicShop;
import me.hadzakee.dynamicshop.menu.*;
import me.hadzakee.dynamicshop.utils.MessageUtils;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PurchaseSellMenu extends Menu {

    private PlayerMenuUtility pmu;
    private Economy economy;

    public PurchaseSellMenu(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        this.pmu = (PlayerMenuUtility) pmu;
        this.economy = DynamicShop.getEconomy();
    }

    @Override
    public String getMenuName() {
        return "Purchase or Sell";
    }

    @Override
    public int getSlots() {
        return 27;
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
        if (!e.getClickedInventory().equals(e.getView().getTopInventory()) || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        if (e.getCurrentItem().getType() == Material.BARRIER) {
            new ShopPage(MenuManager.getPlayerMenuUtility(pmu.getOwner()), pmu.getPage()).open();
            return;
        }

        String lore = e.getCurrentItem().getItemMeta().getLore().get(0);
        int amount=0;

        if (lore.contains("one")) amount = 1;
        else if (lore.contains("ten")) amount = 10;
        else if (lore.contains("stack")) amount = 64;
        else return;

        if (lore.contains("Buy")) buy(amount, amount * pmu.getShopItem().getCurrentPrice());
        else sell(amount, amount * pmu.getShopItem().getCurrentPrice());
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        Material material = pmu.getItem().getType();

        ItemStack main = makeItem(material, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()),
                ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lStock: &#2ca5f5" + pmu.getShopItem().getStock()));


        ItemStack sellOne = makeItem(material, 1, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcSell one"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*1));
        ItemStack sellTen = makeItem(material, 10, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcSell ten"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*10));
        ItemStack sellStack = makeItem(material, 64, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcSell a stack"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*64));

        ItemStack buyOne = makeItem(material, 1, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcBuy one"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*1));
        ItemStack buyTen = makeItem(material, 10, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcBuy ten"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*10));
        ItemStack buyStack = makeItem(material, 64, ColorTranslator.translateColorCodes("&8&l" + pmu.parseItemName(material.name())),
                ColorTranslator.translateColorCodes("&#035efcBuy a stack"), ColorTranslator.translateColorCodes(" &#24e331➯ &#2ff53c&lPrice: &#2ca5f5$" + pmu.getShopItem().getCurrentPrice()*64));

        ItemStack back = makeItem(Material.BARRIER,  ColorTranslator.translateColorCodes("&#f0300e&lBack"));

        inventory.setItem(4, main);
        inventory.setItem(10, sellOne);
        inventory.setItem(11, sellTen);
        inventory.setItem(12, sellStack);
        inventory.setItem(14, buyOne);
        inventory.setItem(15, buyTen);
        inventory.setItem(16, buyStack);
        inventory.setItem(22, back);
    }

    public void sell(int amount, int price) throws MenuManagerNotSetupException, MenuManagerException {

        ItemStack item = new ItemStack(pmu.getItem().getType(), amount);

        if (hasEnoughItems(item.getType(), amount)) {

            EconomyResponse response = economy.depositPlayer(pmu.getOwner(), price);

            if (response.transactionSuccess()) {

                removeItems(item.getType(), amount);
                pmu.getShopItem().setSold(pmu.getShopItem().getSold() + amount);
                pmu.getOwner().sendMessage(MessageUtils.message("Successfully sold the items."));
                new PurchaseSellMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner())).open();
            }else{
                pmu.getOwner().sendMessage(MessageUtils.message("Transaction failed."));
            }

        }else{
            pmu.getOwner().sendMessage(MessageUtils.message("Not enough items in inventory."));
        }

    }

    public void buy(int amount, int price) throws MenuManagerNotSetupException, MenuManagerException {

        if (amount > (pmu.getShopItem().getSold() - pmu.getShopItem().getPurchased())) {
            pmu.getOwner().sendMessage(MessageUtils.message("Not enough items in stock."));
            return;
        }

        if (economy.getBalance(pmu.getOwner()) >= price) {
            ItemStack item = new ItemStack(pmu.getItem().getType(), amount);

            if (hasEnoughSpace(item.getType(), amount)) {

                EconomyResponse response = economy.withdrawPlayer(pmu.getOwner(), price);

                if (response.transactionSuccess()) {
                    pmu.getOwner().getInventory().addItem(item);
                    pmu.getShopItem().setPurchased(pmu.getShopItem().getPurchased() + amount);
                    pmu.getOwner().sendMessage(MessageUtils.message("Successfully bought the items."));
                    new PurchaseSellMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner())).open();
                }else{
                    pmu.getOwner().sendMessage(MessageUtils.message("Transaction failed."));
                }

            }else{
                pmu.getOwner().sendMessage(MessageUtils.message("Not enough space in inventory."));
            }

        }else{
            pmu.getOwner().sendMessage(MessageUtils.message("You don't have enough balance."));
        }

    }

    public boolean hasEnoughSpace(Material material, int amount) {
        int available = 0;
        for (ItemStack item : pmu.getOwner().getInventory().getStorageContents()) {
            if (item == null || item.getType() == Material.AIR) available += new ItemStack(material).getMaxStackSize();
            else if (item.getType() == material) {
                available += (item.getMaxStackSize() - item.getAmount());
            }
        }

        return available >= amount;
    }

    public boolean hasEnoughItems(Material material, int amount) {
        int available = 0;
        for (ItemStack item : pmu.getOwner().getInventory()) {
            if (item != null && item.getType() == material) {
                available += item.getAmount();
            }
        }
        return available >= amount;
    }

    public void removeItems(Material material, int amount) {
        for (ItemStack item : pmu.getOwner().getInventory()) {
            if (item != null && item.getType() == material) {
                if (item.getAmount() <= amount) {
                    amount -= item.getAmount();
                    item.setAmount(0);
                }else{
                    item.setAmount(item.getAmount()-amount);
                    return;
                }
            }
        }
    }
}
