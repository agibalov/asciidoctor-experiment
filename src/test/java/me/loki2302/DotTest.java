package me.loki2302;

import me.loki2302.testtools.Documentation;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class DotTest {
    @Rule
    public final Documentation documentation = new Documentation(System.getProperty("snippetsDir"));

    @Test
    public void generateDummyGraph() throws IOException {
        documentation.dummyDotGraph("dummyGraph");
    }

    @Test
    public void generateRandomGraph() throws IOException {
        documentation.randomDotGraph("randomGraph");
    }
}
