package tkom.ast.statement;

import tkom.ast.expression.MathExpr;

public class ReturnStatement extends Statement {
    private MathExpr expression;

    public ReturnStatement(Statement parent, MathExpr expression) {
        super(parent);
        this.expression = expression;
    }

    @Override
    public void execute() {
        //TODO
    }
}
