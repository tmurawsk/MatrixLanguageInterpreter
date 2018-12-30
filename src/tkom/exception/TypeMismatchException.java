package tkom.exception;

import tkom.Position;
import tkom.Token;
import tkom.ast.FunctionDef;
import tkom.ast.Variable;

public class TypeMismatchException extends Exception {
    private TypeMismatchException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public TypeMismatchException(Position position, FunctionDef functionDef) {
        this(position, "Error: Function return type mismatch. Expected: " + Token.getNameByToken(functionDef.returnType));
    }

    public TypeMismatchException(Position position, Variable variable) {
        this(position, "Error: Type mismatch. Expected: " + Token.getNameByToken(variable.type));
    }
}
