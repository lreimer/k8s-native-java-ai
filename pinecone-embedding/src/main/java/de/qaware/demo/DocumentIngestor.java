package de.qaware.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.pinecone.PineconeEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocumentIngestor {

    @Inject
    PineconeEmbeddingStore embeddingStore;

    @Inject
    EmbeddingModel embeddingModel;

    public void ingest(String text) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        Metadata metadata = Metadata.from(Map.of("category", "text", "date",currentDateTime));
        Document doc = Document.from(text, metadata);
        ingest(List.of(doc));
    }

    void ingest(List<Document> documents) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(DocumentSplitters.recursive(500, 0))
                .build();
        ingestor.ingest(documents);
    }
}
