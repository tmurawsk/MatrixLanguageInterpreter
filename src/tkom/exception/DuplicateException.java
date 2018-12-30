package tkom.exception;

import tkom.Position;
import tkom.Token;
import tkom.ast.FunctionDef;
import tkom.ast.Variable;

import java.util.LinkedList;

public class DuplicateException extends Exception {
    private DuplicateException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public DuplicateException(Position position, Variable variable) {
        this(position, "Error: Variable already defined: " + variable.name);
    }

    public DuplicateException(Position position, FunctionDef functionDef) {
        this(position,
                "Error: Function already defined: " + functionDef.name + "("
                        + getArgumentTypesString(functionDef.getArguments()) + ")"
        );
    }

    private static String getArgumentTypesString(LinkedList<Variable> arguments) {
        LinkedList<String> types = new LinkedList<>();
        for (Variable v : arguments)
            types.add(Token.getNameByToken(v.type));
        return String.join(", ", types);
    }
}
