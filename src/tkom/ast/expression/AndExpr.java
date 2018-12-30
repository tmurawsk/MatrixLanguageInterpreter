package tkom.ast.expression;

import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class AndExpr extends LogicExpression {
    public LinkedList<RelationExpr> relationExprs;

    public AndExpr(Statement parent, RelationExpr expr) {
        super(parent);
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
