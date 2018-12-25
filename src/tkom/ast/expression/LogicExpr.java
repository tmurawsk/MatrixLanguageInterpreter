package tkom.ast.expression;

import java.util.LinkedList;

public class LogicExpr {
    private LinkedList<AndExpr> andExprs;

    public LogicExpr(AndExpr expr) {
        andExprs = new LinkedList<>();
        andExprs.add(expr);
    }
}
