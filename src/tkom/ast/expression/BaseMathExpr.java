package tkom.ast.expression;

import tkom.ast.Variable;
import tkom.ast.VariableCall;
import tkom.ast.statement.Statement;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private VariableCall variableCall;

    private BaseMathExpr(Statement parent) {
        super(parent);
    }

    public BaseMathExpr(Statement parent, boolean isMinus, MathExpression expression) {
        this(parent);
        this.isMinus = isMinus;
        this.expression = expression;
    }

    public BaseMathExpr(Statement parent, boolean isMinus, VariableCall variableCall) {
        this(parent);
        this.isMinus = isMinus;
        this.variableCall = variableCall;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
