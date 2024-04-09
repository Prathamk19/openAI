package com.openai.ai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WebPageChatbot {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    WebPageChatbot(ChatClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    public String chat(String message) {
        List<Document> listOfSimilarDocuments = this.vectorStore.similaritySearch(message);
        String documents = listOfSimilarDocuments
                .stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        String template = """
                            
                You're assisting with questions about various web pages, text files and pdfs provided by the User and contents are stored in the database.
                
                If you are unsure, simply state that you don't know.
                            
                DOCUMENTS:
                {documents}
                            
                """;
        Message systemMessage = new SystemPromptTemplate(template)
                .createMessage(Map.of("documents", documents));
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse aiResponse = aiClient.call(prompt);
        return aiResponse.getResult().getOutput().getContent();
    }
}
