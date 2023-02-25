package com.ece.sfs.command;

import com.ece.sfs.prompt.ShellPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

import static com.ece.sfs.Util.validName;

@ShellComponent
public class AdminCommand extends Command {

    private final GroupManager groupManager;

    private final GrantedAuthority adminAuthority;

    private final GrantedAuthority userAuthority;

    private final InMemoryUserDetailsManager userDetailsManager;

    private final ShellPrompt shellPrompt;

    @Autowired
    public AdminCommand(
            GrantedAuthority adminAuthority,
            GrantedAuthority userAuthority,
            GroupManager groupManager, InMemoryUserDetailsManager userDetailsManager,
            ShellPrompt shellPrompt
    ) {
        this.adminAuthority = adminAuthority;
        this.userAuthority = userAuthority;
        this.groupManager = groupManager;
        this.shellPrompt = shellPrompt;
        this.userDetailsManager = userDetailsManager;
    }

    @ShellMethod("Create user group")
    public void groupadd(String groupName, String authorities) {
        if (validName(groupName)) {
            shellPrompt.printError("Invalid group name");

            return;
        }

        if (groupManager.findAllGroups().contains(groupName)) {
            shellPrompt.printError("Group already exists");

            return;
        }

        if (authorities.compareTo(adminAuthority.getAuthority()) == 0) {
            groupManager.createGroup(groupName, List.of(adminAuthority));
        } else if (authorities.compareTo(userAuthority.getAuthority()) == 0) {
            groupManager.createGroup(groupName, List.of(userAuthority));
        } else {
            shellPrompt.printError("Invalid authority");
        }
    }

    @ShellMethod("Delete user group")
    public void groupdel(String groupName) {
        if (validName(groupName)) {
            shellPrompt.printError("Invalid group name");

            return;
        }

        if (!groupManager.findAllGroups().contains(groupName)) {
            shellPrompt.printError("Group does not exist");

            return;
        }

        groupManager.deleteGroup(groupName);
    }

    @ShellMethod("Add user to group")
    public void useradd(String username, String groupName) {
        if (validUsernameAndGroup(username, groupName)) {
            return;
        }

        if (groupManager.findUsersInGroup(groupName).contains(username)) {
            shellPrompt.printError("User already in group");

            return;
        }

        groupManager.addUserToGroup(username, groupName);
    }

    @ShellMethod("Remove user from group")
    public void userdel(String username, String groupName) {
        if (validUsernameAndGroup(username, groupName)) {
            return;
        }

        if (!groupManager.findUsersInGroup(groupName).contains(username)) {
            shellPrompt.printError("User not in group");

            return;
        }

        groupManager.removeUserFromGroup(username, groupName);
    }

    private boolean validUsernameAndGroup(String username, String groupName) {
        if (validName(username) || validName(groupName)) {
            shellPrompt.printError("Invalid username or group name");

            return true;
        }

        if (!groupManager.findAllGroups().contains(groupName)) {
            shellPrompt.printError("Group does not exist");

            return true;
        }

        if (!userDetailsManager.userExists(username)) {
            shellPrompt.printError("User does not exist");

            return true;
        }


        return false;
    }
}
