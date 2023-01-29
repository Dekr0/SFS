package com.ece.sfs;

import org.springframework.beans.factory.annotation.Value;

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
}
