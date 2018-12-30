package tkom.ast.expression;

import tkom.ast.statement.Statement;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private MathExpr mathExpr;

    public BaseLogicExpr(Statement parent, MathExpr expr) {
        super(parent);
        isNegation = false;
        mathExpr = expr;
    }

    public void setNegation() {
        isNegation = true;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
