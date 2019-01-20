package tkom.ast;

import tkom.TokenID;
import tkom.ast.expression.MathExpr;
import tkom.exception.ExecutionException.ExecutionException;

public class Value {
    private MathExpr expression;

    private int number;

    public Value(MathExpr expression) {
        this.expression = expression;
    }

    public Value(int number) {
        this.number = number;
    }

    public int evaluate() throws ExecutionException {
        return expression == null ? number : expression.evaluate().getInt();
    }

    public TokenID getType() {
        return expression == null ? TokenID.Num : expression.getType();
    }
}
