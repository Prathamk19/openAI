package com.openai.ai.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebPageReaderService {

    public String readWebPage(String url) {
        try {
            // Fetch the web page content
            Document document = Jsoup.connect(url).get();

            // Find the main content element based on your page structure
            Element mainContent = document.select("main").first();
            if (mainContent == null) {
                // If "main" element not found, try selecting the body content
                mainContent = document.body();
            }
            // Extract the text from the main content element
            return mainContent.text();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading web page: " + e.getMessage();
        }
    }
}
