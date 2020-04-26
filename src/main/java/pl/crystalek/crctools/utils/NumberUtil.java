package pl.crystalek.crctools.utils;

public class NumberUtil {

    public static boolean isInt(final String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
