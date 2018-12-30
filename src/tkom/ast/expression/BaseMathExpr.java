package tkom.ast.expression;

import tkom.ast.Variable;
import tkom.ast.statement.Statement;

public class BaseMathExpr extends MathExpression {
    private boolean isMinus;
    private MathExpression expression;
    private Variable variable;

    private BaseMathExpr(Statement parent) {
        super(parent);
        isMinus = false;
    }

    public BaseMathExpr(Statement parent, MathExpression expression) {
        this(parent);
        this.expression = expression;
    }

    public BaseMathExpr(Statement parent, Variable variable) {
        this(parent);
        this.variable = variable;
    }

    public void setMinus() {
        isMinus = true;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
