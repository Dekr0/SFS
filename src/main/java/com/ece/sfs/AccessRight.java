package com.ece.sfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum AccessRight {
    EXECUTE,
    OWN,
    READ,
    WRITE;

    public static ArrayList<AccessRight> defaultAR() {
        return new ArrayList<>(List.of(EXECUTE, OWN, READ, WRITE));
    }

    public static Map<String, ArrayList<AccessRight>> rootAR() {
        return Map.of("root", defaultAR());
    }
}
