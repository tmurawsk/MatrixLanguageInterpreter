package tkom.exception.ExecutionException;

import tkom.Position;
import tkom.Token;
import tkom.TokenID;
import tkom.ast.FunctionDef;
import tkom.ast.Program;
import tkom.ast.statement.ReturnStatement;

public class TypeMismatchException extends ExecutionException {
    private TypeMismatchException(Position position, String subject, String expectedType, String givenType) {
        super(position, subject + " mismatch.\n\t\texpected: " + expectedType + "\n\t\twas: " + givenType);
    }

    public TypeMismatchException(Position position, ReturnStatement returnStatement, TokenID givenType) {
        this(position, "Function return type", Token.getNameByToken(Program.popFunctionCall().returnType), Token.getNameByToken(givenType));
    }

    public TypeMismatchException(Position position, TokenID expectedType, TokenID givenType) {
        this(position, "Type", Token.getNameByToken(expectedType), Token.getNameByToken(givenType));
    }
}
