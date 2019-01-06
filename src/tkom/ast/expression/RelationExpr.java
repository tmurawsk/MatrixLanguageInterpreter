package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.statement.Statement;

public class RelationExpr extends LogicExpression {
    private BaseLogicExpr leftExpr;
    private BaseLogicExpr rightExpr;
    private TokenID operator;

    public RelationExpr(Statement parent, Position position, BaseLogicExpr leftExpr) {
        super(parent, position);
        this.leftExpr = leftExpr;
    }

    public RelationExpr(Statement parent, Position position, BaseLogicExpr leftExpr, TokenID operator, BaseLogicExpr rightExpr) {
        this(parent, position, leftExpr);
        this.operator = operator;
        this.rightExpr = rightExpr;
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
