package tkom.ast.expression;

import tkom.TokenID;
import tkom.ast.statement.Statement;

public class RelationExpr extends LogicExpression {
    public BaseLogicExpr leftExpr;
    public BaseLogicExpr rightExpr;
    public TokenID operator;

    public RelationExpr(Statement parent, TokenID operator, BaseLogicExpr leftExpr, BaseLogicExpr rightExpr) {
        super(parent);
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        this.operator = operator;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
