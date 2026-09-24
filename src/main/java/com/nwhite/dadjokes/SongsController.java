package com.nwhite.dadjokes;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SongsController {
    private final ChatClient chatClient;

    public SongsController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

//    @GetMapping("/songs")
//    public String getSongsByArtist(@RequestParam(value = "artist", defaultValue = "Taylor Swift") String artist) {
//        var message = """
//                Please, give me a list of top 10 songs for the artist {artist}. If you don't know the answer, just say "I don't know".
//                """;
//        PromptTemplate promptTemplate = new PromptTemplate(message);
//        Prompt prompt = promptTemplate.create(Map.of("artist", artist));
//        return chatClient.prompt(prompt).call().content();
//    }

    @GetMapping("/songs")
    public List<String> getSongsByArtist(@RequestParam(value = "artist", defaultValue = "Taylor Swift") String artist) {
        var message = """
                Return a JSON array of exactly 10 song titles for {artist}.\s
                                    Format: ["Song 1", "Song 2", ...].\s
                                    NO markdown, NO explanations, NO extra quotes.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("artist", artist));

        String response = chatClient.prompt(prompt).call().content();

        ListOutputConverter outputConverter = new ListOutputConverter();
        return outputConverter.convert(response);
    }
}
