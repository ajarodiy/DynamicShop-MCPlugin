package me.hadzakee.dynamicshop.commands;

import me.hadzakee.dynamicshop.menu.MenuManager;
import me.hadzakee.dynamicshop.menu.MenuManagerException;
import me.hadzakee.dynamicshop.menu.MenuManagerNotSetupException;
import me.hadzakee.dynamicshop.menus.ShopPage;
import me.hadzakee.dynamicshop.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenShop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.message("Only players are allowed to open the shop."));
            return true;
        }

        Player player = (Player) sender;

        try {
            new ShopPage(MenuManager.getPlayerMenuUtility(player), 1).open();
        } catch (MenuManagerNotSetupException | MenuManagerException e) {
            e.printStackTrace();
        }


        return true;
    }
}
