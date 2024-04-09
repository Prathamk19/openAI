package com.openai.ai;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class WebPageAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebPageAiApplication.class, args);
	}

	@Bean
	ApplicationRunner webPageAiApp(
			VectorStore vectorStoreAI,
			JdbcTemplate jdbcTemplate) {
		return args -> {
			init(vectorStoreAI, jdbcTemplate);
		};
	}

	@Bean
	VectorStore vectorStoreAI(EmbeddingClient ec,
							  JdbcTemplate t) {
		return new PgVectorStore(t, ec);
	}

	@Bean
	TokenTextSplitter tokenTextSplitter() {
		return new TokenTextSplitter();
	}

	static void init(VectorStore vectorStore, JdbcTemplate template)
			throws Exception {
		template.update("delete from vector_store");
	}
}


