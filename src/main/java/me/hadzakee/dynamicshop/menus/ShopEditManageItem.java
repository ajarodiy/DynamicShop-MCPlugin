package me.hadzakee.dynamicshop.menus;

import me.hadzakee.dynamicshop.DynamicShop;
import me.hadzakee.dynamicshop.menu.*;
import me.hadzakee.dynamicshop.models.ShopItemsList;
import me.hadzakee.dynamicshop.utils.MessageUtils;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShopEditManageItem extends Menu {

    private PlayerMenuUtility pmu;

    public ShopEditManageItem(AbstractPlayerMenuUtility pmu) {
        super(pmu);
        this.pmu = (PlayerMenuUtility) pmu;
    }

    @Override
    public String getMenuName() {
        return "Manage Item";
    }

    @Override
    public int getSlots() {
        return 9;
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
        if (!e.getView().getTitle().contains("Manage")) return;

        switch (e.getCurrentItem().getType()) {
            case BLUE_DYE:
                pmu.getOwner().closeInventory();
                startConversation();
                break;
            case RED_DYE:
                ShopItemsList.removeItem(pmu.getPage(), pmu.getSlot());
                pmu.getOwner().sendMessage(MessageUtils.message("Item has been successfully removed from the shop."));
                new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), pmu.getPage()).open();
                break;
            case BARRIER:
                new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), pmu.getPage()).open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(0, pmu.getItem());

        ItemStack change = makeItem(Material.BLUE_DYE,  ColorTranslator.translateColorCodes("&#4ced28&lChange Price"));
        ItemStack delete = makeItem(Material.RED_DYE, ColorTranslator.translateColorCodes("&#7244cf&lDelete Item"));
        ItemStack back = makeItem(Material.BARRIER,  ColorTranslator.translateColorCodes("&#f0300e&lBack"));

        inventory.setItem(6,change);
        inventory.setItem(7, delete);
        inventory.setItem(8, back);
    }

    public void startConversation() {
        Prompt enterAmount = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext context, String s) {
                try { Integer.parseInt(s); return true; }
                catch (NumberFormatException exception) {
                    if (s.equalsIgnoreCase("cancel")) return true;
                    pmu.getOwner().sendMessage(MessageUtils.message("Please enter a valid amount"));
                    return false;
                }
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    pmu.getOwner().sendMessage(MessageUtils.message("Price change has been cancelled"));
                }else {
                    ShopItemsList.changePrice(pmu.getPage(), pmu.getSlot(), Integer.parseInt(s));
                    pmu.getOwner().sendMessage(MessageUtils.message("Price has been changed successfully"));
                }
                try {
                    new ShopEditMenu(MenuManager.getPlayerMenuUtility(pmu.getOwner()), pmu.getPage()).open();
                } catch (MenuManagerNotSetupException | MenuManagerException e) {
                    e.printStackTrace();
                }

                return Prompt.END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return MessageUtils.message("Enter the new price (Type \"cancel\" to cancel)");
            }
        };


        new ConversationFactory(DynamicShop.getInstance())
                .withModality(false)
                .withTimeout(15)
                .withFirstPrompt(enterAmount)
                .buildConversation(pmu.getOwner())
                .begin();

    }
}
