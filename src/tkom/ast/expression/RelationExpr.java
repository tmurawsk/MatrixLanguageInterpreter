package tkom.ast.expression;

import tkom.TokenID;

public class RelationExpr {
    public BaseLogicExpr leftExpr;
    public BaseLogicExpr rightExpr;
    public TokenID relationOp;

    public RelationExpr(BaseLogicExpr leftExpr) {
        this.leftExpr = leftExpr;
    }
}
