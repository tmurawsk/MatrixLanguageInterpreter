package tkom.ast.expression;

import tkom.Position;
import tkom.ast.statement.Statement;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private MathExpr mathExpr;
    private LogicExpr logicExpr;

    private BaseLogicExpr(Statement parent, Position position, boolean isNegation) {
        super(parent, position);
        this.isNegation = isNegation;
    }

    public BaseLogicExpr(Statement parent, Position position, boolean isNegation, MathExpr expr) {
        this(parent, position, isNegation);
        mathExpr = expr;
    }

    public BaseLogicExpr(Statement parent, Position position, boolean isNegation, LogicExpr expr) {
        this(parent, position, isNegation);
        logicExpr = expr;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
