package me.loki2302;

public class DummyPersonService implements PersonService {
    @Override
    public Person getPersonById(String id) {
        Person person = new Person();
        person.id = id;
        person.name = "John Smith";
        return person;
    }
}
