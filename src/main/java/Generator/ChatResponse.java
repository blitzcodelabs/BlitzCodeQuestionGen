package Generator;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class ChatResponse {
    public String getQuestion(String prompt){
        var service = new OpenAiService("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", Duration.ofMinutes(1));
        List<ChatMessage> messages = new ArrayList<>();
        var message = new ChatMessage("user", prompt);
        messages.add(message);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(messages).model("gpt-3.5-turbo").build();

        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }
}
