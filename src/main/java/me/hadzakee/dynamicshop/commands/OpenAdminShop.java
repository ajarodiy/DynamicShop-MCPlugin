package me.hadzakee.dynamicshop.commands;

import me.hadzakee.dynamicshop.menu.MenuManager;
import me.hadzakee.dynamicshop.menu.MenuManagerException;
import me.hadzakee.dynamicshop.menu.MenuManagerNotSetupException;
import me.hadzakee.dynamicshop.menus.ShopEditMenu;
import me.hadzakee.dynamicshop.menus.ShopPage;
import me.hadzakee.dynamicshop.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenAdminShop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.message("Only players are allowed to open the admin shop."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("change.this")) {
            player.sendMessage(MessageUtils.message("You don't have the required permissions."));
            return true;
        }

        try {
            new ShopEditMenu(MenuManager.getPlayerMenuUtility(player), 1).open();
        } catch (MenuManagerNotSetupException | MenuManagerException e) {
            e.printStackTrace();
        }


        return true;
    }
}
