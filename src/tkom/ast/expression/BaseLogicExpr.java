package tkom.ast.expression;

import tkom.Position;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private MathExpr mathExpr;
    private LogicExpr logicExpr;

    private BaseLogicExpr(Position position, boolean isNegation) {
        super(position);
        this.isNegation = isNegation;
    }

    public BaseLogicExpr(Position position, boolean isNegation, MathExpr expr) {
        this(position, isNegation);
        mathExpr = expr;
    }

    public BaseLogicExpr(Position position, boolean isNegation, LogicExpr expr) {
        this(position, isNegation);
        logicExpr = expr;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
