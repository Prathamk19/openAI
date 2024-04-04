package com.openai.ai.controller;

import com.openai.ai.WebPageChatbot;
import com.openai.ai.model.QueryResponse;
import com.openai.ai.model.WebPageQuery;
import com.openai.ai.model.WebPageRequest;
import com.openai.ai.service.WebPageReaderService;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class WebPageController {

    @Autowired
    private WebPageReaderService webPageReaderService;
    @Autowired
    private WebPageChatbot chatbot;

    @Autowired
    private VectorStore vectorStore;

    @PostMapping("/read")
    public String readWebPage(@RequestBody WebPageRequest request) {
        String url = request.getUrl();
        saveToAIModel(url, webPageReaderService.readWebPage(url));
        return ResponseEntity.accepted().toString();
    }

    private void saveToAIModel(String url, String content) {
        List<Document> documentList = new ArrayList<>();
        for (String chunks : splitIntoChunks(content, 400)) {
            documentList.add(new Document(chunks));
        }
        var textSplitter = new TokenTextSplitter();
        textSplitter.split(content, 200);
        for (Document document : documentList) {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            vectorStore.add(List.of(document));
        }
    }

    @GetMapping("/query")
    public QueryResponse queryToChatbot(@RequestBody WebPageQuery webPageQuery) {
        String response = chatbot.chat(webPageQuery.getQuery());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setAnswer(response);
        System.out.println(Map.of("response", response));
        return queryResponse;
    }

    public List<String> splitIntoChunks(String text, int targetWordCount) {
        List<String> chunks = new ArrayList<>();

        // Create a BreakIterator to iterate over sentences
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);

        int start = iterator.first();
        int end = iterator.next();
        int wordCount = 0;
        StringBuilder chunk = new StringBuilder();

        while (end != BreakIterator.DONE) {
            String sentence = text.substring(start, end);
            int sentenceWordCount = countWords(sentence);

            // Check if adding the sentence to the chunk exceeds the target word count
            if (wordCount + sentenceWordCount <= targetWordCount) {
                // Add the sentence to the chunk
                chunk.append(sentence);
                wordCount += sentenceWordCount;

                // Check if the sentence ends with a full stop
                if (sentence.trim().endsWith(".")) {
                    // If yes, add the chunk to the list and reset
                    chunks.add(chunk.toString().trim());
                    chunk.setLength(0);
                    wordCount = 0;
                } else {
                    // If no full stop, add space to separate from next sentence
                    chunk.append(" ");
                }
            } else {
                // Add the current chunk to the list and reset
                chunks.add(chunk.toString().trim());
                chunk.setLength(0);
                wordCount = 0;
            }

            start = end;
            end = iterator.next();
        }

        // Add the last chunk if not empty
        if (!chunk.isEmpty()) {
            chunks.add(chunk.toString().trim());
        }
        return chunks;
    }

    private int countWords(String sentence) {
        return sentence.split("\\s+").length;
    }
}
