package tkom.ast.expression;

import java.util.LinkedList;

public class AndExpr {
    public LinkedList<RelationExpr> relationExprs;

    public AndExpr(RelationExpr expr) {
        relationExprs = new LinkedList<>();
        relationExprs.add(expr);
    }
}
