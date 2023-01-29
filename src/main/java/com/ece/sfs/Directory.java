package com.ece.sfs;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Directory extends FileSystemComponent {

    private String absolutePath = "";
    private String baseDir = "";
    private String name = "";
    private Date lastModifiedDate;

    private UUID uuid;

    private HashMap<String, FileSystemComponent> entries;

    // TODO: Implement File Navigation
    // TODO: Implement Group and User Access for directory

    public Directory(String baseDir, String name, Date lastModifiedDate) {
        uuid = UUID.randomUUID();
        entries = new HashMap<>();
        setName(name);
        setBaseDir(baseDir);
        setDate(lastModifiedDate);
        setAbsolutePath(Util.resolvePath(baseDir, name));
    }

    @Override
    public Long getSize() {
        Long size = 0L;
        for (FileSystemComponent fileSystemComponent : entries.values()) {
            size += fileSystemComponent.getSize();
        }

        return size;
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        if (absolutePath != null) {
            this.absolutePath = absolutePath;
        } else {
            throw new IllegalArgumentException("Invalid absolute path");
        }
    }

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    @Override
    public void setBaseDir(String baseDir) {
        if (name.compareTo("/") == 0) {
            if (baseDir.isEmpty()) {
                return;
            } else {
                throw new IllegalArgumentException("Invalid base directory");
            }
        }

        this.baseDir = baseDir;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        this.name = name;
    }

    @Override
    public String getDate() {
        return Util.dateToString(lastModifiedDate, "dd/MM/yyyy");
    }

    @Override
    public void setDate(Object date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid date");
        }

        if (date instanceof Date) {
            this.lastModifiedDate = (Date) date;
        }
    }

    @Override
    public void add(FileSystemComponent fileSystemComponent) {
        if (fileSystemComponent == null) {
            throw new IllegalArgumentException("Invalid file system component");
        }

        entries.put(fileSystemComponent.getName(), fileSystemComponent);
    }

    @Override
    public void remove(FileSystemComponent fileSystemComponent) {
        if (fileSystemComponent == null) {
            throw new IllegalArgumentException("Invalid file system component");
        }

        if (entries.remove(fileSystemComponent.getName()) == null) {
            throw new IllegalArgumentException("File or Directory" +
                    fileSystemComponent.getName() + "does not exist");
        }
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
