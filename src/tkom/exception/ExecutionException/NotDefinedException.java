package tkom.exception.ExecutionException;

import tkom.Position;
import tkom.Token;
import tkom.ast.FunctionCall;
import tkom.ast.expression.MathExpr;

import java.util.LinkedList;

public class NotDefinedException extends ExecutionException {
    public NotDefinedException(Position position, FunctionCall functionCall) {
        super(position, "Function not defined: " + getArgumentTypesString(functionCall.getParameters()));
    }

    private static String getArgumentTypesString(LinkedList<MathExpr> arguments) {
        LinkedList<String> types = new LinkedList<>();
        for (MathExpr expr : arguments)
            types.add(Token.getNameByToken(expr.getType()));
        return String.join(", ", types);
    }
}
