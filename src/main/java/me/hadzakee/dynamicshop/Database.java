package me.hadzakee.dynamicshop;

import me.hadzakee.dynamicshop.models.ShopItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            try {
                connection = DriverManager.getConnection(DynamicShop.getConnectionURL());
            } catch (SQLException e) {
                System.out.println("Unable to establish a connection with the database");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to find the h2 DB sql driver");
        }
        return connection;
    }


    public static void initializeDatabase() {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS ShopItems(Page int, Slot int, Material varchar(255), Purchased int DEFAULT 0, Sold int DEFAULT 0, Price DECIMAL(30,3));");
            System.out.println("Database loaded successfully");
            statement.close();

        } catch (SQLException e) {
            System.out.println("[DynamicShop] Database initialization error.");
            e.printStackTrace();
        }
    }


    public static void addShopItem(ShopItem item) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO ShopItems(Page, Slot, Material, Purchased, Sold, Price) VALUES(?, ?, ?, ?, ?, ?);");
            statement.setInt(1, item.getPage());
            statement.setInt(2, item.getSlot());
            statement.setString(3, item.getMaterial());
            statement.setInt(4, item.getPurchased());
            statement.setInt(5, item.getSold());
            statement.setInt(6, item.getPrice());
//            System.out.println(item.getPrice());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ShopItem getShopItem(ShopItem item) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT Purchased, Sold, Price FROM ShopItems WHERE Page = ?, Slot = ?;");
            statement.setInt(1, item.getPage());
            statement.setInt(2, item.getSlot());

            ResultSet result = statement.executeQuery();
            result.next();
            item.setPurchased(result.getInt(1));
            item.setSold(result.getInt(2));
            item.setPrice(result.getInt(3));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return item;
    }

    public static List<ShopItem> getAllShopItems() {
        List<ShopItem> items = new ArrayList<>();

        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM ShopItems;");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ShopItem item = new ShopItem();
                item.setPage(result.getInt(1));
                item.setSlot(result.getInt(2));
                item.setMaterial(result.getString(3));
                item.setPurchased(result.getInt(4));
                item.setSold(result.getInt(5));
                item.setPrice(result.getInt(6));
                items.add(item);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return items;
    }

    public static void truncateTable() {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("TRUNCATE TABLE ShopItems;");
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
