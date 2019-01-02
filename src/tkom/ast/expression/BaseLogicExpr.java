package tkom.ast.expression;

import tkom.ast.statement.Statement;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private MathExpr mathExpr;
    private LogicExpr logicExpr;

    public BaseLogicExpr(Statement parent, boolean isNegation, MathExpr expr) {
        super(parent);
        this.isNegation = isNegation;
        mathExpr = expr;
    }

    public BaseLogicExpr(Statement parent, boolean isNegation, LogicExpr expr) {
        super(parent);
        this.isNegation = isNegation;
        logicExpr = expr;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
