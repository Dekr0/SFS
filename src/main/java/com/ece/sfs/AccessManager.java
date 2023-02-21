package com.ece.sfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ece.sfs.Util.validString;


public class AccessManager {

    private final UserGroupManager userGroupManager;

    private HashMap<String, AccessRightList> accessRightListMap;

    public AccessManager(UserGroupManager userGroupManager) {
        accessRightListMap = new HashMap<>();

        this.userGroupManager = userGroupManager;
    }

    public void addUserAccessRight(String username, String uuid, ArrayList<AccessRight> newAccessRightsUser) {
        if (validString(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (validString(uuid)) {
            throw new IllegalArgumentException("UUID cannot be empty");
        } else if (!accessRightListMap.containsKey(uuid)) {
            accessRightListMap.put(uuid, new AccessRightList(
                    new HashMap<>(Map.of(username, newAccessRightsUser)),
                    new HashMap<>()
            ));
        } else {
            accessRightListMap.get(uuid).addUserAccessRight(username, newAccessRightsUser);
        }
    }

    public void addGroupAccessRight(String groupName, String uuid, ArrayList<AccessRight> newAccessRightsGroup) {
        if (validString(groupName)) {
            throw new IllegalArgumentException("Group name cannot be empty");
        } else if (validString(uuid)) {
            throw new IllegalArgumentException("UUID cannot be empty");
        } else if (!accessRightListMap.containsKey(uuid)) {
            accessRightListMap.put(uuid, new AccessRightList(
                    new HashMap<>(),
                    new HashMap<>(Map.of(groupName, newAccessRightsGroup))
            ));
        } else {
            accessRightListMap.get(uuid).addGroupAccessRight(groupName, newAccessRightsGroup);
        }
    }

    public void addAccessRightList(String uuid, AccessRightList accessRightList)
            throws IllegalArgumentException {
        if (validString(uuid)) {
            throw new IllegalArgumentException("UUID cannot be null or empty");
        } else if (accessRightList == null) {
            throw new IllegalArgumentException("AccessRightList cannot be null");
        } else if (accessRightListMap.containsKey(uuid)) {
            throw new IllegalArgumentException("AccessRightList with UUID " + uuid + " already exists");
        } else {
            accessRightListMap.put(uuid, accessRightList);
        }
    }

    public boolean hasAccessRight(String groupName, String username, String uuid, AccessRight accessRight)
            throws IllegalArgumentException {

        if (validString(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (validString(groupName)) {
            throw new IllegalArgumentException("Group name cannot be empty");
        } else if (validString(uuid)) {
            throw new IllegalArgumentException("UUID cannot be empty");
        } else if (!accessRightListMap.containsKey(uuid)) {
            throw new IllegalArgumentException("AccessRightList with UUID " + uuid + " does not exist");
        } else {
            if (accessRightListMap.get(uuid).hasUserAccessRight(username, accessRight)) {
                return true;
            } else {
                for (String group : userGroupManager.getGroupsForUser(username)) {
                    if (accessRightListMap.get(uuid).hasGroupAccessRight(group, accessRight)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
