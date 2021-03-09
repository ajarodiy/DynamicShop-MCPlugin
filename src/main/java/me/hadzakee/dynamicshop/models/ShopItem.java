package me.hadzakee.dynamicshop.models;

public class ShopItem {
    private int page;
    private int slot;
    private String material;
    private int sellPrice;
    private int purchased = 0;
    private int sold = 0;
    private int buyPrice;

    public ShopItem(int page, int slot, String material, int sellPrice, int buyPrice, int purchased, int sold) {
        this.page = page;
        this.slot = slot;
        this.material = material;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.purchased = purchased;
        this.sold = sold;
    }

    public ShopItem() {
        this.page = 0;
        this.slot = 0;
        this.material = null;
        this.sellPrice = 0;
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

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
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

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getCurrentSellPrice() {
        int stock = getStock();
        return sellPrice - stock/100;
    }

    public int getCurrentBuyPrice() {
        int stock = getStock();
        return buyPrice - stock/100;
    }

//    public int getCurrentPrice() {
//        int stock = sold - purchased;
//        return sellPrice - stock/100;
//    }

    public int getStock() {
        return sold - purchased;
    }
}
