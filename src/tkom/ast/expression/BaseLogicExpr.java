package tkom.ast.expression;

import tkom.Position;
import tkom.ast.Variable;
import tkom.exception.ExecutionException.ExecutionException;

public class BaseLogicExpr extends LogicExpression {
    private boolean isNegation;
    private LogicExpr logicExpr;
    private MathExpr mathExpr;

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

    Variable evaluateMath() throws ExecutionException {
        return mathExpr.evaluate();
    }

    @Override
    public boolean evaluate() throws ExecutionException {
        return isNegation != logicExpr.evaluate();
    }
}
