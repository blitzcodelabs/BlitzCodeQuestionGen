package Format;

public class QuestionPrompter {
    public String createPrompt(String targetLang, String srcLang, String topic){
        String prompt = "Create 5 questions asking to translate a line of " + srcLang + "language code to " + targetLang + "language, with code on the topic of " + topic + ". Include wrong answers. Format the response as JSON in the shape of \n" +
                "[{\n" +
                "  \"question\": \"What is the translation: int x = 5; \",\n" +
                "  \"answer\": \"x = 5\",\n" +
                "  \"wrongAnswers\": [\"let x = 5\", \"var x = 5 \", \"integer = 5;\"]\n" +
                "}];";
        return prompt;
    }
}
