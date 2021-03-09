package me.hadzakee.dynamicshop.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopItemsList {
    private static List<ShopItem> items = new ArrayList<>();

    public static List<ShopItem> getItems() {
        return items;
    }

    public static void setItems(List<ShopItem> items) {
        ShopItemsList.items = items;
    }

    public static List<ShopItem> getItemsOnPage(int page) {
        return items.stream().filter(item -> item.getPage() == page).collect(Collectors.toList());
    }

    public static void removeItem(int page, int slot) {
        items.stream().filter(item -> item.getPage()==page && item.getSlot()==slot).collect(Collectors.toList()).forEach(item -> items.remove(item));
    }

    public static void changeSellPrice(int page, int slot, int price) {
        items.stream().filter(item -> item.getPage()==page && item.getSlot()==slot).collect(Collectors.toList()).forEach(item -> item.setSellPrice(price));
    }

    public static void changeBuyPrice(int page, int slot, int price) {
        items.stream().filter(item -> item.getPage()==page && item.getSlot()==slot).collect(Collectors.toList()).forEach(item -> item.setBuyPrice(price));
    }

    public static ShopItem getItem(int page, int slot) {
        List<ShopItem> shopItems = items.stream().filter(item -> item.getPage()==page && item.getSlot()==slot).collect(Collectors.toList());
        return shopItems.get(0);
    }

    public static boolean isPageEmpty(int page) {
        return items.stream().noneMatch(item -> item.getPage() == page);
    }
}
