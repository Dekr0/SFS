package com.ece.sfs.injection;


import com.ece.sfs.prompt.ShellPrompt;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringShellConfig {

    @Bean
    public ShellPrompt shellPrompt() throws Exception {
        return new ShellPrompt(TerminalBuilder.builder().system(false).build());
    }
}
