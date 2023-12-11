package Generator;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * This class implements a request to OpenAI's API.
 */
public class ChatResponse {
    private final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    /**
     * returns response from OpenAPI API with given prompt.
     */
    public String getQuestion(String prompt){
        var service = new OpenAiService(OPENAI_API_KEY, Duration.ofMinutes(3));
        List<ChatMessage> messages = new ArrayList<>();
        var message = new ChatMessage("user", prompt);
        messages.add(message);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages).model("gpt-3.5-turbo").build();

        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }
}

