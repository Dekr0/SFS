package com.ece.sfs;

public abstract class FileSystemComponent {

    public String getDate() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get modified date of a file system component");
    }

    public void setDate(Object date) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set modified date of a file system component");
    }

    public String getName() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get name of a file system component");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set name of a file system component");
    }

    public Long getSize() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get size of a file system component");
    }

    public void setSize(Long size) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set size of a file system component");
    }

    public void add(FileSystemComponent fileSystemComponent) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "add a file system component to a file system component");
    }

    public void remove(FileSystemComponent fileSystemComponent) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "remove a file system component from a file system component");
    }
}
