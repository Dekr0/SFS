package com.ece.sfs;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.GroupManager;

import java.util.ArrayList;
import java.util.List;


public class UserGroupManager implements GroupManager {

    private final ArrayList<Group> groups;

    public UserGroupManager(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @Override
    public List<String> findAllGroups() {
        ArrayList<String> groupNames = new ArrayList<>();

        groups.forEach(group -> groupNames.add(group.getName()));

        return groupNames;
    }

    @Override
    public List<String> findUsersInGroup(String groupName) {
        return null;
    }

    @Override
    public void createGroup(String groupName, List<GrantedAuthority> authorities) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                throw new IllegalArgumentException("Group already exists");
            }
        }

        groups.add(new Group(groupName));
    }

    @Override
    public void deleteGroup(String groupName) {

    }

    @Override
    public void renameGroup(String oldName, String newName) {

    }

    @Override
    public void addUserToGroup(String username, String group) {

    }

    @Override
    public void removeUserFromGroup(String username, String groupName) {

    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        return null;
    }

    @Override
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {

    }

    @Override
    public void removeGroupAuthority(String groupName, GrantedAuthority authority) {

    }
}
