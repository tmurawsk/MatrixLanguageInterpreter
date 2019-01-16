package tkom.ast.expression;

import tkom.Position;
import tkom.exception.ExecutionException.ExecutionException;

import java.util.LinkedList;

public class LogicExpr extends LogicExpression {
    private LinkedList<AndExpr> andExprs;

    public LogicExpr(Position position, AndExpr expr) {
        super(position);
        andExprs = new LinkedList<>();
        andExprs.add(expr);
    }

    public void addAndExpression(AndExpr andExpr) {
        andExprs.add(andExpr);
    }

    @Override
    public boolean evaluate() throws ExecutionException {
        for (AndExpr expr : andExprs) {
            if (expr.evaluate())
                return true;
        }
        return false;
    }
}
