package me.loki2302;

import org.asciidoctor.ast.DocumentRuby;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DummyIncludeProcessor extends IncludeProcessor {
    private static final String DUMMY_URL_PROTOCOL_PREFIX = "dummy://";

    public DummyIncludeProcessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public boolean handles(String target) {
        System.out.println(target);
        return target.startsWith(DUMMY_URL_PROTOCOL_PREFIX);
    }

    @Override
    public void process(
            DocumentRuby document,
            PreprocessorReader reader,
            String target,
            Map<String, Object> attributes) {

        String glob = target.substring(DUMMY_URL_PROTOCOL_PREFIX.length());
        List<Path> filePaths;
        try {
            filePaths = findFiles(Paths.get(""), glob);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(Path filePath : filePaths) {
            String content;
            try {
                byte[] fileBytes = Files.readAllBytes(filePath);
                content = new String(fileBytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            reader.push_include(content, filePath.toString(), filePath.toString(), 1, attributes);
        }
    }

    private static List<Path> findFiles(Path location, String glob) throws IOException {
        List<Path> foundFilePaths = new ArrayList<>();

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
        Files.walkFileTree(location, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                if(pathMatcher.matches(path)) {
                    foundFilePaths.add(path);
                }

                return FileVisitResult.CONTINUE;
            }
        });

        return foundFilePaths;
    }
}
