package com.ece.sfs;

import java.util.*;


public class FileSystem {

    public enum FileType {
        FILE, DIR
    }

    private Directory currentDirectory;
    private final String username;

    public FileSystem(Directory root, String username) {
        currentDirectory = (Directory) root.getComponent("home");
        currentDirectory.addAccessRights(username, new ArrayList<>(List.of(AccessRight.READ)));

        if (!currentDirectory.hasComponent(username)) {
            currentDirectory.addComponent(
                    new Directory(
                            username,
                            new AccessRightList(
                                    new HashMap<>(Map.of(username, AccessRight.defaultAR()))
                            ),
                            currentDirectory,
                            Calendar.getInstance().getTime(),
                            UUID.randomUUID(),
                            new HashMap<>()
                    )
            );
        }

        currentDirectory = (Directory) currentDirectory.getComponent(username);

        this.username = username;
    }

    public boolean changeDirectory(String name) {
        if (name != null && !name.isEmpty()) {
            try {
                if (currentDirectory.getComponent(name).validAccessRight(username, AccessRight.READ)) {
                    currentDirectory = (Directory) currentDirectory.getComponent(name);
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

    public boolean changePrevDirectory() {
        Directory parent = (Directory) currentDirectory.getParent();
        if (parent != null) {
            if (parent.validAccessRight(username, AccessRight.READ)) {
                currentDirectory = parent;

                return true;
            } else {
                System.err.println("You do not have permission to access directory " + parent.getName());
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
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                if (type == FileType.FILE) {
                    currentDirectory.addComponent(
                            new File(
                                    name,
                                    new AccessRightList(
                                            new HashMap<>(Map.of(username, AccessRight.defaultAR()))
                                    ),
                                    currentDirectory,
                                    Calendar.getInstance().getTime(),
                                    UUID.randomUUID()
                            )
                    );
                } else if (type == FileType.DIR) {
                    currentDirectory.addComponent(
                            new Directory(
                                    name,
                                    new AccessRightList(
                                            new HashMap<>(Map.of(username, AccessRight.defaultAR()))
                                    ),
                                    currentDirectory,
                                    Calendar.getInstance().getTime(),
                                    UUID.randomUUID(),
                                    new HashMap<>()
                            )
                    );
                }

                return true;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

    public boolean deleteComponent(String name) {
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                if (currentDirectory.getComponent(name).validAccessRight(username, AccessRight.WRITE)) {
                    currentDirectory.removeComponent(name);

                    return true;
                } else {
                    System.err.println("You do not have permission to delete " + name);
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

    public boolean renameComponent(String oldName, String newName) {
        try {
            Component component = currentDirectory.getComponent(oldName);
            if (component.validAccessRight(username, AccessRight.WRITE)) {
                component.setName(newName);

                currentDirectory.removeComponent(oldName);
                currentDirectory.addComponent(component);

                return true;
            } else {
                System.err.println("You do not have permission to rename " + oldName);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }
}
