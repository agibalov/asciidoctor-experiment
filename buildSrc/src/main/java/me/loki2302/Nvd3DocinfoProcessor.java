package me.loki2302;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.DocinfoProcessor;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Nvd3DocinfoProcessor extends DocinfoProcessor {
    public Nvd3DocinfoProcessor() {
    }

    public Nvd3DocinfoProcessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public String process(Document document) {
        URL url = Resources.getResource("nvd3-head.html");
        String content;
        try {
            content = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
