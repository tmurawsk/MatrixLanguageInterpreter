package tkom.ast.expression;

import tkom.TokenID;
import tkom.ast.statement.Statement;

public class RelationExpr extends LogicExpression {
    public BaseLogicExpr leftExpr;
    public BaseLogicExpr rightExpr;
    public TokenID operator;

    public RelationExpr(Statement parent, BaseLogicExpr leftExpr) {
        super(parent);
        this.leftExpr = leftExpr;
    }

    public RelationExpr(Statement parent, BaseLogicExpr leftExpr, TokenID operator, BaseLogicExpr rightExpr) {
        this(parent, leftExpr);
        this.operator = operator;
        this.rightExpr = rightExpr;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
