package com.ece.sfs;


import java.util.Date;
import java.util.UUID;

public class File extends FileSystemComponent {

    private Long size = 0L;
    private String absolutePath = "";
    private String baseDir = "";
    private String name = "";
    private Date lastModifiedDate;

    private UUID uuid;

    // TODO: Implement File Navigation
    // TODO: Implement Group and User Access for file

    public File(String name, String baseDir, Long size, Date lastModifiedDate) {
        uuid = UUID.randomUUID();
        setName(name);
        setBaseDir(baseDir);
        setSize(size);
        setDate(lastModifiedDate);
        setAbsolutePath(Util.resolvePath(baseDir, name));
    }

    @Override
    public Long getSize() {
        return size;
    }

    @Override
    public void setSize(Long size) {
        if (size != null && size >= 0) {
            this.size = size;
        } else {
            throw new IllegalArgumentException("Invalid size");
        }
    }

    @Override
    public String getAbsolutePath() {
        return absolutePath;
    }

    @Override
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    @Override
    public void setBaseDir(String baseDir) {
        if (baseDir != null) {
            this.baseDir = baseDir;
        } else {
            throw new IllegalArgumentException("Invalid base directory");
        }
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
    public int hashCode() {
        return uuid.hashCode();
    }
}
