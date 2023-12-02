package Generator;
import Format.QuestionPrompter;
import Generator.ChatResponse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class QuestionDistributor {

    public void distribute(String filePath, String targetLang, String srcLang, String topic){
        ChatResponse gen = new ChatResponse();
        QuestionPrompter prompter = new QuestionPrompter();
        String prompt = prompter.createPrompt(targetLang, srcLang, topic);
        String questions = gen.getQuestion(prompt);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(questions);
        } catch (IOException e) {
            System.out.println("Error writing JSON to the file: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        QuestionDistributor distributor = new QuestionDistributor();
        distributor.distribute("src/main/data/JavaToPython/modules/Essentials/Printing.json", "python", "java", "printing statements");
        //distributor.distribute("src/main/data/PythonToJava/modules/Essentials/Printing.json", "java", "python", "printing statements");
    }

}
