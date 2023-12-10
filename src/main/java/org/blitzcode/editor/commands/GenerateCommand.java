package org.blitzcode.editor.commands;

import Generator.QuestionDistributor;
import org.blitzcode.editor.helper.InputReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GenerateCommand {
    private final InputReader inputReader;
    @Autowired
    public GenerateCommand(InputReader inputReader) {
        this.inputReader = inputReader;
    }
    @ShellMethod(key = "generate")
    public void generateAQuestion(){
        String numOfQuestions = this.inputReader.prompt("How many questions would you like to generate?");
        String srcLang = this.inputReader.prompt("What is the source language?");
        String targetLang = this.inputReader.prompt("What is the target language?");
        String module = this.inputReader.prompt("Which module does your question go in?");
        String lesson = this.inputReader.prompt("Which lesson does your question go in?");

        QuestionDistributor distributor = new QuestionDistributor();
        System.out.println("Generating question... Please wait.");
        distributor.distribute("src/main/data/adminQuestions/adminQuestion.json", targetLang, srcLang, lesson);
        System.out.println("Successfully generated question(s). Check file path.");

    }
}
