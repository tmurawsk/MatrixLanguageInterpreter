package tkom.exception.ExecutionException;

import tkom.Position;
import tkom.Token;
import tkom.TokenID;
import tkom.ast.Program;
import tkom.ast.statement.ReturnStatement;

public class TypeMismatchException extends ExecutionException {
    private TypeMismatchException(Position position, String message) {
        super(position, message);
    }

    private TypeMismatchException(Position position, String subject, String expectedType, String givenType) {
        this(position, subject + " mismatch\n\t\texpected: " + expectedType + "\n\t\twas: " + givenType);
    }

    public TypeMismatchException(Position position, ReturnStatement returnStatement, TokenID givenType) {
        this(position, "Function return type", Token.getNameByToken(Program.popFunctionCall().returnType), Token.getNameByToken(givenType));
    }

    public TypeMismatchException(Position position, TokenID expectedType, TokenID givenType) {
        this(position, "Type", Token.getNameByToken(expectedType), Token.getNameByToken(givenType));
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return new TypeMismatchException(position, getMessage().split("\n", 2)[1]);
    }
}
