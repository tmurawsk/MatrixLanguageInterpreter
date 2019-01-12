package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.FunctionCall;
import tkom.ast.Variable;
import tkom.ast.VariableCall;
import tkom.ast.statement.Statement;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private VariableCall variableCall;
    private FunctionCall functionCall;

    private BaseMathExpr(Statement parent, Position position, boolean isMinus) {
        super(parent, position);
        this.isMinus = isMinus;
        this.type = TokenID.Any;
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, MathExpression expression) {
        this(parent, position, isMinus);
        this.expression = expression;
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, VariableCall variableCall) {
        this(parent, position, isMinus);
        this.variableCall = variableCall;
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, FunctionCall functionCall) {
        this(parent, position, isMinus);
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
    public Variable evaluate() {
        return null; //TODO
    }
}
