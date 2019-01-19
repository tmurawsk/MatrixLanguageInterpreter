package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.FunctionCall;
import tkom.ast.Variable;
import tkom.ast.VariableCall;
import tkom.exception.ExecutionException.ExecutionException;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private VariableCall variableCall;
    private FunctionCall functionCall;

    private BaseMathExpr(Position position, boolean isMinus) {
        super(position);
        this.isMinus = isMinus;
        this.type = TokenID.Any;
    }

    public BaseMathExpr(Position position, boolean isMinus, MathExpression expression) {
        this(position, isMinus);
        this.expression = expression;
    }

    public BaseMathExpr(Position position, boolean isMinus, VariableCall variableCall) {
        this(position, isMinus);
        this.variableCall = variableCall;
    }

    public BaseMathExpr(Position position, boolean isMinus, FunctionCall functionCall) {
        this(position, isMinus);
        this.functionCall = functionCall;
    }

    @Override
    TokenID evaluateType() {
        if (expression != null)
            return expression.getType();
        if (variableCall != null)
            return variableCall.getType();
        if (functionCall != null)
            return functionCall.getType();
        return TokenID.Invalid;
    }

    @Override
    public Variable evaluate() throws ExecutionException {
        if (expression != null)
            return expression.evaluate();
        if (variableCall != null)
            return variableCall.evaluate();
        return functionCall.evaluate();
    }
}
