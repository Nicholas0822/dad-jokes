package com.nwhite.dadjokes;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/olympics")
public class OlympicController {
    private static final Logger log = LoggerFactory.getLogger(OlympicController.class);
    private final ChatClient chatClient;

    @Value("classpath:/prompts/olympic-sports.st")
    private Resource olympicSportsResource;
    @Value("classpath:/docs/olympic-sports.txt")
    private Resource docsToStuffResource;

    public OlympicController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // http://localhost:8080/olympics
    // http://localhost:8080/olympics/2024?stuffit=true
    @GetMapping("/2024")
    public String get2024OlympicSports(
            @RequestParam(value = "message", defaultValue = "What sports are being included in the 2024 Summer Olympics?") String message,
            @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit
    ) throws IOException {
        // TODO: add global exception handler
        String sports = docsToStuffResource.getContentAsString(Charset.defaultCharset());
        log.info("Sports: {}", sports);

        String template = "";
        template = olympicSportsResource.getContentAsString(Charset.defaultCharset());

        String promptText = template
                .replace("{question}", message)
                .replace("{context}", stuffit ? sports : "");
        return chatClient.prompt()
                .user(promptText)
                .call()
                .content();
    }
}
