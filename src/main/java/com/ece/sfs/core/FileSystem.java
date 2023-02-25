package com.ece.sfs.core;

import com.ece.sfs.access.AccessManager;
import com.ece.sfs.access.AccessRight;
import com.ece.sfs.access.AccessRightList;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.ece.sfs.Util.validFileName;


@org.springframework.stereotype.Component
public class FileSystem {

    public enum FileType {
        FILE, DIR
    }

    private String username;
    private String groupName;

    private Directory currentDirectory;
    private Directory root;
    private final AccessManager accessManager;

    @Autowired
    public FileSystem(AccessManager accessManager, Directory root) {
        this.accessManager = accessManager;
        this.root = root;
    }

    public Either<Boolean, String> changeDirectory(String name) {
        if (validFileName(name)) {
            return Either.right("Invalid directory name");
        }

        Directory dest;

        if (name.compareTo("..") == 0) {
            dest = (Directory) currentDirectory.getParent();
        } else {
            Either<Component, String> result = currentDirectory.getComponent(name);

            if (result.isRight()) {
                return Either.right(result.get());
            }

            if (result.getLeft() instanceof Directory) {
                dest = (Directory) result.getLeft();
            } else {
                return Either.right(name + "Not a directory");
            }
        }

        Either<Boolean, String> result = accessManager
                .hasAccessRight(groupName, username, dest.getUUID(), AccessRight.READ);

        if (result.isLeft()) {
            if (result.getLeft()) {
                currentDirectory = dest;
            } else {
                return Either.right("You do not have access rights to this directory");
            }

            return Either.left(result.getLeft());
        }

        return Either.right(result.get());
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

    public Either<Boolean, String> createComponent(String name, FileType type) {
        if (validFileName(name)) {
            return Either.right("Name cannot be empty");
        }

        Either<Boolean, String> result = accessManager.hasAccessRight(
                groupName,
                username,
                currentDirectory.getUUID(),
                AccessRight.WRITE
        );

        if (currentDirectory.hasComponent(name)) {
            return Either.right("Component " + name + " already exists");
        }

        if (result.isLeft()) {
            if (result.getLeft()) {
                if (type == FileType.FILE) {
                    currentDirectory.addComponent(
                            new File(
                                    name,
                                    currentDirectory,
                                    Calendar.getInstance().getTime(),
                                    UUID.randomUUID()
                            )
                    );
                } else if (type == FileType.DIR) {
                    currentDirectory.addComponent(
                            new Directory(
                                    name,
                                    currentDirectory,
                                    Calendar.getInstance().getTime(),
                                    UUID.randomUUID(),
                                    new HashMap<>()
                            )
                    );
                }

                accessManager.addAccessRightList(
                        currentDirectory.getComponent(name).getLeft().getUUID(),
                        new AccessRightList(
                                AccessRight.defaultAR(username),
                                AccessRight.defaultRead(groupName)
                        )
                );
            } else {
                return Either.right("You do not have permission to create a component in this directory");
            }

            return Either.left(result.getLeft());
        }

        return Either.right(result.get());
    }

    public Either<Boolean, String> deleteComponent(String name) {
        if (validFileName(name)) {
            return Either.right("Name cannot be empty");
        }

        Either<Component, String> result = currentDirectory.getComponent(name);

        if (result.isRight()) {
            return Either.right(result.get());
        }

        Component target = result.getLeft();

        Either<Boolean, String> r = accessManager
                .hasAccessRight(groupName, username, target.getUUID(), AccessRight.WRITE);

        if (r.isLeft()) {
            if (r.getLeft()) {
                currentDirectory.removeComponent(name);
            } else {
                return Either.right("You do not have permission to delete this component");
            }

            return Either.left(r.getLeft());
        }

        return Either.right(r.get());
    }

    public Either<String, String> ls() {
        Either<Boolean, String> result = accessManager
                .hasAccessRight(groupName, username, currentDirectory.getUUID(), AccessRight.READ);

        String output = "";

        if (result.isLeft()) {
            if (result.getLeft()) {
                for (Component c : currentDirectory.getComponents()) {
                    Either<Boolean, String> own = accessManager
                            .hasAccessRight(groupName, username, c.getUUID(), AccessRight.OWN);
                    if (own.isLeft()) {
                        output = own.getLeft() ? output + c.getName() + "\n" : c.getUUID() + "\n";
                    } else {
                        return Either.right(own.get());
                    }
                }
            } else {
                return Either.right("You do not have permission to list this directory");
            }
        }

        return Either.left(output);
    }

    public Either<Boolean, String> renameComponent(String oldName, String newName) {
        Either<Component, String> result = currentDirectory.getComponent(oldName);

        if (result.isRight()) {
            return Either.right(result.get());
        }

        Component component = result.getLeft();

        Either<Boolean, String> r = accessManager
                .hasAccessRight(groupName, username, component.getUUID(), AccessRight.WRITE);

        if (r.isLeft()) {
            if (r.getLeft()) {
                Either<Boolean, String> ok = component.setName(newName);
                if (ok.isLeft()) {
                    currentDirectory.removeComponent(oldName);
                    currentDirectory.addComponent(component);
                } else {
                    return Either.right(ok.get());
                }
            } else {
                return Either.right("You do not have permission to rename " + oldName);
            }

            return Either.left(r.getLeft());
        }

        return Either.right(r.get());
    }

    public void setGroupName(String groupName) throws IllegalArgumentException {
        if (validFileName(groupName)) {
            throw new IllegalArgumentException("Group name cannot be empty");
        } else {
            this.groupName = groupName;
        }
    }

    public void setUsername(String username) throws IllegalArgumentException {
        if (validFileName(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else {
            this.username = username;
        }
    }

    public void loginHome(String groupName, String username) throws IllegalArgumentException {
        setUsername(username);
        setGroupName(groupName);

        currentDirectory = (Directory) root.getComponent("home").getLeft();

        if (!currentDirectory.hasComponent(username)) {
            currentDirectory.addComponent(
                    new Directory(
                            username,
                            currentDirectory,
                            Calendar.getInstance().getTime(),
                            UUID.randomUUID(),
                            new HashMap<>()
                    )
            );

            accessManager.addAccessRightList(
                    currentDirectory.getComponent(username).getLeft().getUUID(),
                    new AccessRightList(
                            AccessRight.defaultAR(username),
                            AccessRight.defaultRead(groupName)
                    )
            );
        }

        currentDirectory = (Directory) currentDirectory.getComponent(username).getLeft();

    }
}
