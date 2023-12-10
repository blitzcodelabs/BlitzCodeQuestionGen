package org.blitzcode.editor.commands;

import Generator.QuestionDistributor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.blitzcode.editor.controller.ModuleController;
import org.blitzcode.editor.helper.InputReader;
import org.blitzcode.editor.model.Language;
import org.blitzcode.editor.model.Lesson;
import org.blitzcode.editor.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.blitzcode.editor.model.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ShellComponent
public class GenerateCommand {
    private final InputReader inputReader;
    @Autowired
    public GenerateCommand(InputReader inputReader) {
        this.inputReader = inputReader;
    }

    @Autowired
    private ModuleController moduleController;

    @ShellMethod(key = "generate")
    public void generateAQuestion() {
        String numOfQuestions = this.inputReader.prompt("How many questions would you like to generate?");
        String srcLang = this.inputReader.prompt("What is the source language?");
        String targetLang = this.inputReader.prompt("What is the target language?");
        String module = this.inputReader.prompt("Which module does your question go in?");
        String lesson = this.inputReader.prompt("Which lesson does your question go in?");
        String upload = this.inputReader.prompt("Do you want to save the questions to the database now? y/n");

        QuestionDistributor distributor = new QuestionDistributor();
        System.out.println("Generating question... Please wait.");
        Optional<String> questions = distributor.distribute("src/main/data/adminQuestions/adminQuestion.json", numOfQuestions, targetLang, srcLang, lesson);
        if(upload.equals("y")){
            if(questions.isPresent()){
                try {
                    createDatabaseEntry(module, lesson, questions.get(), srcLang, targetLang);
                } catch (JsonProcessingException e) {
                    System.out.println("Failed to generate questions! " + e.getMessage());
                    return;
                }
            }else{
                System.out.println("Failed to generate questions!");
                return;
            }
        }
        System.out.println("Successfully generated question(s). Check file path.");
    }

    private void createDatabaseEntry(String module, String lesson, String questions, String base, String target) throws JsonProcessingException {
        Module existingModule = moduleController.findModuleByName(module);

        Lesson existingLesson = moduleController.findLessonByName(lesson);
        Lesson newLesson;
        if(existingLesson == null){
            newLesson = new Lesson();
        }else{
            newLesson = existingLesson;
        }
        newLesson.setPoints(1);
        newLesson.setName(lesson);

        ObjectMapper mapper = new ObjectMapper();
        List<QuestionItem> quizItems = mapper.readValue(questions, new TypeReference<List<QuestionItem>>() {});
        ArrayList<Question> questionList = new ArrayList<>();

        // Now you can use the quizItems list
        for (QuestionItem q: quizItems) {
            Question question = new Question();
            question.setBaseLanguage(Language.valueOf(base));
            question.setTargetLanguage(Language.valueOf(target));
            question.setText(q.question);
            question.setLesson(newLesson);
            question.setCorrectAnswer(q.answer);
            question.setWrongOptions(q.wrongAnswers);
            questionList.add(question);
        }

        if(newLesson.getQuestions() != null){
            List<Question> questionsByLessonId = moduleController.lessonRepository.findQuestionsByLessonId(newLesson.getId());
            questionsByLessonId.addAll(questionList);
        }else{
            newLesson.setQuestions(questionList);
        }

        if(existingModule == null){
            Module newModule = new Module();
            newModule.setName(module);
            newLesson.setModule(newModule);
            newModule.setLessons(List.of(newLesson));
            moduleController.moduleRepository.save(newModule);
        }else{
            if(!existingModule.getLessons().contains(newLesson)){
                existingModule.getLessons().add(newLesson);
            }
            moduleController.moduleRepository.save(existingModule);
        }
    }

    private record QuestionItem(String question, String answer, List<String> wrongAnswers) {}
}
