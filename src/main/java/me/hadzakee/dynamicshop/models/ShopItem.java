package me.hadzakee.dynamicshop.models;

public class ShopItem {
    private int page;
    private int slot;
    private String material;
    private int price;
    private int purchased = 0;
    private int sold = 0;

    public ShopItem(int page, int slot, String material, int price, int purchased, int sold) {
        this.page = page;
        this.slot = slot;
        this.material = material;
        this.price = price;
        this.purchased = purchased;
        this.sold = sold;
    }

    public ShopItem() {
        this.page = 0;
        this.slot = 0;
        this.material = null;
        this.price = 0;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getCurrentPrice() {
        int stock = sold - purchased;
        return price + stock/100;
    }

    public int getStock() {
        return sold - purchased;
    }
}
