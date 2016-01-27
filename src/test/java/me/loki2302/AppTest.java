package me.loki2302;

import me.loki2302.testtools.Documentation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @Rule
    public final Documentation documentation = new Documentation(System.getProperty("snippetsDir"));

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void helloWorldShouldSayHelloWorld() {
        App.main(new String[] {});

        assertEquals("Hello World!\n", systemOutRule.getLog());
        documentation.text("consoleOutput", systemOutRule.getLog());
    }
}
