package org.blitzcode.editor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan("org.blitzcode.editor.commands")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
