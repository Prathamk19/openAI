package com.openai.ai.controller;

import com.openai.ai.WebPageChatbot;
import com.openai.ai.model.QueryResponse;
import com.openai.ai.model.WebPageQuery;
import com.openai.ai.model.WebPageRequest;
import com.openai.ai.service.WebPageReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class WebPageController {

    private static final Logger LOG = LoggerFactory.getLogger(WebPageController.class);
    @Autowired
    private WebPageReaderService webPageReaderService;
    @Autowired
    private WebPageChatbot chatbot;
    @Autowired
    private VectorStore vectorStore;

    @PostMapping("/urls")
    public String readWebPage(@RequestBody WebPageRequest request) {
        String url = request.getUrl();
        createAndStoreEmbeddings(url, webPageReaderService.readWebPage(url));
        LOG.info("Completed URL operation.");
        return ResponseEntity.accepted().toString();
    }

    @PostMapping("/pdfs")
    public String readPdfs(@Value("${pdf.dir.path}") String pdfFilePath) {
        readFilesAndStoreEmbeddings(pdfFilePath);
        LOG.info("Completed PDF operation.");
        return ResponseEntity.accepted().toString();
    }

    @GetMapping("/query")
    public String queryToChatbot(@RequestBody WebPageQuery webPageQuery) {
        String response = chatbot.chat(webPageQuery.getQuery());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setAnswer(response);
        LOG.info("Received : {}", Map.of("response", response));
        return queryResponse.getAnswer();
    }

    private void createAndStoreEmbeddings(String url, String content) {
        List<Document> documentList = new ArrayList<>();
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        // Default chunk size is defined to 800 in TokenTextSplitter
        for (String chunks : textSplitter.split(content, 800)) {
            documentList.add(new Document(chunks));
        }
        vectorStore.add(List.of(new Document(url + " associated link for reference")));
        for (Document document : documentList) {
            vectorStore.add(List.of(document));
        }
    }

    private void readFilesAndStoreEmbeddings(String pdfFilePath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(pdfFilePath), "*.pdf")) {
            for (Path filePath : directoryStream) {
                LOG.info("Reading file {}", filePath);
                Resource pdfResource = new FileSystemResource(filePath);
                PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(3)
                                .build())
                        .withPagesPerDocument(1)
                        .build();

                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
                TokenTextSplitter textSplitter = new TokenTextSplitter();
                var docs = textSplitter.apply(pdfReader.get());
                vectorStore.accept(docs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
