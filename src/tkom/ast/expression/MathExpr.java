package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.exception.ExecutionException.ExecutionException;

import java.util.LinkedList;

public class MathExpr extends MathExpression {
    private LinkedList<MultExpr> multExprs;

    private LinkedList<TokenID> addOps;

    public MathExpr(Position position, MultExpr expression) {
        super(position);
        addOps = new LinkedList<>();
        multExprs = new LinkedList<>();
        multExprs.add(expression);
        type = TokenID.Any;
    }

    public void addPlus(MultExpr expr) {
        multExprs.add(expr);
        addOps.add(TokenID.Plus);
    }

    public void addMinus(MultExpr expr) {
        multExprs.add(expr);
        addOps.add(TokenID.Minus);
    }

    @Override
    TokenID evaluateType() {
        return multExprs.getFirst().getType();
    }

    @Override
    public Variable evaluate() throws ExecutionException {
        if (addOps.isEmpty())
            return multExprs.get(0).evaluate();

        Variable result = multExprs.get(0).evaluate();
        for (int i = 1; i < multExprs.size(); i++) {
            try {
                if (addOps.get(i - 1) == TokenID.Plus)
                    result = result.add(multExprs.get(i).evaluate());
                else
                    result = result.subtract(multExprs.get(i).evaluate());
            } catch (ExecutionException e) {
                throw e.setPosition(multExprs.get(i).getPosition());
            }
        }
        return result;
    }
}
