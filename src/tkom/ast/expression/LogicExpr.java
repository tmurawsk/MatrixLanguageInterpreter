package tkom.ast.expression;

import tkom.Position;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class LogicExpr extends LogicExpression {
    private LinkedList<AndExpr> andExprs;

    public LogicExpr(Statement parent, Position position, AndExpr expr) {
        super(parent, position);
        andExprs = new LinkedList<>();
        andExprs.add(expr);
    }

    public void addAndExpression(AndExpr andExpr) {
        andExprs.add(andExpr);
    }

    @Override
    public boolean evaluate() {
        return false; //TODO
    }
}
