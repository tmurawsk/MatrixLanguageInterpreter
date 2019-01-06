package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;
import tkom.exception.TypeMismatchException;

import java.util.LinkedList;

public class MathExpr extends MathExpression {
    private LinkedList<MultExpr> multExprs;

    private LinkedList<TokenID> addOps;

    public MathExpr(Statement parent, Position position, MultExpr expression) {
        super(parent, position);
        addOps = new LinkedList<>();
        multExprs = new LinkedList<>();
        multExprs.add(expression);
        type = expression.getType();
    }

    public void addPlus(MultExpr expr) throws TypeMismatchException {
        multExprs.add(expr);
        addOps.add(TokenID.Plus);
        updateType(expr);
    }

    public void addMinus(MultExpr expr) throws TypeMismatchException {
        multExprs.add(expr);
        addOps.add(TokenID.Minus);
        updateType(expr);
    }

    private void updateType(MultExpr expr) throws TypeMismatchException {
        if (type != expr.getType())
            throw new TypeMismatchException(expr.getPosition(), type, expr.getType());
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
