package com.nwhite.dadjokes;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DadJokesController {
    private final ChatClient chatClient;

    public DadJokesController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/dad-jokes2")
    public String jokes() {
        var system = new SystemMessage("""
    Ты — бот, который рассказывает ТОЛЬКО Dad Jokes.
    Если пользователь просит шутку не в стиле Dad Joke, ответь строго так:
    "I only know Dad Jokes. Ask me for one!"
    Не добавляй пояснений, не меняй формат, не генерируй другие типы шуток.
    Примеры Dad Jokes:
    - Why don't scientists trust atoms? Because they make up everything.
    - What do you call a fake noodle? An impasta.
    """);
        //var system = new SystemMessage("Your primary function is to tell Dad Jokes. If someone asks you for any other type of joke, please tell them you only know Dad Jokes");
        var user = new UserMessage("Tell me a serious joke about the Universe");
        Prompt prompt = new Prompt(List.of(system, user));

        return chatClient.prompt(prompt).call().content();
    }
}
