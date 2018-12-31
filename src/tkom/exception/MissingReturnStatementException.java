package tkom.exception;

import tkom.Position;
import tkom.ast.FunctionDef;

public class MissingReturnStatementException extends Exception {
    private MissingReturnStatementException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public MissingReturnStatementException(Position position, FunctionDef functionDef) {
        this(position, "Error: Missing return statement in function " + functionDef.name);
    }
}
