package com.ece.sfs;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class File extends Component {
    private final AccessRightList accessRightList;
    private Date lastModifiedDate;
    private Directory parent;
    private String name = "";
    private final UUID uuid;

    // TODO: Implement File Navigation
    // TODO: Implement Group and User Access for file

    public File(String name, AccessRightList accessRightList, Directory parent, Date lastModifiedDate, UUID uuid) {
        this.accessRightList = accessRightList;
        this.accessRightList.addAccessRight("root", AccessRight.defaultAR());
        this.uuid = uuid;
        setName(name);
        setParent(parent);
        setDate(lastModifiedDate);
    }

    @Override
    public void addAccessRights(String username, ArrayList<AccessRight> newAccessRights) {
        accessRightList.addAccessRight(username, newAccessRights);
    }

    @Override
    public void removeAccessRights(String username, ArrayList<AccessRight> newAccessRights) {

    }

    @Override
    public boolean validAccessRight(String username, AccessRight accessRight) {
        return accessRightList.validAccessRight(username, accessRight);
    }

    @Override
    public Directory getParent() {
        return parent;
    }

    @Override
    public void setParent(Directory parent) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent directory cannot be null");
        }

        this.parent = parent;
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
