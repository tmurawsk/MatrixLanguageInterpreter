package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.exception.ExecutionException.ExecutionException;
import tkom.exception.ExecutionException.MathException;
import tkom.exception.ExecutionException.TypeMismatchException;

import java.util.LinkedList;

public class MultExpr extends MathExpression {
    private LinkedList<BaseMathExpr> baseMathExprs;

    private LinkedList<TokenID> multOps;

    public MultExpr(Position position, BaseMathExpr expr) {
        super(position);
        multOps = new LinkedList<>();
        baseMathExprs = new LinkedList<>();
        baseMathExprs.add(expr);
        type = TokenID.Any;
    }

    public void addMultiply(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Multiply);
    }

    public void addDivide(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Divide);
    }

    @Override
    TokenID evaluateType() {
        TokenID firstType = baseMathExprs.getFirst().getType();

        for (BaseMathExpr expr : baseMathExprs)
            if (firstType == TokenID.Mat || expr.getType() == TokenID.Mat)
                firstType = TokenID.Mat;

        return firstType;
    }

    @Override
    public Variable evaluate() throws ExecutionException {
        if (multOps.isEmpty())
            return baseMathExprs.get(0).evaluate();

        Variable result = baseMathExprs.get(0).evaluate();
        for (int i = 1; i < baseMathExprs.size(); i++) {
            try {
                if (multOps.get(i - 1) == TokenID.Multiply)
                    result = result.multiply(baseMathExprs.get(i).evaluate());
                else
                    result = result.divide(baseMathExprs.get(i).evaluate());
            } catch (ExecutionException e) {
                throw e.setPosition(baseMathExprs.get(i).getPosition());
            }
        }
        return result;
    }
}
