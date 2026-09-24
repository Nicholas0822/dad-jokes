package com.nwhite.dadjokes;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.*;

import com.nwhite.dadjokes.domain.Author;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private final ChatClient chatClient;

    public BookController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(value = "author", defaultValue = "Ken Kousen") String author) {
        String promptText = """
                Generate a list of books written by {author}.
                """;

        return chatClient.prompt()
                .user(u -> u.text(promptText)
                        .param("author", author))
                .call()
                .entity(Author.class);
    }

    // http://localhost:8080/books/author/benjamin%20graham
    @GetMapping("/author/{author}")
    public Map<String, Object> getAuthorsSocialLinks(@PathVariable String author) {
        String promptMessage = """
                Generate a list of links for the author {author}. 
                Return ONLY a JSON object with the author's name as the key 
                and a map of social platforms to URLs as the value.
                Example: {"AuthorName": {"Twitter": "https://...", "Instagram": "https://..."}}
                DO NOT include markdown, backticks, or explanations.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(promptMessage);
        Prompt prompt = promptTemplate.create(Map.of("author", author));

        String rawResponse = chatClient.prompt(prompt).call().content();

        MapOutputConverter converter = new MapOutputConverter();
        return converter.convert(rawResponse);
    }
}
