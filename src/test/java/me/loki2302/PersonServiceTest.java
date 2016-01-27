package me.loki2302;

import me.loki2302.testtools.Documentation;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class PersonServiceTest {
    @Rule
    public final Documentation documentation = new Documentation(System.getProperty("snippetsDir"));

    @Test
    public void personServiceShouldReturnPerson() {
        // tag::example[]
        DummyPersonService personService = new DummyPersonService();
        Person person = personService.getPersonById("123");
        // end::example[]

        assertEquals("123", person.id);
        assertEquals("John Smith", person.name);

        documentation.snippet("person", new HashMap<String, Object>() {{
            put("id", person.id);
            put("name", person.name);
        }});
    }
}
