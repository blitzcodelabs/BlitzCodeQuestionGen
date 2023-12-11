package Generator;

import Format.QuestionPrompter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
/**
 * This class handles distributing created JSON files to a specified directory
 */
public class QuestionDistributor {
    /**
     * Method uses arguments to create a prompt using QuestionPrompter.
     * Distributes the generated response to a created JSON file at specified destination.
     */
    public Optional<String> distribute(String filePath, String numOfQuestions, String targetLang, String srcLang, String topic){
        var gen = new ChatResponse();
        var prompter = new QuestionPrompter();
        String prompt = prompter.createPrompt(targetLang, numOfQuestions, srcLang, topic);
        String questions = gen.getQuestion(prompt);

        try {
            var path = Path.of(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, questions);
            return questions.describeConstable();
        } catch (IOException e) {
            System.err.println("Error writing JSON to the file: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public static void main(String[] args){
        var distributor = new QuestionDistributor();
        //distributor.distribute("src/main/data/JavaToPython/modules/Data Structures I/Stacks.json", 10, "python", "java", "stacks data structures");
        //distributor.distribute("src/main/data/PythonToJava/modules/Data Structures I/Stacks.json", 10, "java", "python", "stacks data structures");
    }

}
