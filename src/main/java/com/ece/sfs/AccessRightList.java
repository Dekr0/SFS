package com.ece.sfs;

import java.util.ArrayList;
import java.util.HashMap;


public class AccessRightList {
    private final HashMap<String, ArrayList<AccessRight>> accessRightList;

    public AccessRightList(HashMap<String, ArrayList<AccessRight>> accessRightList) {
        this.accessRightList = accessRightList;
    }

    public void addAccessRight(String username, ArrayList<AccessRight> newAccessRights) {
        if (!accessRightList.containsKey(username)) {
            accessRightList.put(username, new ArrayList<>(newAccessRights));
        } else {
            for (AccessRight newAccessRight : newAccessRights) {
                if (!accessRightList.get(username).contains(newAccessRight)) {
                    accessRightList.get(username).add(newAccessRight);
                }
            }
        }
    }

    public boolean validAccessRight(String username, AccessRight accessRight) {
        return accessRightList.containsKey(username) &&
                accessRightList.get(username).contains(accessRight);
    }
}
