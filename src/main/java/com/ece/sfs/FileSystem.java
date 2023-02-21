package com.ece.sfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

import static com.ece.sfs.Util.validString;


@Controller
public class FileSystem {

    public enum FileType {
        FILE, DIR
    }

    private String username;
    private String groupName;

    private Directory currentDirectory;
    private final AccessManager accessManager;

    @Autowired
    public FileSystem(AccessManager accessManager, Directory root) {
        this.accessManager = accessManager;
        this.currentDirectory = root;
    }

    public boolean changeDirectory(String name) {
        if (!validString(name)) {
            try {
                Directory dest;

                if (name.compareTo("..") == 0) {
                    dest = (Directory) currentDirectory.getParent();
                } else {
                    dest = (Directory) currentDirectory.getComponent(name);
                }

                if (accessManager.hasAccessRight(groupName, username, dest.getUUID(), AccessRight.READ)) {
                    currentDirectory = dest;
                    return true;
                }
                System.err.println("You do not have permission to access directory " + name);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (ClassCastException e) {
                System.err.println(name + " is not a directory");
            }
        }

        return false;
    }

    public String getCurrentPath() {
        StringBuilder currentPath = new StringBuilder();

        Directory current = currentDirectory;
        while (current.getName().compareTo("/") != 0) {
            currentPath.insert(0, "/" + current.getName());
            current = (Directory) current.getParent();
        }

        return currentPath.toString();
    }

    public boolean createComponent(String name, FileType type) {
        if (validString(name)) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                if (accessManager.hasAccessRight(groupName, username, currentDirectory.getUUID(), AccessRight.WRITE)) {
                    if (type == FileType.FILE) {
                        currentDirectory.addComponent(
                                new File(
                                        name,
                                        currentDirectory,
                                        Calendar.getInstance().getTime(),
                                        UUID.randomUUID()
                                )
                        );
                    } else if (type == FileType.DIR) {
                        currentDirectory.addComponent(
                                new Directory(
                                        name,
                                        currentDirectory,
                                        Calendar.getInstance().getTime(),
                                        UUID.randomUUID(),
                                        new HashMap<>()
                                )
                        );
                    }

                    accessManager.addAccessRightList(
                            currentDirectory.getComponent(name).getUUID(),
                            new AccessRightList(
                                    AccessRight.defaultAR(username),
                                    AccessRight.defaultRead(groupName)
                            )
                    );

                    return true;
                }

                System.err.println("You do not have permission to create a component in this directory");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

    public boolean deleteComponent(String name) {
        if (validString(name)) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                Component target = currentDirectory.getComponent(name);
                if (accessManager.hasAccessRight(groupName, username, target.getUUID(), AccessRight.WRITE)) {
                    currentDirectory.removeComponent(name);
                    return true;
                }
                System.err.println("You do not have permission to delete " + name);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

    public boolean renameComponent(String oldName, String newName) {
        try {
            Component component = currentDirectory.getComponent(oldName);
            if (accessManager.hasAccessRight(groupName, username, component.getUUID(), AccessRight.WRITE)) {
                component.setName(newName);

                currentDirectory.removeComponent(oldName);
                currentDirectory.addComponent(component);

                return true;
            }

            System.err.println("You do not have permission to rename " + oldName);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public void setGroupName(String groupName) throws IllegalArgumentException {
        if (validString(groupName)) {
            throw new IllegalArgumentException("Group name cannot be empty");
        } else {
            this.groupName = groupName;
        }
    }

    public void setUsername(String username) throws IllegalArgumentException {
        if (validString(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else {
            this.username = username;
        }
    }

    public void loginHome(String groupName, String username) throws IllegalArgumentException {
        setUsername(username);
        setGroupName(groupName);

        currentDirectory = (Directory) currentDirectory.getComponent("home");

        if (!currentDirectory.hasComponent(username)) {
            currentDirectory.addComponent(
                    new Directory(
                            username,
                            currentDirectory,
                            Calendar.getInstance().getTime(),
                            UUID.randomUUID(),
                            new HashMap<>()
                    )
            );

            accessManager.addAccessRightList(
                    currentDirectory.getComponent(username).getUUID(),
                    new AccessRightList(
                            AccessRight.defaultAR(username),
                            AccessRight.defaultRead(groupName)
                    )
            );
        }

        currentDirectory = (Directory) currentDirectory.getComponent(username);

    }
}
