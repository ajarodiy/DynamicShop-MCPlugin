package me.hadzakee.dynamicshop.utils;

public class MessageUtils {

    private static String prefix;
    private static String messagecolor;

    public static void setPrefix(String prefix) {
        MessageUtils.prefix = prefix;
    }

    public static void setMessagecolor(String messagecolor) {
        MessageUtils.messagecolor = messagecolor;
    }

    public static String message(String message) {
        return ColorTranslator.translateColorCodes(prefix + " " + messagecolor + message);
    }

}
