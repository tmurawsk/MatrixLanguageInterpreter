package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class MultExpr extends MathExpression {
    private LinkedList<BaseMathExpr> baseMathExprs;

    private LinkedList<TokenID> multOps;

    public MultExpr(Statement parent, Position position, BaseMathExpr expr) {
        super(parent, position);
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
    public Variable evaluate() {
        return null; //TODO
    }
}
