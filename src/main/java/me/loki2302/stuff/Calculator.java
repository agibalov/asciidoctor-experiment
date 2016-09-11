package me.loki2302.stuff;

import me.loki2302.stuff.handlers.OperationHandler;

public class Calculator {
    private final OperationHandler addOperationHandler;
    private final OperationHandler subOperationHandler;

    public Calculator(
            OperationHandler addOperationHandler,
            OperationHandler subOperationHandler) {

        this.addOperationHandler = addOperationHandler;
        this.subOperationHandler = subOperationHandler;
    }

    public int add(int a, int b) {
        return addOperationHandler.execute(a, b);
    }

    public int sub(int a, int b) {
        return subOperationHandler.execute(a, b);
    }
}
