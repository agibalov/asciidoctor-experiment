package me.loki2302.stuff.handlers;

public class AddOperationHandler implements OperationHandler {
    @Override
    public int execute(int a, int b) {
        return a + b;
    }
}
