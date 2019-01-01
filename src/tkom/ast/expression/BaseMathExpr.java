package tkom.ast.expression;

import tkom.ast.Variable;
import tkom.ast.statement.Statement;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private Variable variable;

    private BaseMathExpr(Statement parent) {
        super(parent);
    }

    public BaseMathExpr(Statement parent, boolean isMinus, MathExpression expression) {
        this(parent);
        this.isMinus = isMinus;
        this.expression = expression;
    }

    public BaseMathExpr(Statement parent, boolean isMinus, Variable variable) {
        this(parent);
        this.isMinus = isMinus;
        this.variable = variable;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
