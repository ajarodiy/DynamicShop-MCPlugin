package me.hadzakee.dynamicshop;

import me.hadzakee.dynamicshop.commands.OpenAdminShop;
import me.hadzakee.dynamicshop.commands.OpenShop;
import me.hadzakee.dynamicshop.menu.MenuManager;
import me.hadzakee.dynamicshop.menus.PlayerMenuUtility;
import me.hadzakee.dynamicshop.models.ShopItemsList;
import me.hadzakee.dynamicshop.utils.FillDatabaseTask;
import me.hadzakee.dynamicshop.utils.MessageUtils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class DynamicShop extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static String url;
    private static DynamicShop plugin;
    private static Economy economy = null;

    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        url = "jdbc:h2:" + getDataFolder().getAbsolutePath() + "/data/ShopItems";
        plugin = this;
        Database.initializeDatabase();

        registerCommands();

        MenuManager.setup(getServer(), this, PlayerMenuUtility.class);

        fillList();

        new FillDatabaseTask().runTaskTimerAsynchronously(this, 20*10, 20*10);
    }

    @Override
    public void onDisable() {
        System.out.println("[DynamicShop] Updating database, please wait...");
        fillDatabase();
    }



    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public void fillList() {
        new BukkitRunnable() {
            @Override
            public void run() {
                ShopItemsList.setItems(Database.getAllShopItems());
            }
        }.runTaskAsynchronously(this);
    }

    public static void fillDatabase() {
        Database.truncateTable();
        ShopItemsList.getItems().forEach(Database::addShopItem);
//        System.out.println("Database updated successfully.");
    }

    public void registerCommands() {
        getCommand("shop").setExecutor(new OpenShop());
        getCommand("ashop").setExecutor(new OpenAdminShop());
    }






    public static String getConnectionURL() {
        return url;
    }

    public static DynamicShop getInstance() {
        return plugin;
    }

    public static Economy getEconomy() {
        return economy;
    }

}
