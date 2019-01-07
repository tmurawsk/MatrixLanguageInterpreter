package tkom.ast.expression;

import tkom.Position;
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
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, MathExpression expression) {
        this(parent, position, isMinus);
        this.expression = expression;
        this.type = expression.getType();
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, VariableCall variableCall) {
        this(parent, position, isMinus);
        this.variableCall = variableCall;
        this.type = variableCall.getType();
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, FunctionCall functionCall) {
        this(parent, position, isMinus);
        this.functionCall = functionCall;
        this.type = functionCall.getType();
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
