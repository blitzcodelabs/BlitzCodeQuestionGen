package Format;
/**
 * This class creates a prompt taking in a series of arguments from the user.
 * These arguments help specialize the prompt to generate questions in the shape of JSON files.
 */
public class QuestionPrompter {
    /**
     * Creates a prompt using the user's desired target language, number of questions, source language and topic.
     */
    public String createPrompt(String targetLang, String numOfQuestions, String srcLang, String topic){
        return "Create " + numOfQuestions + " simple questions asking to translate a line of " + srcLang + " language code to " + targetLang + " language code, with code ONLY on the topic of " + topic +
                """
                . Include 3 wrong answers. Don't include the question itself in the 'question' key value. Don't include: "Java code:" and "Python code:" in the responses. Use quotation marks when necessary. Format the response as JSON in the shape of
                [{
                  "question": "code here",
                  "answer": "code here",
                  "wrongAnswers": ["wrong code here", "wrong code here", "wrong code here"]
                }];""";
    }
}
