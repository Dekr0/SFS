package com.ece.sfs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Configuration
@ComponentScan(basePackageClasses = Directory.class)
public class SFSConfig {

    @Bean
    @Scope("singleton")
    public Directory root() {
        Directory root = new Directory(
                "/",
                null,
                Calendar.getInstance().getTime(),
                UUID.randomUUID(),
                new HashMap<>()
        );

        root.addComponent(
                new Directory(
                        "home",
                        root,
                        Calendar.getInstance().getTime(),
                        UUID.randomUUID(),
                        new HashMap<>()
                )
        );

        return root;
    }

    @Bean
    @Scope("singleton")
    @Qualifier("INTERNAL")
    public GrantedAuthority INTERNAL() {
        return new SimpleGrantedAuthority("INTERNAL");
    }


    @Bean
    @Scope("singleton")
    @Qualifier("EXTERNAL")
    public GrantedAuthority EXTERNAL() {
        return new SimpleGrantedAuthority("EXTERNAL");
    }

    @Bean
    @Scope("singleton")
    public UserGroupManager userGroupManager() {
        UserGroupManager manager = new UserGroupManager(new ArrayList<>());

        manager.createGroup("Users", Collections.singletonList(INTERNAL()));
        manager.createGroup("Admins", Collections.singletonList(INTERNAL()));

        return manager;
    }

    @Bean
    @Scope("singleton")
    public AccessManager accessRightListManager(Directory root, UserGroupManager userGroupManager) {
        AccessManager manager = new AccessManager(userGroupManager);

        manager.addAccessRightList(
                root.getUUID(),
                new AccessRightList(
                        AccessRight.defaultAR("root"),
                        AccessRight.defaultRead("Admins")
                )
        );

        manager.addAccessRightList(
                root.getComponent("home").getUUID(),
                new AccessRightList(
                        AccessRight.defaultAR("root"),
                        AccessRight.defaultRead("Users")
                )
        );

        manager.addGroupAccessRight(
                "Admin",
                root.getComponent("home").getUUID(),
                AccessRight.defaultAR()
        );

        return manager;
    }

}
