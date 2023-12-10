package org.blitzcode.editor;

import org.blitzcode.editor.helper.InputReader;
import org.jline.reader.LineReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan("org.blitzcode.editor.commands")
public class Main {
    @Bean
    public InputReader inputReader(@Lazy LineReader lineReader) {
        return new InputReader(lineReader);
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
