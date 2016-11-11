package me.loki2302;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.DocinfoProcessor;

import java.util.Map;

public class DummyDocinfoProcessor extends DocinfoProcessor {
    public DummyDocinfoProcessor() {
    }

    public DummyDocinfoProcessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public String process(Document document) {
        // this gets added to html -> head
        return "<meta name=\"dummy\" content=\"hello\">";
    }
}
