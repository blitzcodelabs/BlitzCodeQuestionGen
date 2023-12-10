package org.blitzcode.editor.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blitzcode.editor.controller.ModuleController;
import org.blitzcode.editor.controller.UserController;
import org.blitzcode.editor.model.Language;
import org.blitzcode.editor.model.Lesson;
import org.blitzcode.editor.model.Module;
import org.blitzcode.editor.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.shell.command.annotation.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Command(command = "save")
public class SaveCommand {

    Logger logger = LoggerFactory.getLogger(SaveCommand.class);

    private UserController userController;

    private ResourceLoader resourceLoader;

    private ModuleController moduleController;

    public SaveCommand(ResourceLoader resourceLoader, ModuleController moduleController) {
        this.resourceLoader = resourceLoader;
        this.moduleController = moduleController;
    }

    @Command(command = "all")
    public String all(){
        return "Hi";
    }

    public void getQuestions() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            URL url = getClass().getResource("/Courses");
            Path path = Paths.get(url.toURI());
            Stream<Path> stream = Files.walk(path, 1);
            ArrayList<String> courses = new ArrayList<>();
            stream.filter(Files::isDirectory)
                    .map(p -> p.getFileName().toString())
                    .filter(filename -> filename.contains("To"))
                    .forEach(courses::add);

            for(String course: courses){
                ArrayList<String> modules = new ArrayList<>();
                url = getClass().getResource("/Courses/" + course + "/modules");
                path = Paths.get(url.toURI());
                stream = Files.walk(path, 1);
                stream.filter(Files::isDirectory)
                        .map(p -> p.getFileName().toString())
                        .filter(filename -> !filename.contains("modules"))
                        .forEach(modules::add);
                Language baseLanguage = Language.valueOf(course.split("To")[0].toUpperCase());
                Language targetLanguage = Language.valueOf(course.split("To")[1].toUpperCase());
                for(String module: modules){
                    ArrayList<String> lessons = new ArrayList<>();
                    url = getClass().getResource("/Courses/" + course + "/modules/" + module);
                    path = Paths.get(url.toURI());
                    stream = Files.walk(path, 1);
                    stream.filter(Files::isRegularFile)
                            .map(p -> p.getFileName().toString())
                            .filter(filename -> !filename.contains(module.toString()))
                            .forEach(lessons::add);
                    // Create a Module object for the database
                    Module newModule;
                    Module databaseModule = moduleController.findModuleByName(module.toString());
                    if (databaseModule != null) {
                        newModule = databaseModule;
                    }else{
                        newModule = new Module();
                        newModule.setName(module.toString());
                    }
                    ArrayList<Lesson> newLessons = new ArrayList<>();
                    for(String lesson: lessons){
                        Lesson newLesson;
                        String lessonName = lesson.split("\\.")[0];
                        Lesson databaseLesson = moduleController.findLessonByName(lessonName);
                        if(databaseModule == null){
                            newLesson = new Lesson();
                            newLesson.setName(lesson.split("\\.")[0]);
                            newLesson.setModule(newModule);
                            newLesson.setPoints(1);
                        }else{
                            newLesson = databaseLesson;
                        }

                        InputStream inputStream = getClass().getResourceAsStream("/Courses/" + course + "/modules/" + module + "/" + lesson);
                        if (inputStream == null) {
                            logger.error("\"Resource not found: /JavaToPython/modules/Essentials/Printing.json\"");
                        }
                        List<QuestionItem> quizItems = mapper.readValue(inputStream, new TypeReference<List<QuestionItem>>() {});

                        org.blitzcode.editor.model.ResponseTypes.Question[] questions = new org.blitzcode.editor.model.ResponseTypes.Question[quizItems.size()];
                        ArrayList<Question> questionDatabaseList = new ArrayList<>();

                        // Now you can use the quizItems list
                        for (int i = 0; i < questions.length; i++) {
                            QuestionItem item = quizItems.get(i);
                            int answerIndex = insertStringAtRandomIndex(item.wrongAnswers, item.answer);
                            String[] arr = item.wrongAnswers.toArray(new String[0]);
                            questions[i] = new org.blitzcode.editor.model.ResponseTypes.Question(item.question, answerIndex, arr);
                            org.blitzcode.editor.model.Question newQuestion = new org.blitzcode.editor.model.Question();
                            newQuestion.setText(item.question);
                            newQuestion.setBaseLanguage(baseLanguage);
                            newQuestion.setTargetLanguage(targetLanguage);
                            newQuestion.setCorrectAnswer(item.answer);
                            newQuestion.setWrongOptions(item.wrongAnswers);
                            newQuestion.setLesson(newLesson);
                            questionDatabaseList.add(newQuestion);
                        }
                        if(newLesson.getQuestions() != null){
                            questionDatabaseList.addAll(newLesson.getQuestions());
                        }
                        newLesson.setQuestions(questionDatabaseList);
                        newLessons.add(newLesson);
                    }
                    newModule.setLessons(newLessons);
                    moduleController.createModule(newModule);
                }
            }

//            Files.walk(path, 5).forEach(p -> logger.error(p.toString()));
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int insertStringAtRandomIndex(List<String> stringList, String stringToInsert) {
        Random random = new Random();
        int insertIndex = random.nextInt(stringList.size() + 1);

        stringList.add(insertIndex, stringToInsert);
        return insertIndex;
    }

    private record QuestionItem(String question, String answer, List<String> wrongAnswers) {}

    //    @GetMapping(path = "/updateJsonFiles")
//    public void getQuestions(JwtAuthenticationToken token) {
    // Commenting these out becuase they need more testing before making it available
//        ObjectMapper mapper = new ObjectMapper();
//        try{
//            URL url = getClass().getResource("/Courses");
//            Path path = Paths.get(url.toURI());
//            Stream<Path> stream = Files.walk(path, 1);
//            ArrayList<String> courses = new ArrayList<>();
//            stream.filter(Files::isDirectory)
//                    .map(p -> p.getFileName().toString())
//                    .filter(filename -> filename.contains("To"))
//                    .forEach(courses::add);
//
//            for(String course: courses){
//                ArrayList<String> modules = new ArrayList<>();
//                url = getClass().getResource("/Courses/" + course + "/modules");
//                path = Paths.get(url.toURI());
//                stream = Files.walk(path, 1);
//                stream.filter(Files::isDirectory)
//                        .map(p -> p.getFileName().toString())
//                        .filter(filename -> !filename.contains("modules"))
//                        .forEach(modules::add);
//                Language baseLanguage = Language.valueOf(course.split("To")[0].toUpperCase());
//                Language targetLanguage = Language.valueOf(course.split("To")[1].toUpperCase());
//                for(String module: modules){
//                    ArrayList<String> lessons = new ArrayList<>();
//                    url = getClass().getResource("/Courses/" + course + "/modules/" + module);
//                    path = Paths.get(url.toURI());
//                    stream = Files.walk(path, 1);
//                    stream.filter(Files::isRegularFile)
//                            .map(p -> p.getFileName().toString())
//                            .filter(filename -> !filename.contains(module.toString()))
//                            .forEach(lessons::add);
//                    // Create a Module object for the database
//                    Module newModule;
//                    Module databaseModule = moduleController.findModuleByName(module.toString());
//                    if (databaseModule != null) {
//                        newModule = databaseModule;
//                    }else{
//                        newModule = new Module();
//                        newModule.setName(module.toString());
//                    }
//                    ArrayList<Lesson> newLessons = new ArrayList<>();
//                    for(String lesson: lessons){
//                        Lesson newLesson;
//                        String lessonName = lesson.split("\\.")[0];
//                        Lesson databaseLesson = moduleController.findLessonByName(lessonName);
//                        if(databaseModule == null){
//                            newLesson = new Lesson();
//                            newLesson.setName(lesson.split("\\.")[0]);
//                            newLesson.setModule(newModule);
//                            newLesson.setPoints(1);
//                        }else{
//                            newLesson = databaseLesson;
//                        }
//
//                        InputStream inputStream = getClass().getResourceAsStream("/Courses/" + course + "/modules/" + module + "/" + lesson);
//                        if (inputStream == null) {
//                            throw new FileNotFoundException("Resource not found: /JavaToPython/modules/Essentials/Printing.json");
//                        }
//                        List<QuestionItem> quizItems = mapper.readValue(inputStream, new TypeReference<List<QuestionItem>>() {});
//
//                        Question[] questions = new Question[quizItems.size()];
//                        ArrayList<org.blitzcode.api.model.Question> questionDatabaseList = new ArrayList<>();
//
//                        // Now you can use the quizItems list
//                        for (int i = 0; i < questions.length; i++) {
//                            QuestionItem item = quizItems.get(i);
//                            int answerIndex = insertStringAtRandomIndex(item.wrongAnswers, item.answer);
//                            String[] arr = item.wrongAnswers.toArray(new String[0]);
//                            questions[i] = new Question(item.question, answerIndex, arr);
//                            org.blitzcode.api.model.Question newQuestion = new org.blitzcode.api.model.Question();
//                            newQuestion.setText(item.question);
//                            newQuestion.setBaseLanguage(baseLanguage);
//                            newQuestion.setTargetLanguage(targetLanguage);
//                            newQuestion.setCorrectAnswer(item.answer);
//                            newQuestion.setWrongOptions(item.getWrongAnswers());
//                            newQuestion.setLesson(newLesson);
//                            questionDatabaseList.add(newQuestion);
//                        }
//                        if(newLesson.getQuestions() != null){
//                            questionDatabaseList.addAll(newLesson.getQuestions());
//                        }
//                        newLesson.setQuestions(questionDatabaseList);
//                        newLessons.add(newLesson);
//                    }
//                    newModule.setLessons(newLessons);
//                    moduleController.createModule(newModule);
//                }
//            }
//
////            Files.walk(path, 5).forEach(p -> logger.error(p.toString()));
//        }catch (URISyntaxException e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
