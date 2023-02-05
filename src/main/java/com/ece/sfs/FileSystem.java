package com.ece.sfs;

import java.util.Calendar;


public class FileSystem {

    public enum FileType {
        FILE, DIR
    }

    private final Directory root;
    private Directory currentDirectory;


    public FileSystem(Directory root, String usr) {
        this.root = root;

        currentDirectory = (Directory) root.getComponent("home");

        if (!currentDirectory.hasComponent(usr)) {
            currentDirectory.addComponent(
                    new Directory(
                            usr,
                            currentDirectory,
                            Calendar.getInstance().getTime()
                    )
            );
        }

        currentDirectory = (Directory) currentDirectory.getComponent(usr);
    }

    public void changeDirectory(String name) {
        if (name != null && !name.isEmpty()) {
            try {
                currentDirectory = (Directory) currentDirectory.getComponent(name);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (ClassCastException e) {
                System.err.println(name + " is not a directory");
            }
        }
    }

    public void changePrevDirectory() {
        if (currentDirectory.getParent() != null) {
            currentDirectory = (Directory) currentDirectory.getParent();
        }
    }

    public String getCurrentPath() {
        StringBuilder currentPath = new StringBuilder();

        Directory current = currentDirectory;
        while (current != root) {
            currentPath.insert(0, "/" + current.getName());
            current = (Directory) current.getParent();
        }

        return currentPath.toString();
    }

    public void createComponent(String name, FileType type) {
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                if (type == FileType.FILE) {
                    currentDirectory.addComponent(
                            new File(
                                    name,
                                    currentDirectory,
                                    Calendar.getInstance().getTime()
                            )
                    );
                } else if (type == FileType.DIR) {
                    currentDirectory.addComponent(
                            new Directory(
                                    name,
                                    currentDirectory,
                                    Calendar.getInstance().getTime()
                            )
                    );
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void deleteComponent(String name) {
        if (name == null || name.isEmpty()) {
            System.err.println("Name cannot be empty");
        } else {
            try {
                currentDirectory.removeComponent(name);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void renameComponent(String oldName, String newName) {
        try {
            Component component = currentDirectory.getComponent(oldName);
            component.setName(newName);

            currentDirectory.removeComponent(oldName);
            currentDirectory.addComponent(component);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
