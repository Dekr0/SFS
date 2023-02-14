package com.ece.sfs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.*;

@Configuration
@ComponentScan(basePackageClasses = Directory.class)
public class SFSConfig {

    @Bean
    @Scope("singleton")
    public Directory root() {
        Directory root = new Directory(
                "/",
                new AccessRightList(new HashMap<>()),
                null,
                Calendar.getInstance().getTime(),
                UUID.randomUUID(),
                new HashMap<>()
        );

        root.addComponent(
                new Directory(
                        "home",
                        new AccessRightList(new HashMap<>()),
                        root,
                        Calendar.getInstance().getTime(),
                        UUID.randomUUID(),
                        new HashMap<>()
                )
        );

        return root;
    }
}
