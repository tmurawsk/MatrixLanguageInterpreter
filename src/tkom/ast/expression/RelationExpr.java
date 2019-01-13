package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;

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
    public boolean evaluate() {
        return false; //TODO
    }
}
