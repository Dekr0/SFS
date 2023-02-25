package com.ece.sfs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String dateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String resolvePath(String baseDir, String name) {
        String absolutePath;
        if (baseDir.compareTo("/") == 0) {
            absolutePath = baseDir + name;
        } else {
            absolutePath = baseDir + "/" + name;
        }

        return absolutePath;
    }

    public static boolean validName(String string) {
        return string == null || string.isEmpty() || !string.matches("^[A-za-z0-9_ ]{1,255}$");
    }

    public static boolean validFileName(String string) {
        return string == null || string.isEmpty() || !string.matches("^[A-za-z0-9_ ./]{1,255}$");
    }

    public static boolean validPassword(String string) {
        return string == null || string.isEmpty() || !string
                .matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,32}$");
    }

    public static boolean validUUID(String string) {
        return string == null || string.isEmpty() || !string
                .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}
