package tkom.ast.expression;

import tkom.ast.Variable;

public class BaseMathExpr {
    boolean isMinus;
    MathExpr mathExpr;
    Variable variable;
    //TODO function call

    public BaseMathExpr() {
        isMinus = false;
    }

    public BaseMathExpr(MathExpr expr) {
        this();
        this.mathExpr = expr;
    }

    public BaseMathExpr(Variable variable) {
        this.variable = variable;
    }
}
