package com.nwhite.dadjokes;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/dad-jokes")
    public String generate(@RequestParam(value = "topic", defaultValue = "Dogs") String topic) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a dad joke about {topic}");
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));

        return chatClient.prompt(prompt).call().content();
    }
}
