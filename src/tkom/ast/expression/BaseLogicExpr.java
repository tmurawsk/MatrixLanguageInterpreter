package tkom.ast.expression;

public class BaseLogicExpr {
    boolean isNegation;
    MathExpr mathExpr;

    public BaseLogicExpr() {
        isNegation = false;
    }

    public BaseLogicExpr(MathExpr expr) {
        this();
        mathExpr = expr;
    }
}
