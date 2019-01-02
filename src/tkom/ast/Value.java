package tkom.ast;

import tkom.ast.expression.MathExpr;

public class Value {
    private MathExpr expression;

    private int number;

    public Value(MathExpr expression) {
        this.expression = expression;
    }

    public Value(int number) {
        this.number = number;
    }

    public int evaluate() {
        return expression == null ? number : expression.evaluate().get(0, 0);
    }
}