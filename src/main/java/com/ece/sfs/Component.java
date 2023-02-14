package com.ece.sfs;


import java.util.ArrayList;

public abstract class Component {

    /* The following methods are not supported for File class */

    public void addComponent(Component component) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Component getComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void removeComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* ------------------------------------------------------ */

    public void addAccessRights(String username, ArrayList<AccessRight> newAccessRights) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void removeAccessRights(String username, ArrayList<AccessRight> newAccessRights) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean validAccessRight(String username, AccessRight accessRight) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getDate() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get modified date of a file system component");
    }

    public void setDate(Object date) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set modified date of a file system component");
    }

    public Component getParent() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setParent(Directory parent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void getEncryptedName() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get encrypted name of a file system component");
    }

    public String getName() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get name of a file system component");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set name of a file system component");
    }
}
