package com.ece.sfs;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Stack;

public class FileSystem {

    // Directory Path
    private Stack<String> path;

    // Map all files into a single table
    private HashMap<String, FileSystemComponent> components;

    public FileSystem() {
        components = new HashMap<>();
        path = new Stack<>();
    }

    public void createFile(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        String baseDir = getCurrentPath();
        String absolutePath = Util.resolvePath(baseDir, name);
        if (components.containsKey(absolutePath)) {
            throw new IllegalArgumentException("File already exists");
        } else {
            components.put(absolutePath, new File(name, baseDir, 0L, Calendar.getInstance().getTime()));
        }
    }

    public void deleteFile(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        String absolutePath = Util.resolvePath(getCurrentPath(), name);
        if (components.containsKey(absolutePath)) {
            components.remove(absolutePath);
        } else {
            throw new IllegalArgumentException("File does not exist");
        }
    }

    public void createDirectory(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        String baseDir = getCurrentPath();
        String absolutePath = Util.resolvePath(baseDir, name);
        if (components.containsKey(absolutePath)) {
            throw new IllegalArgumentException("Directory already exists");
        } else {
            components.put(absolutePath, new Directory(name, baseDir, Calendar.getInstance().getTime()));
        }
    }

    public void deleteDirectory(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        String absolutePath = Util.resolvePath(getCurrentPath(), name);
        if (components.containsKey(absolutePath)) {
            components.remove(absolutePath);
        } else {
            throw new IllegalArgumentException("Directory does not exist");
        }
    }

    public void rename(String name, String newName) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Invalid new name");
        }

    }

    public String getCurrentPath() {
        StringBuilder currentPath = new StringBuilder();
        for (String s : path) {
            if (s.compareTo("/") == 0) {
                currentPath = new StringBuilder(s);
            } else {
                currentPath.append("/").append(s);
            }
        }

        return currentPath.toString();
    }
}
