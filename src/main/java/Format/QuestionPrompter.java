package Format;

public class QuestionPrompter {
    public String createPrompt(String targetLang, String srcLang, String topic){
        var prompt =
                "Create 10 questions asking to translate a line of " + srcLang + " language code to " + targetLang + " language, with code ONLY on the topic of " + topic +
                """
                . Include wrong answers. Format the response as JSON in the shape of
                [{
                  "question": "What is the translation: int x = 5; ",
                  "answer": "x = 5",
                  "wrongAnswers": ["let x = 5", "var x = 5 ", "integer = 5;"]
                }];""";
        return prompt;
    }
}
