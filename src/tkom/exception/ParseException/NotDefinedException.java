package tkom.exception.ParseException;

import tkom.Position;
import tkom.ast.Variable;
import tkom.ast.FunctionCall;

public class NotDefinedException extends ParseException {
    private NotDefinedException(Position position, String subjectType, String subjectName) {
        super(position, subjectType + " not defined: " + subjectName);
    }

    public NotDefinedException(Position position, Variable variable) {
        this(position, "Variable", variable.name);
    }

    public NotDefinedException(Position position, FunctionCall functionCall) {
        this(position, "Function", functionCall.name);
    }
}
