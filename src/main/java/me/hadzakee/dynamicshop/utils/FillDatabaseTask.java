package me.hadzakee.dynamicshop.utils;

import me.hadzakee.dynamicshop.DynamicShop;
import org.bukkit.scheduler.BukkitRunnable;

public class FillDatabaseTask extends BukkitRunnable {
    @Override
    public void run() {
        DynamicShop.fillDatabase();
    }
}
