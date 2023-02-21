package com.ece.sfs;

import java.util.*;


public class Directory extends Component {

    private Directory parent = null;
    private Date lastModifiedDate;
    private String name = "";
    private final String uuid;

    private final HashMap<String, Component> components;

    // TODO: Implement File Navigation
    // TODO: Implement Group and User Access for directory

    public Directory(
            String name,
            Directory parent,
            Date lastModifiedDate,
            UUID uuid,
            HashMap<String, Component> components
    ) {
        this.uuid = uuid.toString();
        this.components = components;
        setName(name);
        setParent(parent);
        setDate(lastModifiedDate);
    }

    @Override
    public void addComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Adding a null object into the directory");
        }

        components.put(component.getName(), component);
    }

    @Override
    public Component getComponent(String name) throws IllegalArgumentException {
        if (hasComponent(name)) {
            return components.get(name);
        }

        throw new IllegalArgumentException("No such file or directory: " + name);
    }

    @Override
    public boolean hasComponent(String name) {
        return components.containsKey(name);
    }

    @Override
    public void removeComponent(String name) {
        if (components.remove(name) == null) {
            throw new IllegalArgumentException(
                    "File or Directory" + name + "does not exist");
        }
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void setParent(Directory parent) {
        if (name.compareTo("/") == 0) {
            if (parent != null) {
                throw new IllegalArgumentException("/ directory does not have a parent directory");
            }
        } else {
            if (parent == null) {
                throw new IllegalArgumentException("Parent directory cannot be null");
            }

            this.parent = parent;
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
    public String getUUID() {
        return uuid;
    }
}
