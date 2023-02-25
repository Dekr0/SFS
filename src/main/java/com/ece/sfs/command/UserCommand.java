package com.ece.sfs.command;

import com.ece.sfs.core.FileSystem;
import com.ece.sfs.prompt.ShellPrompt;
import com.ece.sfs.prompt.TerminalColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static com.ece.sfs.Util.validName;
import static com.ece.sfs.Util.validPassword;


@ShellComponent
public class UserCommand extends Command {

    private final AuthenticationManager authManager;

    private final FileSystem fileSystem;

    private final ShellPrompt shellPrompt;

    @Autowired
    public UserCommand(AuthenticationManager authManager, FileSystem fileSystem, ShellPrompt shellPrompt) {
        this.authManager = authManager;
        this.fileSystem = fileSystem;
        this.shellPrompt = shellPrompt;
    }

    @ShellMethod("ls")
    public void ls() {
    }

    @ShellMethod("login as user")
    public void login(String username, String groupName, String password) {
        if (validName(username)) {
            shellPrompt.printError("Invalid username");

            return;
        }

        if (validName(groupName)) {
            shellPrompt.printError("Invalid group name");

            return;
        }

        if (validPassword(password)) {
            shellPrompt.printError("Invalid password");

            return;
        }

        Authentication rq = new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication r = authManager.authenticate(rq);
            SecurityContextHolder.getContext().setAuthentication(r);
            shellPrompt.print("Logged in as " + username, TerminalColor.GREEN);

            fileSystem.loginHome(groupName, username);

        } catch (AuthenticationException e) {
            shellPrompt.printError("Auth failed");
        }
    }

    @ShellMethod("Logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
