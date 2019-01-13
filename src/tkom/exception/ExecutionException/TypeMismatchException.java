package tkom.exception.ExecutionException;

import tkom.Position;
import tkom.Token;
import tkom.TokenID;
import tkom.ast.FunctionDef;

public class TypeMismatchException extends ExecutionException {
    private TypeMismatchException(Position position, String subject, String expectedType, String givenType) {
        super(position, subject + " mismatch.\n\t\texpected: " + expectedType + "\n\t\twas: " + givenType);
    }

    public TypeMismatchException(Position position, FunctionDef functionDef, TokenID givenType) {
        this(position, "Function return type", Token.getNameByToken(functionDef.returnType), Token.getNameByToken(givenType));
    }

    public TypeMismatchException(Position position, TokenID expectedType, TokenID givenType) {
        this(position, "Type", Token.getNameByToken(expectedType), Token.getNameByToken(givenType));
    }
}
