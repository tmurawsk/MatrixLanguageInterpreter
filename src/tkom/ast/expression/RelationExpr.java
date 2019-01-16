package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.exception.ExecutionException.ExecutionException;

public class RelationExpr extends LogicExpression {
    private BaseLogicExpr leftExpr;
    private BaseLogicExpr rightExpr;
    private TokenID operator;

    public RelationExpr(Position position, BaseLogicExpr leftExpr) {
        super(position);
        this.leftExpr = leftExpr;
    }

    public RelationExpr(Position position, BaseLogicExpr leftExpr, TokenID operator, BaseLogicExpr rightExpr) {
        this(position, leftExpr);
        this.operator = operator;
        this.rightExpr = rightExpr;
    }

    @Override
    public boolean evaluate() throws ExecutionException {
        if (rightExpr == null)
            return leftExpr.evaluate();

        switch (operator) {
            case Equal:
                return leftExpr.evaluateMath().equals(rightExpr.evaluateMath());
            case Unequal:
                return !leftExpr.evaluateMath().equals(rightExpr.evaluateMath());
            case Greater:
                return leftExpr.evaluateMath().greaterThan(rightExpr.evaluateMath());
            case Less:
                return leftExpr.evaluateMath().lowerThan(rightExpr.evaluateMath());
            case GreaterOrEqual:
                return !leftExpr.evaluateMath().lowerThan(rightExpr.evaluateMath());
            case LessOrEqual:
                return !leftExpr.evaluateMath().greaterThan(rightExpr.evaluateMath());
        }

        return false;
    }
}
