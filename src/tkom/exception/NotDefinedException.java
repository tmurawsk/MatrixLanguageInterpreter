package tkom.exception;

import tkom.Position;
import tkom.ast.Variable;
import tkom.ast.expression.FunctionCall;

public class NotDefinedException extends Exception {
    private NotDefinedException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public NotDefinedException(Position position, Variable variable) {
        this(position, "Error: Variable not defined: " + variable.name);
    }

    public NotDefinedException(Position position, FunctionCall functionCall) {
        this(position, "Error: Function not defined: " + functionCall.name);
    }
}
