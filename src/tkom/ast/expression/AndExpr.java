package tkom.ast.expression;

import tkom.Position;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class AndExpr extends LogicExpression {
    public LinkedList<RelationExpr> relationExprs;

    public AndExpr(Statement parent, Position position, RelationExpr expr) {
        super(parent, position);
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
