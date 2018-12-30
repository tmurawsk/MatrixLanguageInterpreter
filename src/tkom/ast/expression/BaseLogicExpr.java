package tkom.ast.expression;

import tkom.ast.statement.Statement;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private MathExpr mathExpr;
    private LogicExpr logicExpr;

    public BaseLogicExpr(Statement parent, MathExpr expr) {
        super(parent);
        isNegation = false;
        mathExpr = expr;
    }

    public BaseLogicExpr(Statement parent, LogicExpr expr) {
        super(parent);
        isNegation = false;
        logicExpr = expr;
    }

    public void setNegation() {
        isNegation = true;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
