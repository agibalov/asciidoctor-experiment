package me.loki2302;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.spi.ExtensionRegistry;

public class DummyExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(Asciidoctor asciidoctor) {
        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();
        javaExtensionRegistry.block("dummy-buildsrc", DummyBlockProcessor.class);
        javaExtensionRegistry.docinfoProcessor(DummyDocinfoProcessor.class);
        javaExtensionRegistry.includeProcessor(DummyIncludeProcessor.class);

        javaExtensionRegistry.block("pie", Nvd3BlockProcessor.class);
        javaExtensionRegistry.docinfoProcessor(Nvd3DocinfoProcessor.class);
    }
}
