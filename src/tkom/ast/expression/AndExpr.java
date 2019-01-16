package tkom.ast.expression;

import tkom.Position;
import tkom.exception.ExecutionException.ExecutionException;

import java.util.LinkedList;

public class AndExpr extends LogicExpression {
    private LinkedList<RelationExpr> relationExprs;

    public AndExpr(Position position, RelationExpr expr) {
        super(position);
        relationExprs = new LinkedList<>();
        relationExprs.add(expr);
    }

    public void addRelationExpr(RelationExpr expr) {
        relationExprs.add(expr);
    }

    @Override
    public boolean evaluate() throws ExecutionException {
        for (RelationExpr expr : relationExprs) {
            if (!expr.evaluate())
                return false;
        }
        return true;
    }
}
