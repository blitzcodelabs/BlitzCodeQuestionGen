package Format;

public class QuestionPrompter {
    public String createPrompt(String targetLang, String srcLang, String topic){
        var prompt = "Create 10 simple questions asking to translate a line of " + srcLang + "language code to " + targetLang + "language code, with code ONLY on the topic of " + topic + ". Include 3 wrong answers. Don't include the question itself in the 'question' key value.  Format the response as JSON in the shape of \n" +
                "[{\n" +
                "  \"question\": \"some code here\",\n" +
                "  \"answer\": \"some code here\",\n" +
                "  \"wrongAnswers\": [\"wrong code here\", \"wrong code here\", \"wrong code here\"]\n" +
                "}];";
        return prompt;
    }
}
