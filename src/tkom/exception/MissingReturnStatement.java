package tkom.exception;

import tkom.Position;
import tkom.ast.FunctionDef;

public class MissingReturnStatement extends Exception {
    private MissingReturnStatement(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public MissingReturnStatement(Position position, FunctionDef functionDef) {
        this(position, "Error: Missing return statement in function " + functionDef.name);
    }
}
