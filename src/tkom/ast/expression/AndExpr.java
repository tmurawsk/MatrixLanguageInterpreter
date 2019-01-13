package tkom.ast.expression;

import tkom.Position;

import java.util.LinkedList;

public class AndExpr extends LogicExpression {
    public LinkedList<RelationExpr> relationExprs;

    public AndExpr(Position position, RelationExpr expr) {
        super(position);
        relationExprs = new LinkedList<>();
        relationExprs.add(expr);
    }

    public void addRelationExpr(RelationExpr expr) {
        relationExprs.add(expr);
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
