package tkom.ast.expression;

import tkom.Position;
import tkom.ast.Variable;
import tkom.ast.VariableCall;
import tkom.ast.statement.Statement;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private VariableCall variableCall;

    private BaseMathExpr(Statement parent, Position position, boolean isMinus) {
        super(parent, position);
        this.isMinus = isMinus;
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, MathExpression expression) {
        this(parent, position, isMinus);
        this.isMinus = isMinus;
        this.expression = expression;
        this.type = expression.getType();
    }

    public BaseMathExpr(Statement parent, Position position, boolean isMinus, VariableCall variableCall) {
        this(parent, position, isMinus);
        this.isMinus = isMinus;
        this.variableCall = variableCall;
        this.type = variableCall.getType();
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
