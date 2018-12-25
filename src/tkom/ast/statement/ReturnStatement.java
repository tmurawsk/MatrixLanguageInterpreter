package tkom.ast.statement;

import tkom.ast.expression.MathExpr;

public class ReturnStatement implements Statement {
    private MathExpr expression;

    public ReturnStatement(MathExpr expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
