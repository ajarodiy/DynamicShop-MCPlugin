package me.hadzakee.dynamicshop.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {
    public MenuListener() {
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu)holder;

            if (menu.cancelNullClicks() && e.getCurrentItem() == null) {
                return;
            }

            if (menu.cancelAllClicks()) {
                e.setCancelled(true);
            }

            try {
                menu.handleMenu(e);
            } catch (MenuManagerNotSetupException var5) {
                System.out.println("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException var6) {
                var6.printStackTrace();
            }
        }

    }
}