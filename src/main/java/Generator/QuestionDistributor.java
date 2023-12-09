package Generator;

import Format.QuestionPrompter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class QuestionDistributor {

    public void distribute(String filePath, String targetLang, String srcLang, String topic){
        var gen = new ChatResponse();
        var prompter = new QuestionPrompter();
        String prompt = prompter.createPrompt(targetLang, srcLang, topic);
        String questions = gen.getQuestion(prompt);

        try {
            var path = Path.of(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, questions);
        } catch (IOException e) {
            System.err.println("Error writing JSON to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        var distributor = new QuestionDistributor();
        distributor.distribute("src/main/data/JavaToPython/modules/Data Structures I/Stacks.json", "python", "java", "stacks data structures");
        distributor.distribute("src/main/data/PythonToJava/modules/Data Structures I/Stacks.json", "java", "python", "stacks data structures");
    }

}
