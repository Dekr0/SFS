package com.ece.sfs.command;

import com.ece.sfs.prompt.ShellPrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

import static com.ece.sfs.Util.validName;
import static com.ece.sfs.Util.validPassword;


@ShellComponent
public class AuthCommand {

    private final UserDetailsManager userManager;

    private final PasswordEncoder passwordEncoder;

    private final GrantedAuthority userAuthority;

    private final ShellPrompt shellPrompt;

    @Autowired
    public AuthCommand(
            UserDetailsManager userManager,
            PasswordEncoder passwordEncoder,
            GrantedAuthority userAuthority,
            ShellPrompt shellPrompt

    ) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
        this.userAuthority = userAuthority;
        this.shellPrompt = shellPrompt;
    }

    @ShellMethod("Sign up")
    public void signup(String username, String password) {
        if (validName(username) || validPassword(password)) {
            shellPrompt.printError("Invalid username or password");

            return;
        }

        if (validPassword(password)) {
            shellPrompt.printError(
                    """
                    Password must contain at least one digit [0-9].
                    Password must contain at least one lowercase Latin character [a-z].
                    Password must contain at least one uppercase Latin character [A-Z].
                    Password must contain at least one special character like ! @ # & ( ).
                    Password must contain a length of at least 8 characters and a maximum of 32 characters.
                    """
            );
        }

        if (userManager.userExists(username)) {
            shellPrompt.printError("User already exists");

            return;
        }

        userManager.createUser(new User(
                username,
                passwordEncoder.encode(password),
                List.of(userAuthority)
        ));
    }

}
