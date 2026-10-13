package com.demo.springairag.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;

@Component
public class TextDocumentReader {

    public List<Document> read(String filePath) {
        Resource resource = new ClassPathResource(filePath);
        TextReader textReader = new TextReader(resource);

        textReader.getCustomMetadata().put("filename", filePath);
        return textReader.read();
    }
}
