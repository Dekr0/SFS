package com.ece.sfs.core;


import io.vavr.control.Either;

import java.util.List;

public abstract class Component {

    /* The following methods are not supported for File class */

    public Either<Boolean, String> addComponent(Component component) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Either<Component, String> getComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Either<Boolean, String> removeComponent(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /* ------------------------------------------------------ */

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

    public String getName() {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "get name of a file system component");
    }

    public Either<Boolean, String> setName(String name) {
        throw new UnsupportedOperationException("Unsupported operation: cannot " +
                "set name of a file system component");
    }

    public String getUUID(){
        throw new UnsupportedOperationException("Not implemented");
    }
}
